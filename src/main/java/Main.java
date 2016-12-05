import controllers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samples.Samples;
import security.LoginHandler;
import server.SparkUtils;

import static server.SparkUtils.getHerokuAssignedPort;
import static spark.Spark.exception;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

/**
 * Created by DjKonik on 2016-10-06.
 */
public class Main {

    static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        LoginHandler loginHandler = new LoginHandler();
        loginHandler.setLoginRestApi();
        loginHandler.setRegisterRestApi();

        FrontClientController.create();

        AbstractController controller = new DeckController();
        controller.registerRestApi();

        controller = new CardController();
        controller.registerRestApi();

        new FavoriteController().registerRestApi();

        new PathController().registerRestApi();

        Samples.create(loginHandler);

        SparkUtils.enableCORS();

        initLogger();
    }

    private static void initLogger() {

        LOG.trace("Hello World!");
        LOG.debug("How are you today?");
        LOG.info("I am fine.");
        LOG.warn("I love programming.");
        LOG.error("I am programming.");

        exception(Exception.class, (e, request, response) -> {
            LOG.error("Unexpected exception", e);
        });
    }
}
