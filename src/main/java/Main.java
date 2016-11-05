import database.repository.CardRepository;
import database.repository.DeckRepository;
import org.pac4j.sparkjava.SecurityFilter;
import security.SecurityLoginHandler;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static database.mongo.MongoUtils.toJson;
import static server.SparkUtils.*;
import static spark.Spark.*;

/**
 * Created by DjKonik on 2016-10-06.
 */
public class Main {

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        SecurityLoginHandler loginHandler = new SecurityLoginHandler();
        //loginHandler.setLoginForm();
        loginHandler.setLoginRestApi();


        //przykład strony html
        get("/", (req, res) -> renderContent("/html/index.html"));

        //przykład FreeMarkera
        get("/template", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "TEMPLATE");
            return new ModelAndView(model, "template.ftl");
        }, templateEngine());

        get("/react", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index.ftl");
        }, templateEngine());

        before("/private", new SecurityFilter(loginHandler.getConfig(), "loginForm"));
        get("/private", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "private.ftl");
        }, templateEngine());

        get("/api/decks", (req, res) -> toJson(new DeckRepository().findAll()));
        get("/api/decks/:id", (req, res) -> toJson(new CardRepository().findByDeckId(req.params(":id"))));

    }

}
