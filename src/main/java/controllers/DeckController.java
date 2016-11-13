package controllers;

import database.document.DeckDocument;
import database.repository.DeckRepository;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static spark.Spark.*;

/**
 * Created by Andrzej on 2016-11-09.
 */
public class DeckController extends AbstractController {

    private DeckRepository repository;

    public DeckController(){
        this.repository = new DeckRepository();
    }

    public void setRestApi(){
        post("/decks/create", (req, resp)-> {
            String owner = req.queryParams("ownerId");
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            String response = null;
            try{
                int difficulty = Integer.parseInt(req.queryParams("difficulty"));

                if(owner != null && name != null) {
                    DeckDocument deck = new DeckDocument(owner, name, description, difficulty);
                    response = deck.toJson();
                    repository.save(deck);
                }
            } catch (NumberFormatException exception){
                resp.status(HTTP_BAD_REQUEST);
            }

            return response;
        });

        delete("/decks/delete/:id", (req, resp) -> {
            String id = req.params("id");
            Object deck = repository.delete(id);
            if(deck != null){
                resp.status(HTTP_OK);
            }
            else{
                resp.status(HTTP_BAD_REQUEST);
            }
            return "";
        });
    }
}
