package database.repository;

import database.document.LanguageDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static database.document.LanguageDocument.M_LANGUAGE;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class LanguageRepository extends MongoRepository<LanguageDocument> {

    public final static String C_LANGUAGES = "languages";


    public LanguageRepository() {
        super(C_LANGUAGES);
    }

    public boolean exists(String language) {

        return !findByLanguage(language).isEmpty();
    }

    public List<LanguageDocument> findAll() {

        List<LanguageDocument> languages = new ArrayList<>();
        getCollection()
                .find()
                .map(document -> new LanguageDocument((Document) document))
                .into(languages);

        return languages;
    }

    private LanguageDocument findByLanguage(String language) {

        return new LanguageDocument((Document) getCollection().find(eq(M_LANGUAGE, language)).first());
    }


}
