package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-26.
 */

public class DeckDocument extends MongoDocument {

    public final static String M_OWNER_ID = "ownerId";
    public final static String M_NAME = "name";
    public final static String M_DESCRIPTION = "description";
    public final static String M_DIFFICULTY = "difficulty";
    public final static String M_SIZE = "size";
    public final static String M_FAVORITE = "favorite";


    public DeckDocument(Document document) {
        super(document);
    }

    public DeckDocument(String ownerId, String name, int difficulty) {
        this(ownerId, name, "", difficulty);
    }

    public DeckDocument(String ownerId, String name, String description, int difficulty) {
        setOwnerId(ownerId);
        setName(name);
        setDescription(description);
        setDifficulty(difficulty);
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

    public String getDescription() {
        return getDocument().getString(M_DESCRIPTION);
    }

    public void setDescription(String description) {
        getDocument().put(M_DESCRIPTION, description);
    }

    public int getDifficulty() {
        return getDocument().getInteger(M_DIFFICULTY, 0);
    }

    public void setDifficulty(int difficulty) {
        getDocument().put(M_DIFFICULTY, difficulty);
    }

    public int getSize() {
        return getDocument().getInteger(M_SIZE, 0);
    }

    public void setSize(int size) {
        getDocument().put(M_SIZE, size);
    }

    public boolean isFavorite() {
        return getDocument().getBoolean(M_FAVORITE, false);
    }

    public void setFavorite(boolean favorite) {
        getDocument().put(M_FAVORITE, favorite);
    }
}