package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class LanguageDocument extends MongoDocument {

    public final static String M_LANGUAGE = "language";


    public LanguageDocument(Document document) {
        super(document);
    }

    public LanguageDocument(String language) {
        setLanguage(language);
    }

    public String getLanguage() {
        return getDocument().getString(M_LANGUAGE);
    }

    public void setLanguage(String language) {
        getDocument().put(M_LANGUAGE, language);
    }

}
