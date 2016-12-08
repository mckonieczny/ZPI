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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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



    private static void initDB() throws Exception {

        UserRepository userDB = new UserRepository();
        DeckRepository deckDB = new DeckRepository();
        CardRepository cardDB = new CardRepository();
        FavoriteRepository favDB = new FavoriteRepository();

        List<UserDocument> users = new ArrayList<UserDocument>();
        /* 0 */ users.add(new UserDocument("admin", createHash("admin")));
        /* 1 */ users.add(new UserDocument("pwd", createHash("pwd")));
        /* 2 */ users.add(new UserDocument("filip", createHash("123")));
        /* 3 */ users.add(new UserDocument("markos", createHash("qwerty")));
        /* 4 */ users.add(new UserDocument("testownik", createHash("test")));
        users.stream().forEach(userDB::save);

        List<DeckDocument> decks = new ArrayList<DeckDocument>();
        /* 0 */ decks.add(new DeckDocument(users.get(1).getId(), "Sporty", "Proste słówka, dyscypliny sportowe", 1));
        /* 1 */ decks.add(new DeckDocument(users.get(1).getId(), "Części do Ursusa", "Słownictwo fachowe, dla mechaników i serwisantów traktorów w Anglii", 5));
        /* 2 */ decks.add(new DeckDocument(users.get(1).getId(), "Jedzenie", "Posiłki, jedzeinie i napoje", 1));
        /* 3 */ decks.add(new DeckDocument(users.get(1).getId(), "Szkoła", "Słownictwo związane z przedmiotawmi szkolnymi", 1));
        /* 4 */ decks.add(new DeckDocument(users.get(1).getId(), "Kolory", "Nazwy kolorów", 1));
        /* 5 */ decks.add(new DeckDocument(users.get(0).getId(), "Owoce i warzywa - część 1", "Proste słówka, owoce i warzywa", 1));
        /* 6 */ decks.add(new DeckDocument(users.get(0).getId(), "Owoce i warzywa - część 2", "Proste słówka, owoce i warzywa", 2));
        /* 7 */ decks.add(new DeckDocument(users.get(0).getId(), "Owoce i warzywa - część 3", "Proste słówka, owoce i warzywa", 3));
        /* 8 */ decks.add(new DeckDocument(users.get(0).getId(), "Owoce i warzywa - część 4", "Proste słówka, owoce i warzywa", 4));
        /* 9 */ decks.add(new DeckDocument(users.get(0).getId(), "Owoce i warzywa - część 5", "Proste słówka, owoce i warzywa", 5));
        /* 10 */ decks.add(new DeckDocument(users.get(0).getId(), "Ubranie - część 1", "Proste słówka, ubranie", 1));
        /* 11 */ decks.add(new DeckDocument(users.get(0).getId(), "Ubranie - część 2", "Proste słówka, ubranie", 2));
        /* 12 */ decks.add(new DeckDocument(users.get(0).getId(), "Ubranie - część 3", "Proste słówka, ubranie", 3));
        /* 13 */ decks.add(new DeckDocument(users.get(0).getId(), "Ubranie - część 4", "Proste słówka, ubranie", 4));
        /* 14 */ decks.add(new DeckDocument(users.get(0).getId(), "Ubranie - część 5", "Proste słówka, ubranie", 5));
        /* 15 */ decks.add(new DeckDocument(users.get(0).getId(), "Części ciała - część 1", "Proste słówka, części ciała", 1));
        /* 16 */ decks.add(new DeckDocument(users.get(0).getId(), "Części ciała - część 2", "Proste słówka, części ciała", 2));
        /* 17 */ decks.add(new DeckDocument(users.get(0).getId(), "Części ciała - część 3", "Proste słówka, części ciała", 3));
        /* 18 */ decks.add(new DeckDocument(users.get(0).getId(), "Części ciała - część 4", "Proste słówka, części ciała", 4));
        /* 19 */ decks.add(new DeckDocument(users.get(0).getId(), "Części ciała - część 5", "Proste słówka, części ciała", 5));
        /* 20 */ decks.add(new DeckDocument(users.get(0).getId(), "Sprzęty domowe - część 1", "Proste słówka, sprzęty domowe", 1));
        /* 21 */ decks.add(new DeckDocument(users.get(0).getId(), "Sprzęty domowe - część 2", "Proste słówka, sprzęty domowe", 2));
        /* 22 */ decks.add(new DeckDocument(users.get(0).getId(), "Sprzęty domowe - część 3", "Proste słówka, sprzęty domowe", 3));
        /* 23 */ decks.add(new DeckDocument(users.get(0).getId(), "Sprzęty domowe - część 4", "Proste słówka, sprzęty domowe", 4));
        /* 24 */ decks.add(new DeckDocument(users.get(0).getId(), "Sprzęty domowe - część 5", "Proste słówka, sprzęty domowe", 5));
        decks.stream().forEach(deckDB::save);

        /* 0 */
        cardDB.save(new CardDocument(decks.get(0).getId(),"aerobik", "aerobics"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"łucznictwo", "archery"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"atletyka", "athletics"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"badminton", "badminton"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"piłka", "ball"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"koszykówka", "basketball"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"bilard", "billiard"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"boks", "boxing"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"skoki na elastycznej linie", "bungee jumping"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"kajakarstwo", "canoeing"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"krykiet", "cricket"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"jazda na rowerze", "cycling"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"gra w strzałki", "darts"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"nurkowanie", "diving"));
        cardDB.save(new CardDocument(decks.get(0).getId(),"piłka nożna", "football"));

        /* 5 */
        cardDB.save(new CardDocument(decks.get(5).getId(),"jabłko", "apple"));
        cardDB.save(new CardDocument(decks.get(5).getId(),"morela", "apricot"));
        cardDB.save(new CardDocument(decks.get(5).getId(),"jagoda", "berry"));
        cardDB.save(new CardDocument(decks.get(5).getId(),"wiśnia", "cherry"));
        cardDB.save(new CardDocument(decks.get(5).getId(),"winogrono", "grape"));

        /* 6 */
        cardDB.save(new CardDocument(decks.get(6).getId(),"jabłko", "apple"));
        cardDB.save(new CardDocument(decks.get(6).getId(),"morela", "apricot"));
        cardDB.save(new CardDocument(decks.get(6).getId(),"jagoda", "berry"));
        cardDB.save(new CardDocument(decks.get(6).getId(),"wiśnia", "cherry"));
        cardDB.save(new CardDocument(decks.get(6).getId(),"winogrono", "grape"));

        /* 7 */
        cardDB.save(new CardDocument(decks.get(7).getId(),"jabłko", "apple"));
        cardDB.save(new CardDocument(decks.get(7).getId(),"morela", "apricot"));
        cardDB.save(new CardDocument(decks.get(7).getId(),"jagoda", "berry"));
        cardDB.save(new CardDocument(decks.get(7).getId(),"wiśnia", "cherry"));
        cardDB.save(new CardDocument(decks.get(7).getId(),"winogrono", "grape"));

        /* 8 */
        cardDB.save(new CardDocument(decks.get(8).getId(),"jabłko", "apple"));
        cardDB.save(new CardDocument(decks.get(8).getId(),"morela", "apricot"));
        cardDB.save(new CardDocument(decks.get(8).getId(),"jagoda", "berry"));
        cardDB.save(new CardDocument(decks.get(8).getId(),"wiśnia", "cherry"));
        cardDB.save(new CardDocument(decks.get(8).getId(),"winogrono", "grape"));

        /* 9 */
        cardDB.save(new CardDocument(decks.get(9).getId(),"jabłko", "apple"));
        cardDB.save(new CardDocument(decks.get(9).getId(),"morela", "apricot"));
        cardDB.save(new CardDocument(decks.get(9).getId(),"jagoda", "berry"));
        cardDB.save(new CardDocument(decks.get(9).getId(),"wiśnia", "cherry"));
        cardDB.save(new CardDocument(decks.get(9).getId(),"winogrono", "grape"));

        favDB.save(users.get(0).getId(), decks.get(0).getId());
        favDB.save(users.get(0).getId(), decks.get(1).getId());
        favDB.save(users.get(0).getId(), decks.get(5).getId());
        favDB.save(users.get(0).getId(), decks.get(6).getId());
        favDB.save(users.get(1).getId(), decks.get(0).getId());
        favDB.save(users.get(1).getId(), decks.get(3).getId());
        favDB.save(users.get(1).getId(), decks.get(6).getId());
        favDB.save(users.get(1).getId(), decks.get(7).getId());

    }
}
