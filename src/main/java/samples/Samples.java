package samples;

import database.document.CardDocument;
import database.document.DeckDocument;
import database.document.UserDocument;
import database.mongo.MongoUtils;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import database.repository.FavoriteRepository;
import database.repository.UserRepository;
import security.LoginHandler;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static database.mongo.MongoUtils.toJson;
import static java.net.HttpURLConnection.*;
import static org.bson.Document.parse;
import static security.LoginHandler.loggedUserId;
import static security.PasswordHash.createHash;
import static server.SparkUtils.notEmpty;
import static server.SparkUtils.renderContent;
import static server.SparkUtils.templateEngine;
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
        get("/login", (req, res) -> new ModelAndView(new HashMap<>(), "login/login.ftl"), templateEngine());
        redirect.post("/login", "/callback?client_name=FormClient", TEMPORARY_REDIRECT);

        //formularz rejestracji
        post("/register", (req, res) -> {

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

        get("/", (req, res) -> getReactPage(), templateEngine());
        get("/home", (req, res) -> getReactPage(), templateEngine());
        get("/addDeck", (req, res) -> getReactPage(), templateEngine());
        get("/decks", (req, res) -> getReactPage(), templateEngine());
        get("/deck/:deckId", (req, res) -> getReactPage(), templateEngine());

        loginHandler.secureUrl("/private");
        get("/private", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "private.ftl");
        }, templateEngine());

        get("/api/decks/:id", (req, res) -> toJson(new CardRepository().findByDeckId(req.params(":id"))));

        get("/register", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "register.ftl");
        }, templateEngine());

        get("/zaorajmibaze",(req, res) -> {

            MongoUtils.dropDatabase();
            initDB();

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

    private static ModelAndView getReactPage() {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "index.ftl");
    }

    private static void initDB() throws Exception {

        UserRepository userDB = new UserRepository();
        DeckRepository deckDB = new DeckRepository();
        CardRepository cardDB = new CardRepository();
        FavoriteRepository favDB = new FavoriteRepository();

        UserDocument user1 = new UserDocument("admin", createHash("admin"));
        UserDocument user2 = new UserDocument("pwd", createHash("pwd"));

        userDB.save(user1);
        userDB.save(user2);

        DeckDocument deck1 = new DeckDocument(user1.getId(), "Owoce i warzywa", "Proste słówka, owoce i warzywa", 1);
        DeckDocument deck2 = new DeckDocument(user1.getId(), "Sporty", "Proste słówka, dyscypliny sportowe", 1);
        DeckDocument deck3 = new DeckDocument(user2.getId(), "Części do Ursusa", "Słownictwo fachowe, dla mechaników i serwisantów traktorów w Anglii", 5);

        deckDB.save(deck1);
        deckDB.save(deck2);
        deckDB.save(deck3);

        cardDB.save(new CardDocument(deck1.getId(),"alpha", "ahpla"));
        cardDB.save(new CardDocument(deck2.getId(),"beta", "ateb"));
        cardDB.save(new CardDocument(deck2.getId(),"gamma", "ammag"));
        cardDB.save(new CardDocument(deck3.getId(),"epsilon", "nolispe"));
        cardDB.save(new CardDocument(deck3.getId(),"lambda", "adbmal"));
        cardDB.save(new CardDocument(deck3.getId(),"omega", "agemo"));

        favDB.save(user1.getId(), deck1.getId());
        favDB.save(user1.getId(), deck3.getId());
        favDB.save(user2.getId(), deck1.getId());
        favDB.save(user2.getId(), deck2.getId());
    }
}
