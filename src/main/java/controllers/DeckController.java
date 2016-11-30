package controllers;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.document.FavoriteDocument;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import database.repository.FavoriteRepository;
import java.util.List;

import static org.bson.Document.parse;
import static database.mongo.MongoUtils.toJson;
import static java.net.HttpURLConnection.*;
import static java.util.stream.Collectors.toList;
import static security.LoginHandler.loggedUserId;
import static server.SparkUtils.notEmpty;
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
            setFavorites(decks, loggedUserId(req, res));
            return toJson(decks);
        });

        get("/api/favorite/decks", (req, res) -> {
            List<DeckDocument> decks = deckRepository.findAll();
            setFavorites(decks, loggedUserId(req, res));
            decks = decks.stream()
                    .filter(DeckDocument::isFavorite)
                    .collect(toList());

            return toJson(decks);
        });

        get("/api/user/decks", (req, res) -> {
            List<DeckDocument> decks =  deckRepository.findByOwnerId(loggedUserId(req, res));
            setFavorites(decks, loggedUserId(req, res));
            return toJson(decks);
        });

        post("/api/decks/create", (req, resp)-> {
            String response = "";
            try{
                DeckDocument deck = new DeckDocument(parse(req.body()));

                int dif = deck.getDifficulty();
                if(notEmpty(deck.getOwnerId()) && notEmpty(deck.getName()) && dif > 0 && dif <= 5) {
                    deckRepository.save(deck);
                    resp.status(HTTP_OK);
                    response = deck.getId();
                }else {
                    resp.status(HTTP_BAD_REQUEST);
                    response = "Owner or name is missing";
                }
            } catch (ClassCastException exception){
                resp.status(HTTP_BAD_REQUEST);
                response = "Bad format of difficulty";
            }catch (Exception  e){
                resp.status(HTTP_INTERNAL_ERROR);
                response = e.getMessage();
            }

            return response;
        });

        post("/api/decks/:id/delete", (req, resp) -> {
            String result = "";
            String id = req.params("id");
            if(notEmpty(id)){
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

    private List<DeckDocument> setFavorites(List<DeckDocument> decks, String userId) {
        List<String> favorites = favoriteRepository.findByUserId(userId)
                .stream()
                .map(FavoriteDocument::getDeckId)
                .collect(toList());

        for(DeckDocument deck : decks) {
            deck.setFavorite(favorites.contains(deck.getId()));
        }
        return decks;
    }
}
