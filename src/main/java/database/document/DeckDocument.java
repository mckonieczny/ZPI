package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-26.
 */

public class DeckDocument extends MongoDocument {

    public final static String M_OWNER_ID = "ownerId";
    public final static String M_OWNER = "owner";
    public final static String M_NAME = "name";
    public final static String M_DESCRIPTION = "description";
    public final static String M_DIFFICULTY = "difficulty";
    public final static String M_SIZE = "size";
    public final static String M_FAVORITE = "favorite";
    public final static String M_LANGUAGE = "language";
    public final static String M_IMAGE = "image";
    public final static String M_MARK = "mark";
    public final static String M_VOTES = "votes";


    public DeckDocument(Document document) {
        super(document);
    }

    public DeckDocument(String ownerId, String owner, String name, String description, int difficulty) {
        this(ownerId, owner, name, description, difficulty, "");
    }

    public DeckDocument(String ownerId, String owner, String name, String description, int difficulty, String language) {
        setOwnerId(ownerId);
        setOwner(owner);
        setName(name);
        setDescription(description);
        setDifficulty(difficulty);
        setLanguage(language);
    }

    public String getOwnerId() {
        return getDocument().getString(M_OWNER_ID);
    }

    public void setOwnerId(String ownerId) {
        getDocument().put(M_OWNER_ID, ownerId);
    }

    public void setOwner(String owner) {
        getDocument().put(M_OWNER, owner);
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

    public String getLanguage() {
        return getDocument().getString(M_LANGUAGE);
    }

    public void setLanguage(String language) {
        getDocument().put(M_LANGUAGE, language);
    }

    public void setLanguage(Document language) {
        getDocument().put(M_LANGUAGE, language);
    }

    public String getImage() {
        return getDocument().getString(M_IMAGE);
    }

    public void setImage(String image) {
        getDocument().put(M_IMAGE, image);
    }

    public void setMark(double mark) {
        getDocument().put(M_MARK, mark);
    }

    public void setVotes(int votes) {
        getDocument().put(M_VOTES, votes);
    }
}