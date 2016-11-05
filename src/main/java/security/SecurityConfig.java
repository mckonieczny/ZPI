package security;

import com.mongodb.MongoClient;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.mongo.credentials.authenticator.MongoAuthenticator;

import static database.document.UserDocument.M_PASSWORD;
import static database.document.UserDocument.M_USERNAME;
import static database.mongo.MongoConfig.MONGO_DATABASE;
import static database.mongo.MongoConnection.getMongoClient;
import static database.repository.UserRepository.C_USER;


/**
 * Created by DjKonik on 2016-11-01.
 */
public class SecurityConfig implements ConfigFactory {

    public final static String FORM_CLIENT = "FormClient";

    public final static String URL_CALLBACK = "/callback";
    public final static String URL_LOGIN_FORM = "/login";
    public final static String URL_LOGIN_REST_API = "/api/login";
    public final static String URL_LOGGED_USER_REST_API = "/api/user";
    public final static String URL_LOGOUT = "/callback";

    @Override
    public Config build() {

        //FormClient formClient = new FormClient(URL_LOGIN_FORM, getMongoAuthenticator());
        FormClient formClient = new FormClient(URL_LOGIN_REST_API, getMongoAuthenticator());

        Clients clients = new Clients(URL_CALLBACK, formClient);

        Config config = new Config(clients);
        //config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));
        config.setHttpActionAdapter(new SecurityHttpActionAdapter());

        return config;
    }

    private MongoAuthenticator getMongoAuthenticator() {

        MongoClient mongoClient = getMongoClient();
        MongoAuthenticator auth = new MongoAuthenticator(mongoClient);

        auth.setUsersDatabase(MONGO_DATABASE);
        auth.setUsersCollection(C_USER);
        auth.setUsernameAttribute(M_USERNAME);
        auth.setPasswordAttribute(M_PASSWORD);

        return auth;
    }
}
