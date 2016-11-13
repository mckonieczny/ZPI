import database.document.UserDocument;
import database.repository.CardRepository;
import database.repository.DeckRepository;
import database.repository.UserRepository;
import org.pac4j.sparkjava.SecurityFilter;
import security.LoginHandler;
import security.PasswordHash;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static database.mongo.MongoUtils.toJson;
import static server.SparkUtils.renderContent;
import static server.SparkUtils.templateEngine;
import static spark.Redirect.Status.TEMPORARY_REDIRECT;
import static spark.Spark.*;
import static spark.Spark.post;

/**
 * Created by DjKonik on 2016-11-13.
 */
public class Samples {

    public static void create(LoginHandler loginHandler) {

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
                userRepository.save(new UserDocument(username, PasswordHash.createHash(password)));
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

        before("/private", new SecurityFilter(loginHandler.getConfig(), "loginForm"));
        get("/private", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "private.ftl");
        }, templateEngine());

        get("/api/decks", (req, res) -> toJson(new DeckRepository().findAll()));
        get("/api/decks/:id", (req, res) -> toJson(new CardRepository().findByDeckId(req.params(":id"))));

        get("/register", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "register.ftl");
        }, templateEngine());

    }

    private static ModelAndView getReactPage() {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "index.ftl");
    }
}