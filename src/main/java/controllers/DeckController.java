package controllers;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.document.FavoriteDocument;
import database.document.LanguageDocument;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import database.repository.FavoriteRepository;
import database.repository.LanguageRepository;

import java.util.List;
import java.util.Map;

import static database.mongo.MongoUtils.toJson;
import static java.lang.Integer.parseInt;
import static java.net.HttpURLConnection.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.bson.Document.parse;
import static security.LoginHandler.loggedUserId;
import static server.SparkUtils.notEmpty;
import static spark.Spark.get;
import static spark.Spark.post;


/**
 * Created by Andrzej on 2016-11-09.
 */
public class DeckController extends AbstractController {

    private DeckRepository deckRepository;
    private FavoriteRepository favoriteRepository = new FavoriteRepository();
    private LanguageRepository languageRepository = new LanguageRepository();

    public DeckController(){
        this.deckRepository = new DeckRepository();
    }

    public void registerRestApi(){

        get("/api/decks", (req, res) -> {

            int page = -1;
            try {
                page = parseInt(req.queryParams("page"));
            } catch (NumberFormatException e) {}

            List<DeckDocument> decks;
            if (page >= 0) {
                decks = deckRepository.findAll(page);
            } else {
                decks = deckRepository.findAll();
            }

            setFavorites(decks, loggedUserId(req, res));
            setLanguage(decks);
            return toJson(decks);
        });

        get("/api/favorite/decks", (req, res) -> {
            List<DeckDocument> decks = deckRepository.findAll();
            setFavorites(decks, loggedUserId(req, res));
            setLanguage(decks);
            decks = decks.stream()
                    .filter(DeckDocument::isFavorite)
                    .collect(toList());

            return toJson(decks);
        });

        get("/api/user/decks", (req, res) -> {
            List<DeckDocument> decks =  deckRepository.findByOwnerId(loggedUserId(req, res));
            setFavorites(decks, loggedUserId(req, res));
            setLanguage(decks);
            return toJson(decks);
        });

        post("/api/decks/create", (req, resp)-> {
            String response = "";
            try{
                DeckDocument deck = new DeckDocument(parse(req.body()));

                int dif = deck.getDifficulty();
                if(notEmpty(deck.getName()) && dif > 0 && dif <= 5) {
                    deck.setOwnerId(loggedUserId(req, resp));
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

    private List<DeckDocument> setLanguage(List<DeckDocument> decks) {

        Map<String, String> languages = languageRepository.findAll()
                .stream()
                .collect(toMap(LanguageDocument::getId, LanguageDocument::getLanguage));

        for (DeckDocument deck : decks) {
            if (languages.containsKey(deck.getLanguage())) {
                String language = languages.get(deck.getLanguage());
                deck.setLanguage(language);
            }
        }
        return decks;
    }
}
