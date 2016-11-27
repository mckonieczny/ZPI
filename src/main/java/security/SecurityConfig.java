package security;

import com.mongodb.MongoClient;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.mongo.credentials.authenticator.MongoAuthenticator;

import static database.document.UserDocument.M_PASSWORD;
import static database.document.UserDocument.M_USERNAME;
import static database.mongo.MongoConfig.MONGO_DATABASE;
import static database.mongo.MongoConnection.getMongoClient;
import static database.repository.UserRepository.C_USER;
import static server.SparkUtils.getAppUrl;
import static server.SparkUtils.isDeployed;


/**
 * Created by DjKonik on 2016-11-01.
 */
public class SecurityConfig implements ConfigFactory {

    public final static String FORM_AUTH = "FormClient";
    public final static String FACEBOOK_AUTH = "CustomFacebookClient";
    public final static String DIRECT_AUTH = "IndirectBasicAuthClient";
    public final static String TOKEN_AUTH = "ParameterClient";

    public final static String URL_CALLBACK = "/callback";
    public final static String URL_LOGIN_REST_API = "/api/login";
    public final static String URL_LOGGED_USER_REST_API = "/api/user";
    public final static String URL_LOGOUT = "/logout";

    @Override
    public Config build() {

        CustomFacebookClient facebookClient = new CustomFacebookClient(isDeployed() ? "1689218041388664" : "1687234428253692", "a4a1c4cae046303bc14f15c7eb495c5a");
        facebookClient.setFields("id,name,first_name,middle_name,last_name,gender,locale,languages,link,third_party_id,timezone,updated_time,verified,birthday,education,email,hometown,interested_in,location,political,favorite_athletes,favorite_teams,quotes,relationship_status,religion,significant_other,website,work");

        FormClient formClient = new FormClient(URL_LOGIN_REST_API, getMongoAuthenticator());
        IndirectBasicAuthClient indirectBasicAuthClient = new IndirectBasicAuthClient(getMongoAuthenticator());

        ParameterClient parameterClient = new ParameterClient("token", new JwtAuthenticator());
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(true);

        Clients clients = new Clients(callbackUrl(), facebookClient, formClient, indirectBasicAuthClient, parameterClient);

        Config config = new Config(clients);
        //config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));
        config.setHttpActionAdapter(new HttpActionAdapter());

        return config;
    }

    private String callbackUrl() {
        return getAppUrl() + URL_CALLBACK;
    }

    private MongoAuthenticator getMongoAuthenticator() {

        MongoClient mongoClient = getMongoClient();
        MongoAuthenticator auth = new MongoAuthenticator(mongoClient, "", new PasswordValidator());

        auth.setUsersDatabase(MONGO_DATABASE);
        auth.setUsersCollection(C_USER);
        auth.setUsernameAttribute(M_USERNAME);
        auth.setPasswordAttribute(M_PASSWORD);

        return auth;
    }
}
