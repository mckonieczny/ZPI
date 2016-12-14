package database.document;

import database.mongo.MongoDocument;

/**
 * Created by Andrzej on 2016-12-14.
 */
public class MarkDocument extends MongoDocument {

    public final static String M_DECK_ID = "deckId";

    public final static String M_OWNER_ID = "ownerId";

    public final static String M_MARK = "mark";

    public MarkDocument(String deckId, String ownerId, int mark){
        setDeckId(deckId);
        setOwnerId(ownerId);
        setMark(mark);
    }

    public void setDeckId(String deckId) {
        getDocument().put(M_DECK_ID, deckId);
    }

    public String getDeckID(){
        return getDocument().getString(M_DECK_ID);
    }

    public void setOwnerId(String ownerId) {
        getDocument().put(M_OWNER_ID, ownerId);
    }

    public String getOwerId(){
        return getDocument().getString(M_OWNER_ID);
    }

    public void setMark(int mark) {
        getDocument().put(M_MARK, mark);
    }

    public int getMark(){
        return getDocument().getInteger(M_MARK, 0);
    }
}
