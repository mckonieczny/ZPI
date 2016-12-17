package samples;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.document.UserDocument;
import database.mongo.MongoInit;
import database.mongo.MongoUtils;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import database.repository.UserRepository;
import security.LoginHandler;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.bson.Document.parse;
import static security.LoginHandler.loggedUserId;
import static security.PasswordHash.createHash;
import static server.SparkUtils.*;
import static spark.Redirect.Status.TEMPORARY_REDIRECT;
import static spark.Spark.*;

/**
 * Created by DjKonik on 2016-11-13.
 */
public class Samples {

    public static void create(LoginHandler loginHandler) {

        DeckSample.create(loginHandler);
        CardSample.create(loginHandler);

        //formularz logowania
        get("/panel/login", (req, res) -> new ModelAndView(new HashMap<>(), "login/login.ftl"), templateEngine());
        redirect.post("/panel/login", "/callback?client_name=FormClient", TEMPORARY_REDIRECT);

        //formularz rejestracji
        post("/panel/register", (req, res) -> {

            Map<String, Object> model = new HashMap<>();

            UserRepository userRepository = new UserRepository();

            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(userRepository.findByName(username).isEmpty()) {
                userRepository.save(new UserDocument(username, createHash(password)));
                model.put("msg", "Dodano użytkownika " + username);
            } else {
                model.put("msg", "Użytkownik " + username + " już istnieje");
            }

            return new ModelAndView(model, "register.ftl");
        }, templateEngine());

        //przykład strony html
        get("/panel", (req, res) -> renderContent("/html/index.html"));

        //przykład FreeMarkera
        get("/template", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "TEMPLATE");
            return new ModelAndView(model, "template.ftl");
        }, templateEngine());

        loginHandler.secureUrl("/private");
        get("/private", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "private.ftl");
        }, templateEngine());

        get("/register", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "register.ftl");
        }, templateEngine());

        get("/zaorajmibaze",(req, res) -> {

            MongoUtils.dropDatabase();
            MongoInit.run();

            return renderContent("/html/index.html");
        });

        post("/api/decks/create2", (req, res)-> {

            DeckDocument deck = new DeckDocument(parse(req.body()));
            deck.setSize(0);
            deck.setOwnerId(loggedUserId(req, res));

            if (notEmpty(deck.getName()) && notEmpty(deck.getDescription())) {
                new DeckRepository().save(deck);
                res.status(HTTP_OK);
                return deck.toJson();
            } else {
                res.status(HTTP_BAD_REQUEST);
                return "";
            }
        });

        post("/api/cards/create2", (req, res) ->{
            CardDocument card = new CardDocument(parse(req.body()));
            if (notEmpty(card.getDeckId()) && notEmpty(card.getWord()) && notEmpty(card.getTranslation())) {
                new CardRepository().save(card);
                new DeckRepository().increaseSize(card.getDeckId());
                res.status(HTTP_OK);
                return card.toJson();
            } else {
                res.status(HTTP_BAD_REQUEST);
                return "";
            }
        });

    }




}
