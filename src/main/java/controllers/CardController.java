package controllers;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.repository.CardRepository;
import database.repository.DeckRepository;

import java.util.Map;

import static java.net.HttpURLConnection.*;
import static spark.Spark.*;
import static database.mongo.MongoUtils.toJson;

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
            String deckId = request.queryParams("deckId");
            String word = request.queryParams("word");
            String translation = request.queryParams("translation");
            String result = "";
            if(deckId != null && !deckId.isEmpty()
                    && word != null && !word.isEmpty()
                    && translation != null && !translation.isEmpty()){
                try{
                    CardDocument card = new CardDocument(deckId, word, translation);
                    DeckRepository deckRepository = new DeckRepository();
                    DeckDocument deck = deckRepository.findByDeckId(deckId);
                    if(deck != null) {
                        this.repository.save(card);
                        deck.setSize(deck.getSize() + 1);
                        deckRepository.updateSize(deck.getId(), deck.getSize());
                        result = card.getId();
                        response.status(HTTP_OK);
                    }else {
                        response.status(HTTP_NOT_FOUND);
                        result = "Deck with given id does not exists";
                    }
                }catch (Exception e){
                    response.status(HTTP_INTERNAL_ERROR);
                    result = e.getMessage();
                }
            }else {
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        });

        delete("/api/cards/delete/:id", (request, response) -> {
            String cardId = request.params("id");
            String result = "";
            if(cardId != null && !cardId.isEmpty()) {
                try{
                    Map<String, String> card = repository.findById(cardId);
                    String deckId = card.get(CardDocument.M_DECK_ID);
                    DeckRepository deckRepository = new DeckRepository();
                    DeckDocument deck = deckRepository.findByDeckId(deckId);
                    deck.setSize(deck.getSize() - 1);
                    deckRepository.updateSize(deck.getId(), deck.getSize());
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


    }
}
