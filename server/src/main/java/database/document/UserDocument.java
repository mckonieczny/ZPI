package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class UserDocument extends MongoDocument {

    public final static String M_USERNAME = "username";
    public final static String M_PASSWORD = "password";


    public  UserDocument(Document document) {
        super(document);
    }

    public  UserDocument(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return getDocument().getString(M_USERNAME);
    }

    public void setUsername(String username) {
        getDocument().put(M_USERNAME, username);
    }

    public String getPassword() {
        return getDocument().getString(M_PASSWORD);
    }

    public void setPassword(String password) {
        getDocument().put(M_PASSWORD, password);
    }
}
