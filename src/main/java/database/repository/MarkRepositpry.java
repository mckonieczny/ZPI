package database.repository;

import com.mongodb.BasicDBObject;
import database.document.MarkDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Andrzej on 2016-12-14.
 */
public class MarkRepositpry extends MongoRepository<MarkDocument> {

    public final static String C_MARKS = "marks";

    public MarkRepositpry(){
        super(C_MARKS);
    }

    public List<MarkDocument> findAll() {

        List<MarkDocument> marks = new ArrayList<>();
        getCollection()
                .find()
                .map(mark -> new MarkDocument((Document) mark))
                .into(marks);

        return marks;
    }

    public boolean isExistingMark(String deckId, String ownerId){
        BasicDBObject search = createSearchObject(deckId, ownerId);
        return getCollection().find(search).first() != null;
    }

    public void updateMark(String deckId, String ownerId, int mark){
        BasicDBObject search = createSearchObject(deckId, ownerId);
        getCollection().updateOne(search, new BasicDBObject("$set", new BasicDBObject(MarkDocument.M_MARK, mark)));
    }

    private BasicDBObject createSearchObject(String deckId, String ownerId){
        BasicDBObject search = new BasicDBObject();
        search.append(MarkDocument.M_DECK_ID, deckId);
        search.append(MarkDocument.M_OWNER_ID, ownerId);
        return search;
    }

    public List<MarkDocument> findByDeckId(String deckId) {
        List<MarkDocument> marks = new ArrayList<MarkDocument>();
        getCollection().find(new BasicDBObject(MarkDocument.M_DECK_ID, deckId))
                       .map(mark -> new MarkDocument((Document) mark))
                       .into(marks);
        return marks;
    }

    public Optional<MarkDocument> findByUserId(String userId) {

        return Optional.ofNullable(new MarkDocument((Document) getCollection().find(eq(MarkDocument.M_OWNER_ID, userId)).first()));
    }
}
