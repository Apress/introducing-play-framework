package config;

import play.http.HttpErrorHandler;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

/**
 * A simple error handler
 */

@Singleton
public class CustomErrorHandler implements HttpErrorHandler {
    public CompletionStage<Result> onClientError(
            RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(
                Results.status(statusCode, "Invalid Request " + message));
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(
                Results.internalServerError("Cannot process the request due to  " + exception.getMessage()));
    }
}