package actors;

import actors.PostsActor.Queries;
import actors.PostsActor.GetPosts;
import akka.Done;
import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.japi.Pair;
import akka.stream.KillSwitches;
import akka.stream.Materializer;
import akka.stream.UniqueKillSwitch;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.databind.JsonNode;
import models.Submission;
import play.libs.Json;
import stocks.Query;

import javax.inject.Inject;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static akka.actor.typed.javadsl.AskPattern.ask;
import static java.util.Objects.requireNonNull;

/**
 * The broker between the WebSocket and the StockActor(s).  The UserActor holds the connection and sends serialized
 * JSON data to the client.
 */
public class UserActor {
    public interface Message {}

    public static final class WatchPosts implements Message {
        final Set<String> queries;
        final ActorRef<Flow<JsonNode, JsonNode, NotUsed>> replyTo;

        public WatchPosts(Set<String> queries, ActorRef<Flow<JsonNode, JsonNode, NotUsed>> replyTo) {
            this.queries = requireNonNull(queries);
            this.replyTo = requireNonNull(replyTo);
        }

        @Override
        public String toString() {
            return "WatchPosts(" + queries + ", " + replyTo + ")";
        }
    }

    public static final class UnwatchPosts implements Message {
        final Set<String> symbols;

        public UnwatchPosts(Set<String> symbols) {
            this.symbols = requireNonNull(symbols);
        }

        @Override
        public String toString() {
            return "UnwatchPosts(" + symbols + ")";
        }
    }

    private static final class InternalStop implements Message {
        private static final InternalStop INSTANCE = new InternalStop();
        public static InternalStop get() {
            return INSTANCE;
        }
        private InternalStop() {}
    }

    private static final Map<String, List<Submission>> posts = new LinkedHashMap<>();

    private final Duration timeout = Duration.of(1000, ChronoUnit.MILLIS);

    private final Map<String, UniqueKillSwitch> postsMap = new HashMap<>(); // The single source of truth



    private final String id;
    private final ActorRef<GetPosts> postsActor;
    private final Materializer mat;
    private final Scheduler scheduler;
    private final ActorContext<Message> context;

    private final Sink<JsonNode, NotUsed> hubSink;
    private final Flow<JsonNode, JsonNode, NotUsed> websocketFlow;

    public static Behavior<Message> create(String id, ActorRef<GetPosts> postsActor) {
        return Behaviors.setup(context -> new UserActor(id, postsActor, context).behavior());
    }

    @Inject
    public UserActor(String id,
                     ActorRef<GetPosts> postsActor,
                     ActorContext<Message> context) {
        this.id = id;
        this.postsActor = postsActor;
        this.mat = Materializer.matFromSystem(context.getSystem());
        this.scheduler = context.getSystem().scheduler();
        this.context = context;

        Pair<Sink<JsonNode, NotUsed>, Source<JsonNode, NotUsed>> sinkSourcePair =
                MergeHub.of(JsonNode.class, 16)
                .toMat(BroadcastHub.of(JsonNode.class, 256), Keep.both())
                .run(mat);

        this.hubSink = sinkSourcePair.first();
        Source<JsonNode, NotUsed> hubSource = sinkSourcePair.second();

        Sink<JsonNode, CompletionStage<Done>> jsonSink = Sink.foreach((JsonNode json) -> {
            // When the user types in a stock in the upper right corner, this is triggered,
            System.out.println("Adding a new stock that the user asked - UserActor.java - "+json.findPath("symbol").asText());
            String symbol = json.findPath("symbol").asText();
            addPosts(Collections.singleton(symbol));
        });

        // Put the source and sink together to make a flow of hub source as output (aggregating all
        // stocks as JSON to the browser) and the actor as the sink (receiving any JSON messages
        // from the browse), using a coupled sink and source.
        this.websocketFlow = Flow.fromSinkAndSourceCoupled(jsonSink, hubSource)
                //.log("actorWebsocketFlow", logger)
                .watchTermination((n, stage) -> {
                    // When the flow shuts down, make sure this actor also stops.
                    context.pipeToSelf(stage, (Done _done, Throwable _throwable) -> InternalStop.get());
                    return NotUsed.getInstance();
                });
    }

    public Behavior<Message> behavior() {
        return Behaviors
            .receive(Message.class)
            .onMessage(WatchPosts.class, watchPosts -> {
                context.getLog().info("Received message {}", watchPosts);
                addPosts(watchPosts.queries);
                System.out.println("Gonna finally tell the websocket that the data has been updated");
                System.out.println(watchPosts.replyTo.path());
                watchPosts.replyTo.tell(websocketFlow);
                return Behaviors.same();
            })
            .onMessage(UnwatchPosts.class, unwatchPosts -> {
                context.getLog().info("Received message {}", unwatchPosts);
                unwatchPosts(unwatchPosts.symbols);
                return Behaviors.same();
            })
            .onMessageEquals(InternalStop.get(), Behaviors::stopped)
            .onSignal(PostStop.class, _postStop -> {
                // If this actor is killed directly, stop anything that we started running explicitly.
                context.getLog().info("Stopping actor {}", context.getSelf());
                unwatchPosts(postsMap.keySet());
                return Behaviors.same();
            })
            .build();
    }

    /**
     * Adds several stocks to the hub, by asking the stocks actor for stocks.
     */
    private void addPosts(Set<String> queries) {
        // Ask the stocksActor for a stream containing these stocks.
        System.out.println("Add stocks received queries: "+queries); // QUERIES WILL ALWAYS BE SINGLETON i.e only one query (user typed from form) will be present inside
        CompletionStage<Queries> future = ask(postsActor, replyTo -> new GetPosts(queries, replyTo), timeout, scheduler);

        System.out.println("Stock map currently has: "+ postsMap.keySet()); // Next calls StocksActor "Inside Stock actor" via addPost

        // when we get the response back, we want to turn that into a flow by creating a single
        // source and a single sink, so we merge all of the stock sources together into one by
        // pointing them to the hubSink, so we can add them dynamically even after the flow
        // has started.
        future.thenAccept((Queries inputQueries) -> {
            System.out.println("Current Posts map: "+postsMap);
            inputQueries.submissions.forEach(query -> { // query will be a singular entity just having the keyword you entered
                System.out.println("Have stock: "+ query);
                if (!postsMap.containsKey(query.symbol)) {

                    System.out.println("Adding key: "+ query.symbol);
                    addPost(query); // TODO: Step 3: 1. Refactoring to add a new query and its list of submissions
                }
            });
        });
    }

    public static Map<String, List<Submission>> getPosts(){
        return posts;
    }

    /**
     * Adds a single stock to the hub.
     */
    private void addPost(Query query) {
        System.out.println("Adding a new query: "+ query);
        // We convert everything to JsValue so we get a single stream for the websocket.
        // Make sure the history gets written out before the updates for this stock...
        final Source<JsonNode, NotUsed> historySource = query.history(40).map(Json::toJson);
        final Source<JsonNode, NotUsed> updateSource = query.update().map(Json::toJson);
        final Source<JsonNode, NotUsed> stockSource = historySource.concat(updateSource);


        // Set up a flow that will let us pull out a killswitch for this specific stock,
        // and automatic cleanup for very slow subscribers (where the browser has crashed, etc).
        final Flow<JsonNode, JsonNode, UniqueKillSwitch> killswitchFlow = Flow.of(JsonNode.class)
                .joinMat(KillSwitches.singleBidi(), Keep.right());
        // Set up a complete runnable graph from the stock source to the hub's sink
        String name = "query-" + query + "-" + id;
        final RunnableGraph<UniqueKillSwitch> graph = stockSource
                .viaMat(killswitchFlow, Keep.right())
                .to(hubSink)
                .named(name);

        // Start it up!
        UniqueKillSwitch killSwitch = graph.run(mat);

        // Pull out the kill switch so we can stop it when we want to unwatch a stock.
        postsMap.put(query.symbol, killSwitch);
        System.out.println("New stocks map is: "+ postsMap);
    }

    private void unwatchPosts(Set<String> symbols) {
        symbols.forEach(symbol -> {
            postsMap.get(symbol).shutdown();
            postsMap.remove(symbol);
        });
    }

    public interface Factory {
        Behavior<Message> create(String id);
    }
}
