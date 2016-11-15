package database.repository;

import database.document.DeckDocument;
import database.mongo.MongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class DeckRepository extends MongoRepository<DeckDocument> {

    public final static String C_DECKS = "decks";


    public DeckRepository() {
        super(C_DECKS);
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

    public DeckDocument findByDeckId(String id){
        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find(eqId(id))
                .map(deck -> new DeckDocument((Document)deck))
                .into(decks);
        return decks.size() > 0 ? decks.get(0) : null;
    }
    
    public Object delete(String id){
        Object deleted = getCollection().findOneAndDelete(eq(DeckDocument.M_ID, new ObjectId(id)));
        return deleted;
    }

    public void update(String whereField, String whereValue, String updateField, String updateValue){
        getCollection().updateOne(new Document(whereField, whereValue), new Document(updateField, updateValue) );
    }
}
