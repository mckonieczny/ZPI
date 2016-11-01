package server;

import com.mongodb.MongoClient;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.mongo.credentials.authenticator.MongoAuthenticator;
import spark.TemplateEngine;

import static database.document.UserDocument.M_PASSWORD;
import static database.document.UserDocument.M_USERNAME;
import static database.mongo.MongoConfig.MONGO_DATABASE;
import static database.mongo.MongoConnection.getMongoClient;
import static database.repository.UserRepository.C_USER;


/**
 * Created by DjKonik on 2016-11-01.
 */
public class SecurityConfig implements ConfigFactory {

    private TemplateEngine templateEngine;

    public SecurityConfig(TemplateEngine templateEngine) {

        this.templateEngine = templateEngine;
    }

    @Override
    public Config build() {

        FormClient formClient = new FormClient("/login", getMongoAuthenticator());
        Clients clients = new Clients("/callback", formClient);

        Config config = new Config(clients);
        //config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));
        config.setHttpActionAdapter(new SecurityHttpActionAdapter(templateEngine));
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
