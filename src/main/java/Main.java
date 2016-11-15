import controllers.DeckController;
import controllers.FavoriteController;
import samples.Samples;
import security.LoginHandler;
import static server.SparkUtils.getHerokuAssignedPort;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

/**
 * Created by DjKonik on 2016-10-06.
 */
public class Main {

    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        LoginHandler loginHandler = new LoginHandler();
        loginHandler.setLoginRestApi();
        loginHandler.setRegisterRestApi();

        DeckController deckController = new DeckController();
        deckController.setRestApi();

        new FavoriteController().setRestApi();

        Samples.create(loginHandler);
    }
}
