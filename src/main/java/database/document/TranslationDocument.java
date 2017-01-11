package database.document;

import database.mongo.MongoDocument;
import org.bson.Document;

/**
 * Created by Andrzej on 2017-01-11.
 */
public class TranslationDocument extends MongoDocument {

    public static final String M_LANGUAGE_FROM = "from";

    public static final String M_LANGUAGE_TO = "to";

    public static final String M_WORD = "word";

    public static final String M_TRANSLATION = "translation";

    public TranslationDocument(Document document){
        super(document);
    }

    public void setLanguageForm(String from){
        getDocument().put(M_LANGUAGE_FROM, from);
    }

    public String getLanguageForm(){
        return getDocument().getString(M_LANGUAGE_FROM);
    }

    public void setLanguageTo(String to){
        getDocument().put(M_LANGUAGE_TO, to);
    }

    public String getLanguageTo(){
        return getDocument().getString(M_LANGUAGE_TO);
    }

    public void setWord(String word){
        getDocument().put(M_WORD, word);
    }

    public String getmWord(){
        return getDocument().getString(M_WORD);
    }

    public void setTranslation(String translation){
        getDocument().put(M_TRANSLATION, translation);
    }

    public String getmTranslation(){
        return getDocument().getString(M_TRANSLATION);
    }
}
