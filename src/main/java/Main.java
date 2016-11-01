import database.repository.CardRepository;
import database.repository.DeckRepository;
import spark.ModelAndView;
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

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        get("/hello", (req, res) -> "Hello Heroku World");

        //przykład strony html
        get("/index", (req, res) -> renderContent("/html/index.html"));

        //przykład FreeMarkera
        get("/template", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "TEMPLATE");
            return new ModelAndView(model, "template.ftl");
        }, new FreeMarkerEngine());

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index.ftl");
        }, new FreeMarkerEngine());

        get("/api/decks", (req, res) -> toJson(new DeckRepository().findAll()));
        get("/api/decks/:id", (req, res) -> toJson(new CardRepository().findByDeckId(req.params(":id"))));

    }




}
