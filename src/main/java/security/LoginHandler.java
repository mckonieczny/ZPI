package security;

import database.document.UserDocument;
import database.repository.UserRepository;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Optional;

import static security.SecurityConfig.*;
import static server.SparkUtils.*;
import static spark.Redirect.Status.TEMPORARY_REDIRECT;
import static spark.Spark.*;

/**
 * Created by DjKonik on 2016-11-05.
 */
public class LoginHandler {

    private static final int CODE_SUCCESS = 200;
    private static final int CODE_ERROR = 403;

    private static final String M_AUTH = "\"auth\"";
    private static final String M_PROFILE = "\"user\"";
    private static final String M_PROFILE_USERNAME = "\"username\"";
    private static final String M_PROFILE_ID = "\"userId\"";


    private UserRepository userRepository = new UserRepository();
    private Config config;
    private Route callback;

    public LoginHandler() {

        config =  new SecurityConfig().build();
        callback = new CallbackRoute(config, isDeployed() ? PROD_URL + URL_LOGGED_USER_REST_API : URL_LOGGED_USER_REST_API);

        get(URL_CALLBACK, callback);
        post(URL_CALLBACK, callback);

        get(URL_LOGOUT, new ApplicationLogoutRoute(config, "/"));
    }

    public Config getConfig() {

        return config;
    }

    public void setLoginRestApi() {

        get(URL_LOGIN_REST_API, (req, res) -> responseError());
        redirect.post(URL_LOGIN_REST_API, callbackUrl(), TEMPORARY_REDIRECT);

        secureUrl(URL_LOGGED_USER_REST_API);
        get(URL_LOGGED_USER_REST_API, (req, res) -> {
            //TODO ciasteczko zalogowanej sesji ustawiane na godzinę
            res.cookie("/", "JSESSIONID", req.cookie("JSESSIONID"), 60*60, false);
            return getProfile(req, res)
                    .map(this::responseSuccess)
                    .orElse(responseError());
        });
    }

    public void setRegisterRestApi() {

        // Rejestracja z logowaniem (logownie z parametrem action=register)
        before(URL_CALLBACK, (req, res) -> {

            String action = req.queryParams("action");
            if (notEmpty(action) && action.equals("register")) {

                String username = req.queryParams("username");
                String password = req.queryParams("password");

                if (notEmpty(username) && notEmpty(username) && userRepository.findByName(username).isEmpty()) {
                    userRepository.save(new UserDocument(username, PasswordHash.createHash(password)));
                } else {
                    halt(401, responseError());
                }
            }
        });

        // Rejestracja bez logowania TODO możliwe że do wywalenia
        post("/api/register", (req, res) -> {

            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(notEmpty(username) && notEmpty(username)  && userRepository.findByName(username).isEmpty()) {

                UserDocument user = new UserDocument(username, PasswordHash.createHash(password));
                userRepository.save(user);

                return responseSuccess(user);
            } else {
                return responseError();
            }

        });
    }

    public void secureUrl(String url) {
        before(url, (req, res) -> {
            new SecurityFilter(config, FORM_CLIENT);
        });
    }

    private String responseSuccess(CommonProfile profile) {

        UserDocument user = userRepository.findByName(profile.getId()); // TODO wsyzstkie dane dostepne z poziomu CommonProfile

        return responseSuccess(user);
    };

    private String responseSuccess(UserDocument user) {

        return "{" +
            M_AUTH + ":" + CODE_SUCCESS + "," +
            M_PROFILE + ":" + "{" +
                M_PROFILE_USERNAME + ":\"" + user.getUsername() + "\"," +
                M_PROFILE_ID + ":\"" + user.getId() +  "\"" +
            "}" +
        "}";
    }

    private String responseError() {
        return "{" +
            M_AUTH + ":" + CODE_ERROR +
        "}";
    }

    private String callbackUrl() {

        FormClient formClient = config.getClients().findClient(FormClient.class);

        return formClient.getCallbackUrl();
    }

    public static Optional<CommonProfile> getProfile(Request request, Response response) {

        SparkWebContext context = new SparkWebContext(request, response);
        ProfileManager manager = new ProfileManager(context);

        return manager.get(true);
    }

    public static String loggedUserId(Request req, Response res) {

        UserRepository userRepository = new UserRepository(); // TODO repository jako singleton

        return getProfile(req, res)
                .map(CommonProfile::getId)
                .map(userRepository::findByName)
                .map(UserDocument::getId)// TODO wsyzstkie dane dostepne z poziomu CommonProfile
                .orElse("");
    }
}
