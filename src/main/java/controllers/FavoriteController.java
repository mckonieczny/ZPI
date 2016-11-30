package controllers;

import database.document.FavoriteDocument;
import database.repository.FavoriteRepository;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.bson.Document.parse;
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
            FavoriteDocument favorite = new FavoriteDocument(parse(req.body()));
            favorite.setUserId(loggedUserId(req, res));
            if (notEmpty(favorite.getDeckId())) {
                favoriteRepository.save(favorite);
                res.status(HTTP_OK);
                return favorite.toJson();
            } else {
                res.status(HTTP_BAD_REQUEST);
                return "";
            }
        });

        post("/api/favorite/remove", (req, res) -> {
            FavoriteDocument favorite = new FavoriteDocument(parse(req.body()));
            if (notEmpty(favorite.getDeckId())) {
                favoriteRepository.delete(favorite.getId(), loggedUserId(req, res));
                res.status(HTTP_OK);
                return "";
            } else {
                res.status(HTTP_BAD_REQUEST);
                return "";
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
