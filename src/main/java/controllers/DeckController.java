package controllers;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.document.FavoriteDocument;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import database.repository.FavoriteRepository;

import java.util.List;
import java.util.stream.Collectors;

import static database.mongo.MongoUtils.toJson;
import static java.net.HttpURLConnection.*;
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

        post("/api/decks/create", (req, resp)-> {
            String owner = req.queryParams("ownerId");
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            String response = "";
            try{
                int difficulty = Integer.parseInt(req.queryParams("difficulty"));

                if(owner != null && !owner.isEmpty()
                        && name != null && !name.isEmpty()) {
                    DeckDocument deck = new DeckDocument(owner, name, description, difficulty);
                    deckRepository.save(deck);
                    resp.status(HTTP_OK);
                    response = deck.getId();
                }else {
                    resp.status(HTTP_BAD_REQUEST);
                    response = "Owner or name is missing";
                }
            } catch (NumberFormatException exception){
                resp.status(HTTP_BAD_REQUEST);
                response = "bad format of difficulty";
            }catch (Exception  e){
                resp.status(HTTP_INTERNAL_ERROR);
                response = e.getMessage();
            }

            return response;
        });

        delete("/api/decks/delete/:id", (req, resp) -> {
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

    }
}
