package controllers;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.repository.CardRepository;
import database.document.FavoriteDocument;
import database.repository.DeckRepository;

import java.util.List;

import database.repository.FavoriteRepository;

import java.util.List;
import java.util.stream.Collectors;

import static database.mongo.MongoUtils.toJson;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static security.LoginHandler.loggedUserId;
import static spark.Spark.*;
import static database.mongo.MongoUtils.toJson;


/**
 * Created by Andrzej on 2016-11-09.
 */
public class DeckController extends AbstractController {

    private DeckRepository deckRepository;
    private FavoriteRepository favoriteRepository = new FavoriteRepository();

    public DeckController(){
        this.deckRepository = new DeckRepository();
    }

    public void registerRestApi(){

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
            String response = "";
            try{
                int difficulty = Integer.parseInt(req.queryParams("difficulty"));

                if(owner != null && owner.isEmpty()
                        && name != null && name.isEmpty()) {
                    DeckDocument deck = new DeckDocument(owner, name, description, difficulty);
                    deckRepository.save(deck);
                    response = deck.getId();
                }else {
                    resp.status(HTTP_BAD_REQUEST);
                }
            } catch (NumberFormatException exception){
                resp.status(HTTP_BAD_REQUEST);
            }catch (Exception  e){
                resp.status(HTTP_INTERNAL_ERROR);
            }

            return response;
        });

        delete("/decks/delete/:id", (req, resp) -> {
            String result = "";
            String id = req.params("id");
            if(id!=null && !id.isEmpty()){
                try{
                    Object deck = deckRepository.delete(id);
                    CardRepository cardRepository = new CardRepository();
                    List<CardDocument> cards = cardRepository.findByDeckId(id);
                    for (CardDocument card: cards) {
                        cardRepository.delete(card);
                    }

                    if(deck != null){
                        resp.status(HTTP_OK);
                    }
                    else{
                        resp.status(HTTP_BAD_REQUEST);
                    }
                }catch (Exception e){
                    resp.status(HTTP_INTERNAL_ERROR);
                    result = e.getMessage();
                }
            }else {
                resp.status(HTTP_BAD_REQUEST);
                result = "Deck Id can not be null";
            }

            return result;
        });

        get("/decks", (request, response) -> toJson(repository.findAll()));
    }
}
