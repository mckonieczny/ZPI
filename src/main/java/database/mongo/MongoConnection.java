package database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static database.mongo.MongoConfig.*;
import static java.util.Optional.ofNullable;

/**
 * Created by DjKonik on 2016-10-06.
 */
public class MongoConnection {

    private MongoClient mongo;
    private MongoDatabase db;
    private static MongoConnection instance;


    private MongoConnection() {
        mongo = getMongoClient();
        db = mongo.getDatabase(MONGO_DATABASE);
    }

    public static MongoConnection open() {
        return ofNullable(instance)
                .orElse(instance = new MongoConnection());
    }

    public static void close() {
        instance.mongo.close();
        instance = null;
    }

    public static MongoClient getMongoClient() {
        MongoClientURI uri = new MongoClientURI("mongodb://"+MONGO_USERNAME+":"+MONGO_PASSWORD+"@"+MONGO_URL+":"+MONGO_PORT+"/"+MONGO_DATABASE);
        return new MongoClient(uri);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return db.getCollection(collectionName);
    }
    public MongoDatabase getDatabase() {return db;}
}
