package database.repository;

import com.mongodb.BasicDBObject;
import database.document.UserDocument;
import database.mongo.MongoRepository;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static database.document.UserDocument.M_USERNAME;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class UserRepository extends MongoRepository<UserDocument> {

    public final static String C_USER = "users";


    public UserRepository() {
        super(C_USER);
    }

    public UserDocument findByName(String name) {
        return new UserDocument((Document) getCollection().find(eq(M_USERNAME, name)).first());
    }

    public List<UserDocument> search(String keyword) {

        List<UserDocument> users = new ArrayList<>();
        getCollection()
                .find(searchCommand(keyword))
                .map(user -> new UserDocument((Document) user))
                .into(users);

        return users;
    }

    private Bson searchCommand(String keyword) {

        List<Bson> searchCommands = new ArrayList<Bson>();

        List<Bson> subSearch = new ArrayList<Bson>();
        for (String word : keyword.split(" ")) {
            subSearch.add(new BasicDBObject(M_USERNAME, compile(word, CASE_INSENSITIVE)));
        }
        searchCommands.add(and(subSearch));

        return or(searchCommands);
    }

}
