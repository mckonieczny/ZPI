package samples;

import database.document.CardDocument;
import database.repository.CardRepository;
import security.LoginHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static server.SparkUtils.notEmpty;
import static server.SparkUtils.templateEngine;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by DjKonik on 2016-11-15.
 */
public class CardSample {

    private static CardRepository cardRepository = new CardRepository();

    public static void create(LoginHandler loginHandler) {

        loginHandler.secureUrl("/panel/decks/:deckId/cards");
        get("/panel/decks/:deckId/cards", (req, res) -> cardsListView(req, res), templateEngine());

        loginHandler.secureUrl("/panel/decks/:deckId/cards");
        post("/panel/decks/:deckId/cards/create", (req, res) -> {

            String deckId = req.queryParams("deckId");
            String word = req.queryParams("word");
            String translation = req.queryParams("translation");

            if (notEmpty(deckId) && notEmpty(word) && notEmpty(translation)) {
                cardRepository.save(new CardDocument(deckId, word, translation));
            }
            return cardsListView(req, res);
        }, templateEngine());

        loginHandler.secureUrl("/panel/decks/:deckId/cards/delete/:id");
        get("/panel/decks/:deckId/cards/delete/:id", (req, res) -> {
            cardRepository.deleteById(req.params(":id"));
            return cardsListView(req, res);
        }, templateEngine());
    }

        private static ModelAndView cardsListView(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        model.put("deckId", req.params(":deckId"));
        model.put("cards", cardRepository.findByDeckId(req.params(":deckId")));
        return new ModelAndView(model, "cards/list.ftl");
    }
}
