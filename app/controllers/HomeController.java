package controllers;

import actors.UserParentActor;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.model.Query;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.databind.JsonNode;
import models.Sentiment;
import org.slf4j.Logger;
import org.webjars.play.WebJarsUtil;
import play.libs.F.Either;
import play.mvc.*;
import services.UserInfoService;
import services.WordStatsService;
import stocks.SubmissionGenerator;
import views.html.Main.User;
import views.html.Main.WordStats;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * The main web controller that handles returning the index page, setting up a WebSocket, and watching a stock.
 */
@Singleton
public class HomeController extends Controller {

    private final Duration timeout = Duration.ofSeconds(1);
    private final Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.HomeController");
    private final ActorRef<UserParentActor.Create> userParentActor;
    private final ActorSystem system;

    private WebJarsUtil webJarsUtil;

    @Inject
    UserInfoService userInfoService;

    @Inject
    public HomeController(ActorRef<UserParentActor.Create> userParentActor, ActorSystem system, WebJarsUtil webJarsUtil) {
        this.userParentActor = userParentActor;
        this.system = system;
        this.webJarsUtil = webJarsUtil;
    }

    public Result index(Http.Request request) { // Step 0: Changed UI and refactored - DONE
        System.out.println("Updating index");
        return ok(views.html.index.render(request, webJarsUtil));
    }

    public Result clear(Http.Request request) { // Step 0: Changed UI and refactored - DONE
        System.out.println("Updating index");
        Sentiment.clearSentiments();
        SubmissionGenerator.clearPosts();
        return ok(views.html.index.render(request, webJarsUtil));
    }

    public Result redirectUser(String user) {
        return ok(User.render(userInfoService.getUserDetails(user), userInfoService.getUserSubmissions(user)));
    }

    public Result getWordStats() {
        Map<String, Integer> word_stats = WordStatsService.getWordStatisticsMap();

        return ok(WordStats.render(word_stats));
    }

    public WebSocket ws() {
        System.out.println("Websocket was called!");
        return WebSocket.Json.acceptOrResult(request -> {
            if (sameOriginCheck(request)) {
                final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request);
                final CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = future.thenApply(Either::Right);
                return stage.exceptionally(this::logException);
            } else {
                return forbiddenResult();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request) {
        System.out.println("Future flow was called");
        String id = Long.toString(request.asScala().id());
        Scheduler scheduler = Adapter.toTyped(system.scheduler());
        return AskPattern.<UserParentActor.Create, Flow<JsonNode, JsonNode, NotUsed>>ask(
            userParentActor, replyTo -> new UserParentActor.Create(id, replyTo), timeout, scheduler
        ).thenApply(f -> f.named("websocket"));
    }

    private CompletionStage<Either<Result, Flow<JsonNode, JsonNode, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final Either<Result, Flow<JsonNode, JsonNode, ?>> left = Either.Left(forbidden);

        return CompletableFuture.completedFuture(left);
    }

    private Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
        logger.error("Cannot create websocket", throwable);
        Result result = Results.internalServerError("error");
        return Either.Left(result);
    }

    /**
     * Checks that the WebSocket comes from the same origin.  This is necessary to protect
     * against Cross-Site WebSocket Hijacking as WebSocket does not implement Same Origin Policy.
     * <p>
     * See https://tools.ietf.org/html/rfc6455#section-1.3 and
     * http://blog.dewhurstsecurity.com/2013/08/30/security-testing-html5-websockets.html
     */
    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (! origin.isPresent()) {
            logger.error("originCheck: rejecting request because no Origin header found");
            return false;
        } else if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin: "
                + String.join(", ", validOrigins));
            return false;
        }
    }

    private List<String> validOrigins = Arrays.asList("localhost:9000", "localhost:19001");
    private boolean originMatches(String actualOrigin) {
        return validOrigins.stream().anyMatch(actualOrigin::contains);
    }

}
