package actors;

import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;

import java.util.*;

public final class UserParentActor {
    private UserParentActor() {}

    public static final class Create {
        final String id;
        final ActorRef<Flow<JsonNode, JsonNode, NotUsed>> replyTo;

        public Create(String id, ActorRef<Flow<JsonNode, JsonNode, NotUsed>> replyTo) {
            this.id = id;
            this.replyTo = replyTo;
        }
    }

    public static Behavior<Create> create(UserActor.Factory childFactory, Config config) {
        return Behaviors.setup(context -> {
            // Step 1: Change Default code to match posts
//            Set<String> defaultStocks = new HashSet<>(config.getStringList("default.stocks"));
//            System.out.println("Current default stocks: "+defaultStocks);  -> Was : [GOOG, AAPL, ORCL]
            Set<String> defaultQueries = Set.of();// TODO: DONE - Step 1: 1. Changing to fresh new posts from previous default.stocks

            Behavior<Create> behavior = Behaviors.receive(Create.class)
                .onMessage(Create.class, create -> {
                    ActorRef<UserActor.Message> child = context.spawn(childFactory.create(create.id), "userActor-" + create.id);
                    System.out.println("Going to ask to WatchPosts - UserParentActor based on message from HomeController");
                    child.tell(new UserActor.WatchPosts(defaultQueries, create.replyTo)); // TODO: DONE Step 2: Refactor every class expecting old input. NOTE: Went ahead with only maintaining queries and then adding an overall sentiment parameter to Submission
                    return Behaviors.same();
                })
                .build();
            return Behaviors.logMessages(behavior);
        });
    }
}
