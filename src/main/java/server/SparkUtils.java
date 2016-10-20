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
            System.out.println(url.toString());
            Path path = Paths.get(url.toURI());
            System.out.println(path.toString());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {}
        return null;
    }

    public static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
