import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public ScheduledTask(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
        this.actorSystem
                .scheduler()
                .scheduleAtFixedRate(
                        Duration.create(30, TimeUnit.SECONDS), // initialDelay
                        Duration.create(1, TimeUnit.MINUTES), // interval
                        () -> actorSystem.log().info("Time in millis now "+System.currentTimeMillis()),
                        this.executionContext);
    }
}
