package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.before;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class SparkUtils {

    private static boolean DEPLOYED = false;
    public static String PROD_URL = "https://zpi.herokuapp.com";
    public static String LOCAL_URL = "http://localhost:4567";
    private static String DEPLOYED_RESOURCES_PATH = "/app/build/resources/main";

    private final static TemplateEngine templateEngine = new FreeMarkerEngine();

    public static String renderContent(String htmlFile) {
        if(isDeployed()) {
            return renderDeployedContent(htmlFile);
        } else {
            return renderLocalContent(htmlFile);
        }
    }

    public static TemplateEngine templateEngine() {

        return templateEngine;
    }

    public static boolean notEmpty(String str) {

        return str != null && !str.equals("");
    }

    public static boolean empty(String str) {

        return str == null || str.equals("");
    }


    private static String renderDeployedContent(String htmlFile) {
        try {
            File file = new File(DEPLOYED_RESOURCES_PATH + htmlFile);
            Path path = file.toPath();
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException e) {}
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

    public static void enableCORS() {
        before("*", (req, res) -> {
            res.header("Access-Control-Allow-Origin", req.headers("Origin"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        });
    }

    public static String getAppUrl() {
        if (isDeployed()) {
            return PROD_URL;
        } else {
            return LOCAL_URL;
        }
    }

    public static List<String> splitJsonArray(String jsonArray) throws IOException, InstantiationException, IllegalAccessException {
        List<String> splittedJsonElements = new ArrayList<String>();
        ObjectMapper jsonMapper = new ObjectMapper();
        JsonNode jsonNode = jsonMapper.readTree(jsonArray);

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode individualElement = arrayNode.get(i);
                splittedJsonElements.add(individualElement.toString());
            }
        }
        return splittedJsonElements;
    }
}
