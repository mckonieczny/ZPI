package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrzej on 2017-01-11.
 */
public class ProgressDoument extends MongoDocument {

    public final static String M_OWNER_ID = "ownerId";

    public final static String M_DECK_ID = "deckId";

    public final static String M_LEARNED_WORDS = "learnedWords";

    public ProgressDoument(Document document){
        super(document);
    }

    public ProgressDoument(String ownerId, String deckId, List<String> learnedWords) {
        setOwnerId(ownerId);
        setDeckId(deckId);
        setLearnedWords(learnedWords);
    }

    public void setOwnerId(String ownerId) {
        getDocument().put(M_OWNER_ID, ownerId);
    }

    public String  getOwnerId(){
        return getDocument().getString(M_OWNER_ID);
    }

    public void setDeckId(String deckId) {
        getDocument().put(M_DECK_ID, deckId);
    }

    public String getDeckId(){
        return getDocument().getString(M_DECK_ID);
    }

    public void setLearnedWords(List<String> learnedWords) {
        getDocument().put(M_LEARNED_WORDS, learnedWords);
    }

    public List<String> getLearnedWords(){
        return getDocument().get(M_LEARNED_WORDS, new ArrayList<String>().getClass());
    }
}
