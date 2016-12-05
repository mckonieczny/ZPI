package controllers;

import database.document.PathDocument;
import database.repository.PathRepository;
import spark.Request;
import spark.Response;

import java.util.List;

import static org.bson.Document.parse;
import static spark.Spark.get;
import static spark.Spark.post;
import static database.mongo.MongoUtils.toJson;
import static security.LoginHandler.loggedUserId;
import static server.SparkUtils.notEmpty;
import static java.net.HttpURLConnection.*;



/**
 * Created by Andrzej on 2016-12-01.
 */
public class PathController extends AbstractController {

    private PathRepository repository;

    public PathController(){
        repository = new PathRepository();
    }

    @Override
    public void registerRestApi() {
        post("/api/paths/create", (request, response) -> {
            PathDocument path = new PathDocument( parse( request.body()));
            String result = "";
            if(notEmpty(path.getName())){
                path.setOwerId(loggedUserId(request, response));
                repository.save(path);
                response.status(HTTP_OK);
                result = path.toJson();
            }else {
                result = "Path name can not be empty";
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        });

        post("/api/paths/:id/update", (Request request, Response response) -> {
           String pathId = request.params("id");
           String result = "";
           if(notEmpty(pathId)){
               PathDocument oldPath = repository.findById(pathId);
               repository.delete(oldPath);
               PathDocument path = new PathDocument(parse(request.body()));
               String name = path.getName();
               String description = path.getDescription();
               List<String> decksIds = path.getDecksIds();

               if(notEmpty(name)){
                   oldPath.setName(name);
               }
               if(notEmpty(description)){
                   oldPath.setDescription(description);
               }
               if(decksIds != null && decksIds.size()>0){
                   oldPath.setDecksIds(decksIds);
               }

               repository.save(oldPath);
           } else {
               result = "Path ID or name can not be empty";
               response.status(HTTP_BAD_REQUEST);
           }
           return result;
        });

        post("/api/paths/:id/delete", (request, response) ->{
            String pathId = request.params("id");
            String result;
            if(notEmpty(pathId)){
                repository.deleteById(pathId);
                result = "Path " + pathId + " was deleted";
                response.status(HTTP_OK);
            }
            else {
                result = "Path ID can not be empty";
                response.status(HTTP_BAD_REQUEST);
            }
            return result;
        });

        get("/api/user/paths", (request, response) ->
                toJson(repository.findByOwnerId(loggedUserId(request, response))));
    }
}
