package config;

import akka.event.LoggingFilter;
import akka.stream.Materializer;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * A simple filter implementation that logs how long it took to process a request
 */
public class ApplicationFilter extends Filter {

    @Inject
    public ApplicationFilter(Materializer mat) {
        super(mat);
    }
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader,
                              CompletionStage<Result>> next, Http.RequestHeader requestHeader) {

        long startTime = System.currentTimeMillis();
        return next
                .apply(requestHeader)
                .thenApply(
                        result -> {
                            long endTime = System.currentTimeMillis();
                            long requestTime = endTime - startTime;
                            log.info(
                                    "{} {} took {}ms to complete and produced the status {}",
                                    requestHeader.method(),
                                    requestHeader.uri(),
                                    requestTime,
                                    result.status());

                            return result;
                        });
    }
}
