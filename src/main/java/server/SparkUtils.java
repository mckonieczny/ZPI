package server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class SparkUtils {

    public static String renderContent(String htmlFile) {
        try {
            URL url = SparkUtils.class.getResource(htmlFile);
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {}
        return null;
    }
}
