package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * An example of Action composition
 */
public class EchoAction extends play.mvc.Action.Simple {

    private static final Logger log = LoggerFactory.getLogger(EchoAction.class);

    public CompletionStage<Result> call(Http.Request req) {
        log.info("Request Method {} ", req.method());
        return delegate.call(req);
    }
}
