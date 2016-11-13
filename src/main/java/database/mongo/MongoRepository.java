package database.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;

/**
 * Created by DjKonik on 2016-10-08.
 */
public abstract class MongoRepository<E extends MongoDocument> {

    private MongoCollection collection;


    protected MongoRepository(String collection) {
        this.collection = MongoConnection.open().getCollection(collection);
    }

    protected MongoCollection getCollection() {
        return collection;
    }

    public void save(E document) {
        collection.insertOne(document.getDocument());
    }

    public void delete(E document) {
        collection.deleteOne(document.getDocument());
    }

    protected static BasicDBObject eqId(String id) {

        BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));

        return query;
    }

    public void deleteById(String documentId) {

        getCollection()
                .findOneAndDelete(eqId(documentId));
    }
}
