package controllers;

import actors.ActorModel;
import actors.PingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import config.EchoAction;
import dao.ReviewRepository;
import models.GiftVO;
import models.Message;
import models.Review;
import modules.Factorial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import play.cache.AsyncCacheApi;
import play.cache.Cached;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Akka;
import play.libs.XPath;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.*;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import static akka.pattern.Patterns.ask;

/**
 * Main controller
 */
public class Application extends Controller {

    final ActorRef pingActor;
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private AsyncCacheApi cache;
    private ReviewRepository reviewRepo;

    @Inject
    public Application(ActorSystem system, AsyncCacheApi cache, ReviewRepository reviewRepo) {

        pingActor = system.actorOf(PingActor.getProps());
        this.cache = cache;
        this.reviewRepo = reviewRepo;
    }

    @Inject
    Materializer materializer;


    /**
     * Simple ping method, demonstrating the use of Futures and asynchronous processing
     *
     * @param msg
     * @return
     */

    public CompletionStage<Result> ping(String msg) {
        return FutureConverters.toJava(
                    ask(pingActor, new ActorModel.Ping(msg), 1000)
                ).thenApply(response -> ok((String) response));
    }

    /**
     * Method demonstrating processing large http responses in chunks
     *
     * @return
     */
    public CompletionStage<Result> processLargeResponse() {
        CompletionStage<WSResponse> futureResponse =
                ws.url("http://www.mocky.io/v2/5e08df833000005b0081a159")
                        .setMethod("GET").stream();
        CompletionStage<Long> bytesReturned =
                futureResponse.thenCompose(
                        res -> {
                            Source<ByteString, ?> responseBody = res.getBodyAsSource();

                            // Count the number of bytes returned
                            Sink<ByteString, CompletionStage<Long>> bytesSum =
                                    Sink.fold(0L, (total, bytes) -> total + bytes.length());

                            return responseBody.runWith(bytesSum, materializer);
                        });
        return bytesReturned.thenApply(response -> ok((String) response.toString()));
    }

    /**
     * Process the home page
     *
     * @return
     */
    public Result index() {
        System.out.println(fact.fact(10));

        return ok(views.html.bookshop.render());
    }

    /**
     * Get the details of a book by id
     *
     * @param id
     * @return
     */
    public Result getBook(String id) {
        return ok(views.html.bookshop.render());
    }

    @Inject
    FormFactory formFactory;
    @Inject
    Factorial fact;

    /**
     * Accept a form post and save the comment
     * Shows the usage of dynamic forms to retrieve data from html form posts
     *
     * @return
     */
    public Result saveComment(Http.Request request) {
        DynamicForm requestData = formFactory.form().bindFromRequest(request);
        String comment = requestData.get("comment");
        Review review = new Review();
        // We hardcoded the ids, but you can take it as
        //exercise to make book id and user id as request parameters.
        // Also moodify repository to save those two against comment
        review.setBookId("123456789");
        review.setUserId("U1");
        review.setComment(comment);
        review.save(reviewRepo);
        return ok(views.html.savecomment.render());
    }

    /**
     * Search a book by title.
     * Demonstrating the various route configuration semantics
     *
     * @param title
     * @return
     */
    public Result searchByTitle(String title) {
        //Query db and get the book details or get from cache
        return ok(views.html.searchresults.render());
    }

    /**
     * Demonstrates ActionComposition
     */
    @Inject
    WSClient ws;

    @With(EchoAction.class)
    public CompletionStage<Result> echoService() {
        return
                ws.url("http://www.mocky.io/v2/5e0edec33400003c0f2d7d27")
                        .get()
                        .thenApply(response -> ok("Feed Response: " + response.getBody()));
    }

    /**
     * Example for asynchronous programming
     *
     * @param uid
     * @param age
     * @param relation
     * @return
     */
    public CompletionStage<Result> recomendGifts(final String uid, final String age, final String relation) {
        return CompletableFuture.supplyAsync(this::getGifts)
                .thenApply((List<GiftVO> gift) -> ok("Got " + gift));
    }

    private List<GiftVO> getGifts() {
        List<GiftVO> gifts = new ArrayList<GiftVO>();
        gifts.add(new GiftVO());
        return gifts;
    }

    /**
     * Example for various route configuration semantics
     *
     * @param bookid
     * @param pageNumber
     * @return
     */
    public Result fetchBookpage(String bookid, int pageNumber) {
        //Query db and get the book details or get from cache
        return ok(views.html.searchresults.render());
    }

    /**
     * Example for handling Json
     *
     * @param request
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result acknowledgeGreeting(Http.Request request) {

        JsonNode json = request.body().asJson();
        String greeting = json.findPath("greeting").textValue();
        if (greeting == null) {
            return badRequest("Missing parameter [greeting]");
        } else {
            return ok("Your greeting " + greeting + " is accepted");
        }
    }

    @BodyParser.Of(BodyParser.Xml.class)
    public Result acknowledgeGreetingXML(Http.Request request) {

        Document dom = request.body().asXml();
        if (dom == null) {
            return badRequest("Requires XML Input");
        } else {
            String greeting = XPath.selectText("//greeting", dom);
            if (greeting == null) {
                return badRequest("Missing parameter [greeting]");
            } else {
                return ok("Your greeting " + greeting + " is accepted");
            }
        }
    }

    /**
     * Example for handling XML
     *
     * @param request
     * @return
     * @throws Exception
     */
    @BodyParser.Of(BodyParser.Xml.class)
    public Result acknowledgeGreetingXMLJaxbVersion(Http.Request request) throws Exception {

        Document doc = request.body().asXml();
        if (doc == null) {
            return badRequest("Requires XML Input");
        } else {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            System.out.println("XML " + output);
            JAXBContext context = JAXBContext.newInstance(Message.class);
            Unmarshaller unMarshaller = context.createUnmarshaller();
            //JAXB Auto conversion- XML to Model
            Message msg = (Message) unMarshaller.unmarshal(new StringReader(output));
            if (msg == null) {
                return badRequest("Requires XML Input");
            } else {
                String greeting = msg.getGreeting();
                if (greeting == null) {
                    return badRequest("Missing parameter [greeting]");
                } else {
                    return ok("Your greeting " + greeting + " is accepted");
                }
            }
        }
    }

    public Result authors(Integer count) {
        return ok("Top selling authors");
    }

    /**
     * Example for Caching Http Response
     *
     * @return
     */
    @Cached(key = "contactus")
    public Result contact() {
        log.info("contact us method: processing");
        return ok("Apress Media, LLC\n" +
                "\n" +
                "One New York Plaza, Suite 4600\n" +
                "\n" +
                "New York, NY 10004-1562");
    }

    public CompletionStage<Result> topThreeBooks() {
        return cache.getOrElseUpdate("topthree", this::getTopBooks)
                .thenApply((List<String> books) -> ok(books.toString()));
    }

    private CompletionStage<List<String>> getTopBooks() {
        List<String> topThreeBooks = new ArrayList<String>();
        topThreeBooks.add("Book 1");
        topThreeBooks.add("Book 2");
        topThreeBooks.add("Book 3");
        return CompletableFuture.completedFuture(topThreeBooks);
    }

    public Result showComment(String userId) {
        return ok("Recent Comments by user");
    }
}
