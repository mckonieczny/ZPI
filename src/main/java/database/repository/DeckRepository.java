package database.repository;

import com.mongodb.BasicDBObject;
import database.document.DeckDocument;
import database.mongo.MongoRepository;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static database.document.DeckDocument.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class DeckRepository extends MongoRepository<DeckDocument> {

    public final static String C_DECKS = "decks";
    public final static int PAGE_OFFSET = 10;

    private UserRepository userRepository = new UserRepository();


    public DeckRepository() {
        super(C_DECKS);
    }

    public List<DeckDocument> findAll() {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find()
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

    public List<DeckDocument> findAll(int page) {

        return findAll(page, PAGE_OFFSET);
    }

    public List<DeckDocument> findAll(int page, int offset) {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find()
                .skip(offset * page)
                .limit(offset)
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

    public List<DeckDocument> search(String keyword, int page) {

        return search(keyword, page, PAGE_OFFSET);
    }

    public List<DeckDocument> search(String keyword, int page, int offset) {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find(searchCommand(keyword))
                .skip(offset * page)
                .limit(offset)
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

    public List<DeckDocument> search(String keyword) {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find(searchCommand(keyword))
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

    private Bson searchCommand(String keyword) {

        List<Bson> searchCommands = new ArrayList<Bson>();

        searchCommands.add(new BasicDBObject(M_OWNER, compile(keyword, CASE_INSENSITIVE)));

        List<Bson> subNameSearch = new ArrayList<Bson>();
        for (String word : keyword.split(" ")) {
            subNameSearch.add(new BasicDBObject(M_NAME, compile(word, CASE_INSENSITIVE)));
        }
        searchCommands.add(and(subNameSearch));

        List<Bson> subDescSearch = new ArrayList<Bson>();
        for (String word : keyword.split(" ")) {
            subDescSearch.add(new BasicDBObject(M_DESCRIPTION, compile(word, CASE_INSENSITIVE)));
        }
        searchCommands.add(and(subDescSearch));

        return or(searchCommands);
    }

    public List<DeckDocument> findByOwnerId(String ownerId) {

        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find(eq(DeckDocument.M_OWNER_ID, ownerId))
                .map(card -> new DeckDocument((Document) card))
                .into(decks);

        return decks;
    }

    public DeckDocument findByDeckId(String id){
        List<DeckDocument> decks = new ArrayList<>();
        getCollection()
                .find(eqId(id))
                .map(deck -> new DeckDocument((Document)deck))
                .into(decks);
        return decks.size() > 0 ? decks.get(0) : null;
    }
    
    public Object delete(String id){
        Object deleted = getCollection().findOneAndDelete(eq(DeckDocument.M_ID, new ObjectId(id)));
        return deleted;
    }

    public void updateSize(String deckId, int newSize){
        getCollection().updateOne(eqId(deckId), new BasicDBObject("$set", new Document(DeckDocument.M_SIZE, newSize)) );
    }

    public void increaseSize(String deckId) {
        DeckDocument deck = findByDeckId(deckId);
        if (!deckId.isEmpty()) {
            updateSize(deckId, deck.getSize() + 1);
        }
    }

    public void decreaseSize(String deckId){
        DeckDocument deck =  findByDeckId(deckId);
        if(deck != null){
            updateSize(deckId, deck.getSize()-1);
        }
    }
}
