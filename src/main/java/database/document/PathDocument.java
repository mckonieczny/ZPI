package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrzej on 2016-12-01.
 */
public class PathDocument extends MongoDocument {

    public final static String M_OWNER_ID = "ownerId";

    public final static String M_NAME = "name";

    public final static String M_DESCRIPTION = "description";

    public final static String M_DECKS_IDS = "decksIds";

    public PathDocument(Document document){
        super(document);
    }

    public PathDocument(String ownerId, String name, String description, List<String> decks) {
        this.setOwerId(ownerId);
        this.setName(name);
        this.setDescription(description);
        this.setDecksIds(decks);
    }

    public void setOwerId(String owerId) {
        getDocument().put(M_OWNER_ID, owerId);
    }

    public String getOwnerID (){
        return getDocument().getString(M_OWNER_ID);
    }

    public void setName(String name) {
        getDocument().put(M_NAME, name);
    }

    public String getName(){
        return getDocument().getString(M_NAME);
    }

    public void setDescription(String description) {
        getDocument().put(M_DESCRIPTION, description);
    }

    public String getDescription(){
        return getDocument().getString(M_DESCRIPTION);
    }

    public void setDecksIds(List<String> decks){
        getDocument().put(M_DECKS_IDS, decks);
    }

    public List<String> getDecksIds(){
        return getDocument().get(M_DECKS_IDS, new ArrayList<String>().getClass());
    }
}
