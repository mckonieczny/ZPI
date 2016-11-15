package database.repository;

import database.document.CardDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static database.document.CardDocument.M_DECK_ID;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class CardRepository extends MongoRepository<CardDocument> {

    public final static String C_CARDS = "cards";


    public CardRepository() {
        super(C_CARDS);
    }

    public List<CardDocument> findByDeckId(String deckId) {
        List<CardDocument> cards = new ArrayList<>();
        getCollection()
                .find(eq(M_DECK_ID, deckId))
                .map(card -> new CardDocument((Document) card))
                .into(cards);

        return cards;
    }

    public Map<String, String> findById(String cardId) {
        Map<String, String> map = (Map<String, String>) getCollection().find(this.eqId(cardId)).first();
        return map;
    }

    public List<CardDocument> findAll() {

        List<CardDocument> cards = new ArrayList<>();
        getCollection()
                .find()
                .map(card -> new CardDocument((Document) card))
                .into(cards);

        return cards;
    }
}
