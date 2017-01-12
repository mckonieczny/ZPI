package database.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.IndexOptions;
import database.document.*;
import database.repository.*;

import java.util.ArrayList;
import java.util.List;

import static database.document.DeckDocument.M_DESCRIPTION;
import static database.document.DeckDocument.M_NAME;
import static database.document.UserDocument.M_USERNAME;
import static security.PasswordHash.createHash;

/**
 * Created by DjKonik on 2016-12-17.
 */
public class MongoInit {

    private static UserRepository userDB = new UserRepository();
    private static DeckRepository deckDB = new DeckRepository();
    private static CardRepository cardDB = new CardRepository();
    private static FavoriteRepository favDB = new FavoriteRepository();
    private static LanguageRepository langDB = new LanguageRepository();
    private static MarkRepositpry markDB = new MarkRepositpry();

    public static void run() throws Exception {

        initIndex();
        initData();
    }

    public static void initIndex() {

        userDB.getCollection().createIndex(new BasicDBObject().append(M_USERNAME, "text"));
        deckDB.getCollection().createIndex(new BasicDBObject().append(M_NAME, "text").append(M_DESCRIPTION, "text"), new IndexOptions().languageOverride("none"));
    }

    private static void initData() throws Exception {

        List<UserDocument> users = new ArrayList<UserDocument>();
        /* 0 */ users.add(new UserDocument("admin", createHash("admin")));
        /* 1 */ users.add(new UserDocument("pwd", createHash("pwd")));
        /* 2 */ users.add(new UserDocument("filip", createHash("123")));
        /* 3 */ users.add(new UserDocument("markos", createHash("qwerty")));
        /* 4 */ users.add(new UserDocument("testownik", createHash("test")));
        users.stream().forEach(userDB::save);

        List<LanguageDocument> languages = new ArrayList<LanguageDocument>();
        /* 0 */ languages.add(new LanguageDocument("Angielski", "1"));
        /* 1 */ languages.add(new LanguageDocument("Niemiecki", "2"));
        /* 2 */ languages.add(new LanguageDocument("Francuski", "3"));
        /* 3 */ languages.add(new LanguageDocument("Hiszpański", "4"));
        /* 4 */ languages.add(new LanguageDocument("Włoski", "5"));
        languages.stream().forEach(langDB::save);

        List<DeckDocument> decks = new ArrayList<DeckDocument>();
        /* 0 */ decks.add(new DeckDocument(users.get(1).getId(), users.get(1).getUsername(), "Sporty", "Proste słówka, dyscypliny sportowe", 1, languages.get(0).getId()));
        /* 1 */ decks.add(new DeckDocument(users.get(1).getId(), users.get(1).getUsername(), "Części do Ursusa", "Słownictwo fachowe, dla mechaników i serwisantów traktorów w Anglii", 5, languages.get(0).getId()));
        /* 2 */ decks.add(new DeckDocument(users.get(1).getId(), users.get(1).getUsername(), "Jedzenie", "Posiłki, jedzeinie i napoje", 1, languages.get(0).getId()));
        /* 3 */ decks.add(new DeckDocument(users.get(1).getId(), users.get(1).getUsername(), "Szkoła", "Słownictwo związane z przedmiotawmi szkolnymi", 1, languages.get(0).getId()));
        /* 4 */ decks.add(new DeckDocument(users.get(1).getId(), users.get(1).getUsername(), "Kolory", "Nazwy kolorów", 1, languages.get(0).getId()));
        /* 5 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Owoce i warzywa - część 1", "Proste słówka, owoce i warzywa", 1, languages.get(0).getId()));
        /* 6 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Owoce i warzywa - część 2", "Proste słówka, owoce i warzywa", 2, languages.get(0).getId()));
        /* 7 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Owoce i warzywa - część 3", "Proste słówka, owoce i warzywa", 3, languages.get(0).getId()));
        /* 8 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Owoce i warzywa - część 4", "Proste słówka, owoce i warzywa", 4, languages.get(0).getId()));
        /* 9 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Owoce i warzywa - część 5", "Proste słówka, owoce i warzywa", 5, languages.get(0).getId()));
        /* 10 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Ubranie - część 1", "Proste słówka, ubranie", 1, languages.get(0).getId()));
        /* 11 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Ubranie - część 2", "Proste słówka, ubranie", 2, languages.get(0).getId()));
        /* 12 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Ubranie - część 3", "Proste słówka, ubranie", 3, languages.get(0).getId()));
        /* 13 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Ubranie - część 4", "Proste słówka, ubranie", 4, languages.get(0).getId()));
        /* 14 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Ubranie - część 5", "Proste słówka, ubranie", 5, languages.get(0).getId()));
        /* 15 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Części ciała - część 1", "Proste słówka, części ciała", 1, languages.get(1).getId()));
        /* 16 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Części ciała - część 2", "Proste słówka, części ciała", 2, languages.get(1).getId()));
        /* 17 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Części ciała - część 3", "Proste słówka, części ciała", 3, languages.get(1).getId()));
        /* 18 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Części ciała - część 4", "Proste słówka, części ciała", 4, languages.get(1).getId()));
        /* 19 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Części ciała - część 5", "Proste słówka, części ciała", 5, languages.get(1).getId()));
        /* 20 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Sprzęty domowe - część 1", "Proste słówka, sprzęty domowe", 1, languages.get(2).getId()));
        /* 21 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Sprzęty domowe - część 2", "Proste słówka, sprzęty domowe", 2, languages.get(2).getId()));
        /* 22 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Sprzęty domowe - część 3", "Proste słówka, sprzęty domowe", 3, languages.get(2).getId()));
        /* 23 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Sprzęty domowe - część 4", "Proste słówka, sprzęty domowe", 4, languages.get(2).getId()));
        /* 24 */ decks.add(new DeckDocument(users.get(0).getId(), users.get(0).getUsername(), "Sprzęty domowe - część 5", "Proste słówka, sprzęty domowe", 5, languages.get(2).getId()));
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

        markDB.save(new MarkDocument(decks.get(0).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(0).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(0).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(0).getId(), users.get(3).getId(), 1));
        markDB.save(new MarkDocument(decks.get(1).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(1).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(1).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(1).getId(), users.get(3).getId(), 1));
        markDB.save(new MarkDocument(decks.get(2).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(2).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(2).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(2).getId(), users.get(3).getId(), 1));
        markDB.save(new MarkDocument(decks.get(3).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(3).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(3).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(3).getId(), users.get(3).getId(), 1));
        markDB.save(new MarkDocument(decks.get(4).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(4).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(4).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(4).getId(), users.get(3).getId(), 1));
        markDB.save(new MarkDocument(decks.get(5).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(5).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(5).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(6).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(6).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(6).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(7).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(7).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(7).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(8).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(8).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(8).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(9).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(9).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(10).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(10).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(11).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(11).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(12).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(12).getId(), users.get(2).getId(), 1));
        markDB.save(new MarkDocument(decks.get(13).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(14).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(15).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(16).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(17).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(17).getId(), users.get(1).getId(), 2));
        markDB.save(new MarkDocument(decks.get(17).getId(), users.get(2).getId(), 3));
        markDB.save(new MarkDocument(decks.get(17).getId(), users.get(3).getId(), 4));
        markDB.save(new MarkDocument(decks.get(18).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(18).getId(), users.get(1).getId(), 1));
        markDB.save(new MarkDocument(decks.get(18).getId(), users.get(2).getId(), 2));
        markDB.save(new MarkDocument(decks.get(18).getId(), users.get(3).getId(), 2));
        markDB.save(new MarkDocument(decks.get(19).getId(), users.get(0).getId(), 1));
        markDB.save(new MarkDocument(decks.get(19).getId(), users.get(1).getId(), 2));
        markDB.save(new MarkDocument(decks.get(19).getId(), users.get(2).getId(), 2));
        markDB.save(new MarkDocument(decks.get(19).getId(), users.get(3).getId(), 3));
        markDB.save(new MarkDocument(decks.get(20).getId(), users.get(0).getId(), 2));
        markDB.save(new MarkDocument(decks.get(20).getId(), users.get(1).getId(), 3));
        markDB.save(new MarkDocument(decks.get(20).getId(), users.get(2).getId(), 3));
        markDB.save(new MarkDocument(decks.get(20).getId(), users.get(3).getId(), 4));
    }
}
