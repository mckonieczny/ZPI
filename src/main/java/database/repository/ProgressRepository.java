package database.repository;

import com.mongodb.BasicDBObject;
import database.document.ProgressDoument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrzej on 2017-01-11.
 */
public class ProgressRepository extends MongoRepository<ProgressDoument> {

    public static final String C_PROGRESSES = "progresses";

    public ProgressRepository(){
        super(C_PROGRESSES);
    }

    public ProgressDoument findByDeckAndOwner(String ownerId, String deckId){
        BasicDBObject search = this.createSearchObject(ownerId, deckId);
        return find(search);
    }


    public ProgressDoument findById(String progressId) {
        return find(eqId(progressId));
    }

    public List<ProgressDoument> findAll(){
        List<ProgressDoument> progresses = new ArrayList<>();
        getCollection().find().map(progress -> new ProgressDoument((Document) progress)). into(progresses);
        return progresses;
    }

    private BasicDBObject createSearchObject(String ownerId, String deckId){
        BasicDBObject search = new BasicDBObject();
        search.append(ProgressDoument.M_OWNER_ID, ownerId);
        search.append(ProgressDoument.M_DECK_ID, deckId);
        return search;
    }

    private ProgressDoument find(BasicDBObject search){
        List<ProgressDoument> progresses = new ArrayList<>();
        getCollection().find(search).map(progress -> new ProgressDoument((Document) progress)). into(progresses);
        return progresses.size() > 0 ? progresses.get(0) : null;
    }
}
