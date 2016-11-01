package database.repository;

import database.document.UserDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static database.document.UserDocument.M_USERNAME;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class UserRepository extends MongoRepository<UserDocument> {

    public final static String C_USER = "users";


    public UserRepository() {
        super(C_USER);
    }

    public UserDocument findByName(String name) {
        return new UserDocument((Document) getCollection().find(eq(M_USERNAME, name)).first());
    }

}
