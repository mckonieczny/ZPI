package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-11-09.
 */
public class FavoriteDocument extends MongoDocument {

    public final static String M_USER_ID = "userId";
    public final static String M_DECK_ID = "deckId";

    public FavoriteDocument(Document document) {
        super(document);
    }

    public FavoriteDocument(String userId, String deckId) {
        setUserId(userId);
        setDeckId(deckId);
    }

    public String getDeckId() {
        return getDocument().getString(M_DECK_ID);
    }

    public void setDeckId(String deckId) {
        getDocument().put(M_DECK_ID, deckId);
    }

    public String getUserId() {
        return getDocument().getString(M_USER_ID);
    }

    public void setUserId(String userId) {
        getDocument().put(M_USER_ID, userId);
    }

}