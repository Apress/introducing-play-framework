package actors;

import akka.actor.*;

public class PingActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(PingActor.class);
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        ActorModel.Ping.class,
                        ping -> {
                            String reply = "Hello, " + ping.msg;
                            sender().tell(reply, self());
                        })
                .build();
    }
}
