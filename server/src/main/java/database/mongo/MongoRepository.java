package database.mongo;

import com.mongodb.client.MongoCollection;

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

}
