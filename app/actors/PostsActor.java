package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import stocks.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * This actor contains a set of stocks internally that may be used by
 * all websocket clients.
 */
public final class PostsActor {
  private PostsActor() {}

  public static class Queries {
    final Set<Query> submissions;

    public Queries(Set<Query> submissions) {
      this.submissions = requireNonNull(submissions);
    }
  }

  public static final class GetPosts {
    final Set<String> queries;
    final ActorRef<Queries> replyTo;

    public GetPosts(Set<String> symbols, ActorRef<Queries> replyTo) {
      this.queries = requireNonNull(symbols);
      this.replyTo = requireNonNull(replyTo);
    }

    @Override
    public String toString() {
      return "GetStocks(" + queries + ")";
    }
  }

  public static Behavior<GetPosts> create() {
    Map<String, Query> postsMap = new HashMap<>();
    return Behaviors.logMessages(
        Behaviors
            .receive(GetPosts.class)
            .onMessage(GetPosts.class, getPosts -> {
              System.out.println("Inside posts actor: Creating new stock reference? "+getPosts.queries); // OP:  [GOOG, AAPL, ORCL]
                System.out.println("Given reference: "+getPosts.replyTo.path());
              Set<Query> queries = getPosts.queries.stream()
                  .map(symbol -> postsMap.compute(symbol, (k, v) -> new Query(k)))
                  .collect(Collectors.toSet());
              System.out.println("New set of symbols inside stocks actor: "+ queries); // OP: [Stock(ORCL), Stock(AAPL), Stock(GOOG)]
              getPosts.replyTo.tell(new Queries(queries));
              return Behaviors.same();
            })
            .build()
    );
  }
}
