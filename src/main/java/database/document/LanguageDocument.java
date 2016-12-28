package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class LanguageDocument extends MongoDocument {

    public final static String M_LANGUAGE = "language";
    public final static String M_IMAGE = "image";


    public LanguageDocument(Document document) {
        super(document);
    }

    public LanguageDocument(String language, String image) {
        setLanguage(language);
        setImage(image);
    }

    public String getLanguage() {
        return getDocument().getString(M_LANGUAGE);
    }

    public void setLanguage(String language) {
        getDocument().put(M_LANGUAGE, language);
    }

    public String getImage() {
        return getDocument().getString(M_IMAGE);
    }

    public void setImage(String image) {
        getDocument().put(M_IMAGE, image);
    }

}
