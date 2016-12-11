package controllers;

import database.document.LanguageDocument;
import database.repository.LanguageRepository;

import java.util.List;

import static database.mongo.MongoUtils.toJson;
import static spark.Spark.get;

/**
 * Created by Andrzej on 2016-11-09.
 */
public class LanguageController extends AbstractController {

    private LanguageRepository languageRepository = new LanguageRepository();

    public void registerRestApi(){
        get("/api/languages", (req, res) -> {
            List<LanguageDocument> languages = languageRepository.findAll();
            return toJson(languages);
        });
    }
}
