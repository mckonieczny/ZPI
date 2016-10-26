package database.repository;

import database.document.DeckDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class DeckRepository extends MongoRepository<DeckDocument> {

    private final static String C_DECKS = "decks";


    public DeckRepository() {
        super(C_DECKS);
    }

    public List<DeckDocument> findByOwnerId(String ownerId) {
        List<DeckDocument> cards = new ArrayList<>();
        getCollection()
                .find(eq(DeckDocument.M_OWNER_ID, ownerId))
                .map(card -> new DeckDocument((Document) card))
                .into(cards);

        return cards;
    }

}
