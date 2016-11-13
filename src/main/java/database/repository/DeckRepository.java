package database.repository;

import database.document.DeckDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class DeckRepository extends MongoRepository<DeckDocument> {

    public final static String C_DECKS = "decks";


    public DeckRepository() {
        super(C_DECKS);
    }


    public Optional<DeckDocument> findById(String deckId) {

        List<DeckDocument> decks = new ArrayList<>();

        getCollection()
                .find(eqId(deckId))
                .map(deck -> new DeckDocument((Document) deck))
                .into(decks);

        if (decks.isEmpty()) {
            return Optional.ofNullable(null);
        }

        return Optional.ofNullable(decks.get(0));
    }

    public List<DeckDocument> findAll() {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find()
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

    public List<DeckDocument> findByOwnerId(String ownerId) {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find(eq(DeckDocument.M_OWNER_ID, ownerId))
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

}
