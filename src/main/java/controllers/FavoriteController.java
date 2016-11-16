package controllers;

import database.repository.FavoriteRepository;

import static security.LoginHandler.loggedUserId;
import static server.SparkUtils.notEmpty;
import static spark.Spark.post;

/**
 * Created by Andrzej on 2016-11-09.
 */
public class FavoriteController extends AbstractController {

    private FavoriteRepository favoriteRepository = new FavoriteRepository();

    public void registerRestApi(){

        post("/api/favorite/add", (req, res) -> {
            String deckId = req.queryParams("deckId");
            if (notEmpty(deckId)) {
                favoriteRepository.save(loggedUserId(req, res), deckId);
                return statusOk();
            } else {
                return statusError();
            }
        });

        post("/api/favorite/remove", (req, res) -> {
            String deckId = req.queryParams("deckId");
            if (notEmpty(deckId)) {
                favoriteRepository.delete(loggedUserId(req, res), deckId);
                return statusOk();
            } else {
                return statusError();
            }
        });
    }

    private String statusOk() {
        return "{\"status\":200}";
    }
    private String statusError() {
        return "{\"status\":404}";
    }
}
