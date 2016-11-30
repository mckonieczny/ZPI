package controllers;

import database.document.CardDocument;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import java.util.Map;

import static database.mongo.MongoUtils.toJson;
import static server.SparkUtils.notEmpty;
import static org.bson.Document.parse;
import static java.net.HttpURLConnection.*;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Andrzej on 2016-11-13.
 */
public class CardController extends AbstractController {

    private CardRepository repository;

    public CardController(){
        this.repository =  new CardRepository();
    }

    @Override
    public void registerRestApi() {

        post("/api/cards/create", (request, response) ->{
            String result = "";
            CardDocument card =  new CardDocument(parse(request.body()));
            if(notEmpty(card.getDeckId()) && notEmpty(card.getWord()) && notEmpty(card.getTranslation())){
                try{
                    DeckRepository deckRepository = new DeckRepository();
                    deckRepository.increaseSize(card.getDeckId());
                    this.repository.save(card);
                    result = card.getId();
                    response.status(HTTP_OK);
                }catch (Exception e){
                    response.status(HTTP_INTERNAL_ERROR);
                    result = e.getMessage();
                }
            }else {
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        });

        delete("/api/cards/:id/delete", (request, response) -> {
            String cardId = request.params("id");
            String result = "";
            if(notEmpty(cardId)) {
                try{
                    Map<String, String> card = repository.findById(cardId);
                    String deckId = card.get(CardDocument.M_DECK_ID);
                    DeckRepository deckRepository = new DeckRepository();
                    deckRepository.decreaseSize(deckId);
                    repository.deleteById(cardId);
                    response.status(HTTP_OK);
                }
                catch(Exception e) {
                    response.status(HTTP_NOT_FOUND);
                    result = e.getMessage();
                }
            }else {
                result = "CardId can not be null or empty";
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        });

        get("/api/cards", (request, response) -> toJson(repository.findAll()));

        get("/api/decks/:id", (req, res) -> toJson(repository.findByDeckId(req.params(":id"))));


    }
}
