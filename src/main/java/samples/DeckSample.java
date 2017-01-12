package samples;

import database.document.DeckDocument;
import database.document.FavoriteDocument;
import database.repository.DeckRepository;
import database.repository.FavoriteRepository;
import security.LoginHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static security.LoginHandler.loggedUserId;
import static security.LoginHandler.loggedUserName;
import static server.SparkUtils.notEmpty;
import static server.SparkUtils.templateEngine;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by DjKonik on 2016-11-15.
 */
public class DeckSample {

    private static DeckRepository deckRepository = new DeckRepository();
    private static FavoriteRepository favoriteRepository= new FavoriteRepository();

    public static void create(LoginHandler loginHandler) {

        loginHandler.secureUrl("/panel/decks");
        get("/panel/decks", (req, res) -> decksListView(req, res), templateEngine());

        loginHandler.secureUrl("/panel/decks/create");
        get("/panel/decks/create", (req, res) -> decksCreateView(), templateEngine());

        post("/panel/decks/create", (req, res) -> {
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            int difficulty = Integer.valueOf(req.queryParams("difficulty"));

            if (notEmpty(name) && notEmpty(description)) {
                deckRepository.save(new DeckDocument(loggedUserId(req,res), loggedUserName(req, res), name, description, difficulty));
                return decksListView(req, res);
            } else {
                return decksCreateView();
            }
        }, templateEngine());

        loginHandler.secureUrl("/panel/decks/delete/:id");
        get("/panel/decks/delete/:id", (req, res) -> {
            deckRepository.deleteById(req.params(":id"));
            return decksListView(req, res);
        }, templateEngine());

        loginHandler.secureUrl("/panel/favorite/add/:id");
        get("/panel/favorite/add/:id", (req, res) -> {
            favoriteRepository.save(loggedUserId(req, res), req.params(":id"));
            return decksListView(req, res);
        }, templateEngine());

        loginHandler.secureUrl("/panel/favorite/remove/:id");
        get("/panel/favorite/remove/:id", (req, res) -> {
            favoriteRepository.delete(loggedUserId(req, res), req.params(":id"));
            return decksListView(req, res);
        }, templateEngine());
    }

    private static ModelAndView decksListView(Request req, Response res) {

        List<DeckDocument> decks = deckRepository.findAll();
        List<String> favorites = favoriteRepository.findByUserId(loggedUserId(req, res))
            .stream()
            .map(FavoriteDocument::getDeckId)
            .collect(Collectors.toList());

        for(DeckDocument deck : decks) {
            deck.setFavorite(favorites.contains(deck.getId()));
        }

        Map<String, Object> model = new HashMap<>();
        model.put("decks", decks);
        return new ModelAndView(model, "decks/list.ftl");
    }

    private static ModelAndView decksCreateView() {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "decks/create.ftl");
    }
}
