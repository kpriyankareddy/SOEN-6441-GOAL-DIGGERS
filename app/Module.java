import actors.*;
import akka.actor.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.Adapter;
import akka.stream.Materializer;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.typesafe.config.Config;
import play.libs.akka.AkkaGuiceSupport;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@SuppressWarnings("unused")
public class Module extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        bind(new TypeLiteral<ActorRef<PostsActor.GetPosts>>() {})
            .toProvider(StocksActorProvider.class)
            .asEagerSingleton();
        bind(new TypeLiteral<ActorRef<UserParentActor.Create>>() {})
            .toProvider(UserParentActorProvider.class)
            .asEagerSingleton();
        bind(UserActor.Factory.class).toProvider(UserActorFactoryProvider.class);
    }

    @Singleton
    public static class StocksActorProvider implements Provider<ActorRef<PostsActor.GetPosts>> {
        private final ActorSystem actorSystem;

        @Inject
        public StocksActorProvider(ActorSystem actorSystem) {
            this.actorSystem = actorSystem;
        }

        @Override
        public ActorRef<PostsActor.GetPosts> get() {
            return Adapter.spawn(
                actorSystem,
                PostsActor.create(),
                "stocksActor");
        }
    }

    @Singleton
    public static class UserParentActorProvider implements Provider<ActorRef<UserParentActor.Create>> {
        private final ActorSystem actorSystem;
        private final UserActor.Factory childFactory;
        private final Config config;

        @Inject
        public UserParentActorProvider(
            ActorSystem actorSystem, UserActor.Factory childFactory, Config config
        ) {
            this.actorSystem = actorSystem;
            this.childFactory = childFactory;
            this.config = config;
        }

        @Override
        public ActorRef<UserParentActor.Create> get() {
            return Adapter.spawn(
                actorSystem,
                UserParentActor.create(childFactory, config),
                "userParentActor");
        }
    }

    @Singleton
    public static class UserActorFactoryProvider implements Provider<UserActor.Factory> {
        private final ActorRef<PostsActor.GetPosts> stocksActor;

        @Inject
        public UserActorFactoryProvider(ActorRef<PostsActor.GetPosts> stocksActor, Materializer mat) {
            this.stocksActor = stocksActor;
        }

        @Override
        public UserActor.Factory get() {
            return id -> UserActor.create(id, stocksActor);
        }
    }
}
