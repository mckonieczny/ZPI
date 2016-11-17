package controllers;

import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static server.SparkUtils.templateEngine;
import static spark.Spark.get;


/**
 * Created by Andrzej on 2016-11-09.
 */
public class FrontClientController {


    public static void create(){

        get("/", (req, res) -> getReactPage(), templateEngine());

        get("/welcome/login", (req, res) -> getReactPage(), templateEngine());
        get("/welcome/signup", (req, res) -> getReactPage(), templateEngine());

        get("/home", (req, res) -> getReactPage(), templateEngine());
        get("/addDeck", (req, res) -> getReactPage(), templateEngine());
        get("/decks", (req, res) -> getReactPage(), templateEngine());
        get("/deck/:deckId", (req, res) -> getReactPage(), templateEngine());

    }

    private static ModelAndView getReactPage() {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "index.ftl");
    }
}
