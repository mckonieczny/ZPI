package controllers;

import database.document.ProgressDoument;
import database.repository.ProgressRepository;

import java.util.List;

import static org.bson.Document.parse;
import static spark.Spark.get;
import static spark.Spark.post;
import static database.mongo.MongoUtils.toJson;
import static security.LoginHandler.loggedUserId;
import static server.SparkUtils.notEmpty;
import static java.net.HttpURLConnection.*;

/**
 * Created by Andrzej on 2017-01-11.
 */
public class ProgressController extends AbstractController {

    private ProgressRepository repository;

    public ProgressController(){
        repository = new ProgressRepository();
    }

    @Override
    public void registerRestApi() {
        post("/api/progress/create", ((request, response) -> {
            String result = "";
            ProgressDoument progress = new ProgressDoument(parse(request.body()));
            if(notEmpty(progress.getDeckId())){
                progress.setOwnerId(loggedUserId(request, response));
                repository.save(progress);
                response.status(HTTP_OK);
                result = progress.toJson();
            }else {
                result = "DeckId can not be empty";
                response.status(HTTP_BAD_REQUEST);

            }
            return result;
        }));

        post("/api/progress/:id/update", ((request, response) -> {
            String result = "";
            String progressId = request.params("id");
            if(notEmpty(progressId)){
                ProgressDoument oldProgress = repository.findById(progressId);
                ProgressDoument newProgress = new ProgressDoument(parse(request.body()));
                String deckId = newProgress.getDeckId();
                List<String> learnedWords = newProgress.getLearnedWords();
                if(notEmpty(deckId)){
                    oldProgress.setDeckId(deckId);
                }

                if(learnedWords != null){
                    oldProgress.setLearnedWords(learnedWords);
                }

                repository.deleteById(oldProgress.getId());
                repository.save(oldProgress);
                result = oldProgress.toJson();
                response.status(HTTP_OK);
            }
            else{
                result = "Progress id can not be null";
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        }));

        post("/api/progress/:id/delete", ((request, response) -> {
            String result = "";
            String progressId = request.params("id");
            if(notEmpty(progressId)){
                repository.deleteById(progressId);
                response.status(HTTP_OK);
            }else {
                result = "Progress id can not be empty";
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        }));

        get("/api/progress", ((request, response) -> toJson(repository.findAll())));
    }
}
