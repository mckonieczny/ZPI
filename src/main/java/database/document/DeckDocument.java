package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-26.
 */

public class DeckDocument extends MongoDocument {

    public final static String M_OWNER_ID = "ownerId";
    public final static String M_NAME = "name";
    public final static String M_SIZE = "size";


    public DeckDocument(Document document) {
        super(document);
    }

    public DeckDocument(String ownerId, String name) {
        setOwnerId(ownerId);
        setName(name);
        setSize(0);
    }

    public String getOwnerId() {
        return getDocument().getString(M_OWNER_ID);
    }

    public void setOwnerId(String deckId) {
        getDocument().put(M_OWNER_ID, deckId);
    }

    public String getName() {
        return getDocument().getString(M_NAME);
    }

    public void setName(String name) {
        getDocument().put(M_NAME, name);
    }

    public int getSize() {
        return getDocument().getInteger(M_SIZE);
    }

    public void setSize(int size) {
        getDocument().put(M_SIZE, size);
    }
}