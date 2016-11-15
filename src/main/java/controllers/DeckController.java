package controllers;

import database.document.DeckDocument;
import database.document.FavoriteDocument;
import database.repository.DeckRepository;
import database.repository.FavoriteRepository;

import java.util.List;
import java.util.stream.Collectors;

import static database.mongo.MongoUtils.toJson;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static security.LoginHandler.loggedUserId;
import static spark.Spark.*;

/**
 * Created by Andrzej on 2016-11-09.
 */
public class DeckController extends AbstractController {

    private DeckRepository deckRepository;
    private FavoriteRepository favoriteRepository = new FavoriteRepository();

    public DeckController(){
        this.deckRepository = new DeckRepository();
    }

    public void setRestApi(){

        get("/api/decks", (req, res) -> {
            List<DeckDocument> decks = deckRepository.findAll();
            List<String> favorites = favoriteRepository.findByUserId(loggedUserId(req, res))
                    .stream()
                    .map(FavoriteDocument::getDeckId)
                    .collect(Collectors.toList());

            for(DeckDocument deck : decks) {
                deck.setFavorite(favorites.contains(deck.getId()));
            }

            return toJson(decks);
        });

        post("/decks/create", (req, resp)-> {
            String owner = req.queryParams("ownerId");
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            String response = null;
            try{
                int difficulty = Integer.parseInt(req.queryParams("difficulty"));

                if(owner != null && name != null) {
                    DeckDocument deck = new DeckDocument(owner, name, description, difficulty);
                    deckRepository.save(deck);
                    response = deck.getId();
                }
            } catch (NumberFormatException exception){
                resp.status(HTTP_BAD_REQUEST);
            }

            return response;
        });

        delete("/decks/delete/:id", (req, resp) -> {
            String id = req.params("id");
            Object deck = deckRepository.delete(id);
            if(deck != null){
                resp.status(HTTP_OK);
            }
            else{
                resp.status(HTTP_BAD_REQUEST);
            }
            return "";
        });
    }
}
