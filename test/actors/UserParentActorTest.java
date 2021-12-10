package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.google.inject.Inject;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.libs.ws.WSClient;

public class UserParentActorTest {
    static ActorSystem system;
    private static Application testApp;
    @Inject
    private WSClient ws;


    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
//        testApp = new GuiceApplicationBuilder()
//                .overrides(bind(UserActor.class))
//                .build();
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testUserActor() {
        new TestKit(system) {
            {
                // simple actor which only forwards messages
                class Forwarder extends AbstractActor {
                    final ActorRef target;

                    @SuppressWarnings("unused")
                    public Forwarder(ActorRef target) {
                        this.target = target;
                    }

                    @Override
                    public Receive createReceive() {
                        return receiveBuilder()
                                .matchAny(message -> target.forward(message, getContext()))
                                .build();
                    }
                }

                // create a test probe
                final TestKit probe = new TestKit(system);

                // create a forwarder, injecting the probe’s testActor
                final Props props = Props.create(Forwarder.class, this, probe.getRef());
                final ActorRef forwarder = system.actorOf(props, "forwarder");

                // verify correct forwarding
                forwarder.tell(42, getRef());
                probe.expectMsgEquals(42);
                Assert.assertEquals(getRef(), probe.getLastSender());
            }
        };
    }
}