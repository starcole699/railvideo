package rgups.railvideo.core;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Dmitry on 06.06.2017.
 */
public class ImageSourceTest {

    @Test
    public void uriTest() {
        String protocol = null;
        String host = "localhost";
        int port = 8080;
        String user = "aa:aapwd";
        String password = null;
        String path = null;
        String query = null;

        try {
            URI uri = new URI(protocol, user, host, port, path, query, null);
            System.out.println(uri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
