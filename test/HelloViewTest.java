import org.junit.Test;
import play.twirl.api.Content;

import static junit.framework.TestCase.assertEquals;


public class HelloViewTest {

    @Test
    public void renderTemplate() {
        Content html = views.html.hello.render("Welcome to Play!");
        assertEquals("text/html", html.contentType());
        assert(html.body().toString().contains("Hello World"));
    }
}
