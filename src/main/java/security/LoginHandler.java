package security;

import database.document.UserDocument;
import database.repository.UserRepository;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.profile.facebook.FacebookProfile;
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
import static spark.Spark.*;

/**
 * Created by DjKonik on 2016-11-05.
 */
public class LoginHandler {

    private static final int CODE_SUCCESS = 200;
    private static final int CODE_ERROR = 400;

    private static final String M_AUTH = "\"auth\"";
    private static final String M_PROFILE = "\"user\"";
    private static final String M_PROFILE_USERNAME = "\"username\"";
    private static final String M_PROFILE_ID = "\"userId\"";
    private static final String M_TOKEN = "\"token\"";

    private UserRepository userRepository = new UserRepository();
    private JwtGenerator<CommonProfile> tokenGenerator = new JwtGenerator<>();
    private Config config;
    private Route callback;
    private SecurityFilter commonFilter;

    public LoginHandler() {

        config =  new SecurityConfig().build();
        callback = new CallbackRoute(config, isDeployed() ? PROD_URL + URL_LOGGED_USER_REST_API : URL_LOGGED_USER_REST_API, true);
        commonFilter = new SecurityFilter(config, null);

        get(URL_CALLBACK, callback);
        post(URL_CALLBACK, callback);

        get(URL_LOGOUT, new ApplicationLogoutRoute(config, "/"));

        get("/logoutFB", (req, res) -> {

            Optional<CommonProfile> profile  =  getProfile(req, res);
            if (profile.isPresent()) {
                String fbLogoutUrl = "https://www.facebook.com/logout.php?next=" + getAppUrl() + URL_LOGOUT + "&access_token=" + profile.get().getAttribute("access_token");
                res.redirect(fbLogoutUrl);
            } else {
                res.redirect(URL_LOGOUT);
            }

            return "";
        });

    }

    public Config getConfig() {

        return config;
    }

    class MyFB extends FacebookClient {

    }

    public void setLoginRestApi() {

        secureUrl(URL_LOGIN_REST_API);
        get(URL_LOGGED_USER_REST_API, this::userInfo);

        secureUrl(URL_LOGGED_USER_REST_API);
        get(URL_LOGGED_USER_REST_API, this::userInfo);

        secureUrl("/panel/login/facebook", FACEBOOK_AUTH);
        get("/panel/login/facebook", this::userInfo);

        secureUrl("/api/login/direct", DIRECT_AUTH);
        get("/api/login/direct", this::userInfo);

        get("/api/login/facebook", this::loginUserWithToken);

        //TODO test uwierzytelnienia tokenem
        secureUrl("/panel/token", TOKEN_AUTH);
        get("/panel/token", this::userInfo);
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

                return responseSuccessMongo(user);
            } else {
                return responseError();
            }

        });
    }

    public void secureUrl(String url) {
        before(url, commonFilter);
    }

    public void secureUrl(String url, String authenticator) {
        before(url, new SecurityFilter(config, authenticator));
    }

    private String loginUserWithToken(Request req, Response res) {

        CustomFacebookClient facebookClient = config.getClients().findClient(CustomFacebookClient.class);
        SparkWebContext context = new SparkWebContext(req, res);
        facebookClient.init(context);
        FacebookProfile profile = facebookClient.retrieveUserProfileFromToken(req.queryParams("token"));

        if (profile != null) {

            ProfileManager manager = new ProfileManager(context);
            profile.setClientName(FACEBOOK_AUTH);
            manager.save(true, profile, false);

            setCookieTimeout(req, res);

            return responseSuccess(profile);
        }
        else {
            return responseError();
        }
    }

    private String userInfo(Request req, Response res) {
        setCookieTimeout(req, res);
        return getProfile(req, res)
                .map(this::responseSuccess)
                .orElse(responseError());
    }

    private void setCookieTimeout(Request req, Response res) {
        //TODO ciasteczko zalogowanej sesji ustawiane na godzinę
        String sessionId = req.cookie("JSESSIONID");
        if (notEmpty(sessionId)) {
            res.cookie("/", "JSESSIONID", sessionId, 60*60, false);
        }
    }

    private String responseSuccess(CommonProfile profile) {

        switch (profile.getClass().getSimpleName()) {
            case "FacebookProfile":
                return responseSuccessFacebook(profile);
            case "MongoProfile":
                return responseSuccessMongo(profile);
        }
        return responseError();
    }

    private String responseSuccessMongo(CommonProfile profile) {

        UserDocument user = userRepository.findByName(profile.getId()); // TODO wsyzstkie dane dostepne z poziomu CommonProfile

        return "{" +
            M_AUTH + ":" + CODE_SUCCESS + "," +
            M_PROFILE + ":" + "{" +
                M_PROFILE_USERNAME + ":\"" + user.getUsername() + "\"," +
                M_PROFILE_ID + ":\"" + user.getId() +  "\"" +
                (profile.containsAttribute("iat") ? "" : ("," + M_TOKEN + ":\"" + tokenGenerator.generate(profile) +  "\"")) +
            "}" +
        "}";
    }

    private String responseSuccessMongo( UserDocument user) {

        return "{" +
            M_AUTH + ":" + CODE_SUCCESS + "," +
            M_PROFILE + ":" + "{" +
                M_PROFILE_USERNAME + ":\"" + user.getUsername() + "\"," +
                M_PROFILE_ID + ":\"" + user.getId() +  "\"" +
            "}" +
        "}";
    }

    private String responseSuccessFacebook(CommonProfile profile) {

        return "{" +
            M_AUTH + ":" + CODE_SUCCESS + "," +
            M_PROFILE + ":" + "{" +
                M_PROFILE_USERNAME + ":\"" + profile.getAttribute("name") + "\"," +
                M_PROFILE_ID + ":\"" + profile.getAttribute("third_party_id") +  "\"" +
                (profile.containsAttribute("iat") ? "" : ("," + M_TOKEN + ":\"" + tokenGenerator.generate(profile) +  "\"")) +
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

        if (getProfile(req, res).isPresent()) {
            CommonProfile profile = getProfile(req, res).get();
            switch (profile.getClass().getSimpleName()) {
                case "FacebookProfile":
                    return profile.getId();
                case "MongoProfile":
                    UserRepository userRepository = new UserRepository(); // TODO repository jako singleton
                    UserDocument user = userRepository.findByName(profile.getId());
                    return user.getId();
            }
        }
        return "";
    }
}
