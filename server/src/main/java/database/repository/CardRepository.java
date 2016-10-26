package database.repository;

import database.document.CardDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static database.document.CardDocument.M_DECK_ID;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class CardRepository extends MongoRepository<CardDocument> {

    private final static String C_CARDS = "cards";


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

}
