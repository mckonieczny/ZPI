import database.repository.CardRepository;
import database.repository.DeckRepository;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import server.SecurityConfig;
import spark.ModelAndView;
import spark.Route;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static database.mongo.MongoUtils.toJson;
import static server.SparkUtils.getHerokuAssignedPort;
import static server.SparkUtils.renderContent;
import static spark.Spark.*;

/**
 * Created by DjKonik on 2016-10-06.
 */
public class Main {

    private final static FreeMarkerEngine templateEngine = new FreeMarkerEngine();

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        final Config config =  new SecurityConfig(templateEngine).build();
        final Route callback = new CallbackRoute(config);
        get("/callback", callback);
        post("/callback", callback);

        get("/login", (rq, rs) -> {
            Map<String, Object> model = new HashMap<>();
            FormClient formClient = config.getClients().findClient(FormClient.class);
            model.put("callbackUrl", formClient.getCallbackUrl());
            return new ModelAndView(model, "login.ftl");
        }, templateEngine);
        get("/logout", new ApplicationLogoutRoute(config, "/"));


        //przykład strony html
        get("/", (req, res) -> renderContent("/html/index.html"));

        //przykład FreeMarkera
        get("/template", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "TEMPLATE");
            return new ModelAndView(model, "template.ftl");
        }, templateEngine);

        get("/react", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index.ftl");
        }, templateEngine);

        before("/private", new SecurityFilter(config, "FormClient"));
        get("/private", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "private.ftl");
        }, templateEngine);

        get("/api/decks", (req, res) -> toJson(new DeckRepository().findAll()));
        get("/api/decks/:id", (req, res) -> toJson(new CardRepository().findByDeckId(req.params(":id"))));
    }

}
