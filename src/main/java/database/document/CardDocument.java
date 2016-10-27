package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class CardDocument extends MongoDocument {

    public final static String M_DECK_ID = "deckId";
    public final static String M_WORD = "word";
    public final static String M_TRANSLATION = "translation";


    public CardDocument(Document document) {
        super(document);
    }

    public CardDocument(String deckId, String word, String translation) {
        setDeckId(deckId);
        setWord(word);
        setTranslation(translation);
    }

    public String getDeckId() {
        return getDocument().getString(M_DECK_ID);
    }

    public void setDeckId(String deckId) {
        getDocument().put(M_DECK_ID, deckId);
    }

    public String getWord() {
        return getDocument().getString(M_WORD);
    }

    public void setWord(String word) {
        getDocument().put(M_WORD, word);
    }

    public String getTranslation() {
        return getDocument().getString(M_TRANSLATION);
    }

    public void setTranslation(String password) {
        getDocument().put(M_TRANSLATION, password);
    }
}
