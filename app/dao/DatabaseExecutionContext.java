package dao;

import akka.actor.ActorSystem;
import play.db.Database;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DatabaseExecutionContext extends CustomExecutionContext {

    @javax.inject.Inject
    public DatabaseExecutionContext(ActorSystem actorSystem) {
        // uses a custom thread pool defined in application.conf
        super(actorSystem, "database.dispatcher");
    }

    @Singleton
    public static class JdbcExample {

        private Database db;
        private DatabaseExecutionContext  executionContext;

        @Inject
        public JdbcExample(Database db, DatabaseExecutionContext  context) {
            this.db = db;
            this.executionContext = executionContext;
        }
        public CompletionStage<Integer> updateSomething() {
            return CompletableFuture.supplyAsync(
                    () -> {
                        return db.withConnection(
                                connection -> {
                                    //perform the operations with the connection
                                    return 1;
                                });
                    },
                    executionContext);
        }
    }
}