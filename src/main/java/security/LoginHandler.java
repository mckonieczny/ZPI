package security;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static security.SecurityConfig.*;
import static server.SparkUtils.templateEngine;
import static spark.Redirect.Status.TEMPORARY_REDIRECT;
import static spark.Spark.*;

/**
 * Created by DjKonik on 2016-11-05.
 */
public class LoginHandler {

    private static final String LOGIN_VIEW = "login/login.ftl";
    private static final String LOGGED_USER_VIEW = "login/user.ftl";

    private static final String M_PROFILES = "profiles";

    private Config config;
    private Route callback;

    public LoginHandler() {

        config =  new SecurityConfig().build();
        callback = new CallbackRoute(config, URL_LOGGED_USER_REST_API);

        get(URL_CALLBACK, callback);
        post(URL_CALLBACK, callback);

        get(URL_LOGOUT, new ApplicationLogoutRoute(config, "/"));
    }

    public Config getConfig() {

        return config;
    }
/*
    public void setLoginForm() {

        get(URL_LOGIN_FORM, (req, res) -> loginFormView(), templateEngine());
    }
*/
    public void setLoginRestApi() {

        get(URL_LOGIN_REST_API, (req, res) -> "{\"auth\":403}");
        redirect.post(URL_LOGIN_REST_API, callbackUrl(), TEMPORARY_REDIRECT);

        get("/api/login2", (req, res) -> loginRestApiView(), templateEngine()); //TODO wyczyscic
        redirect.post("/api/login2", callbackUrl(), TEMPORARY_REDIRECT);

        before(URL_LOGGED_USER_REST_API, new SecurityFilter(config, FORM_CLIENT));
        get(URL_LOGGED_USER_REST_API, (req, res) -> "{\"auth\":200, \"user\":{\"username\":\"user name\", \"userId\":\"1\"}}"); //TODO prawdziwe dane
    }
/*
    private ModelAndView loginFormView() {

        Map<String, Object> model = new HashMap<>();
        FormClient formClient = config.getClients().findClient(FormClient.class);

        model.put("callbackUrl", formClient.getCallbackUrl());

        return new ModelAndView(model, "logon/loginForm.ftl");
    }
*/
    private ModelAndView loginRestApiView() {

        Map<String, Object> model = new HashMap<>();

        return new ModelAndView(model, LOGIN_VIEW);
    }

    private ModelAndView loggedUserView(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        model.put(M_PROFILES, getProfiles(req, res));

        return new ModelAndView(model, LOGGED_USER_VIEW);
    }


    private String callbackUrl() {

        FormClient formClient = config.getClients().findClient(FormClient.class);

        return formClient.getCallbackUrl();
    }

    private List<CommonProfile> getProfiles(Request request, Response response) {

        SparkWebContext context = new SparkWebContext(request, response);
        ProfileManager manager = new ProfileManager(context);

        return manager.getAll(true);
    }
}
