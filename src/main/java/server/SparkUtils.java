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

    private static boolean DEPLOYED = false;
    private static String DEPLOYED_RESOURCES_PATH = "/app/build/resources/main";

    public static String renderContent(String htmlFile) {
        if(isDeployed()) {
            return renderDeployedContent(htmlFile);
        } else {
            return renderLocalContent(htmlFile);
        }
    }

    private static String renderDeployedContent(String htmlFile) {
        try {
            URL url = new URL(DEPLOYED_RESOURCES_PATH + htmlFile);
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {}
        return null;
    }

    private static String renderLocalContent(String htmlFile) {
        try {
            URL url = SparkUtils.class.getResource(htmlFile);
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {}
        return null;
    }

    public static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            DEPLOYED = true;
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static boolean isDeployed() {
        return DEPLOYED;
    }
}
