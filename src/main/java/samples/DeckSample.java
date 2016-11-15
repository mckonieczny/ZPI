package samples;

import database.document.DeckDocument;
import database.repository.DeckRepository;
import security.LoginHandler;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static server.SparkUtils.notEmpty;
import static server.SparkUtils.templateEngine;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by DjKonik on 2016-11-15.
 */
public class DeckSample {

    private static DeckRepository deckRepository = new DeckRepository();

    public static void create(LoginHandler loginHandler) {

        loginHandler.secureUrl("/panel/decks");
        get("/panel/decks", (req, res) -> decksListView(), templateEngine());

        loginHandler.secureUrl("/panel/decks/create");
        get("/panel/decks/create", (req, res) -> decksCreateView(), templateEngine());

        post("/panel/decks/create", (req, res) -> {
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            int difficulty = Integer.valueOf(req.queryParams("difficulty"));

            if (notEmpty(name) && notEmpty(description)) {
                deckRepository.save(new DeckDocument("1", name, description, difficulty));
                return decksListView();
            } else {
                return decksCreateView();
            }
        }, templateEngine());

        loginHandler.secureUrl("/panel/decks/delete/:id");
        get("/panel/decks/delete/:id", (req, res) -> {
            deckRepository.deleteById(req.params(":id"));
            return decksListView();
        }, templateEngine());
    }

    private static ModelAndView decksListView() {
        Map<String, Object> model = new HashMap<>();
        model.put("decks", deckRepository.findAll());
        return new ModelAndView(model, "decks/list.ftl");
    }

    private static ModelAndView decksCreateView() {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "decks/create.ftl");
    }
}
