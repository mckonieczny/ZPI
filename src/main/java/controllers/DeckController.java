package controllers;

import database.document.*;
import database.repository.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static database.mongo.MongoUtils.toJson;
import static java.lang.Integer.parseInt;
import static java.net.HttpURLConnection.*;
import static java.util.stream.Collectors.toList;
import static org.bson.Document.parse;
import static security.LoginHandler.loggedUserId;
import static security.LoginHandler.loggedUserName;
import static server.SparkUtils.empty;
import static server.SparkUtils.notEmpty;
import static spark.Spark.get;
import static spark.Spark.post;


/**
 * Created by Andrzej on 2016-11-09.
 */
public class DeckController extends AbstractController {

    private MarkRepositpry markRepository;
    private DeckRepository deckRepository;
    private FavoriteRepository favoriteRepository = new FavoriteRepository();
    private UserRepository userRepository = new UserRepository();
    private LanguageRepository languageRepository = new LanguageRepository();

    public DeckController(){

        this.deckRepository = new DeckRepository();
        this.markRepository = new MarkRepositpry();
    }

    public void registerRestApi(){

        get("/api/decks", (req, res) -> {

            String keyword = req.queryParams("keyword");
            int page = -1;
            int limit = -1;
            try {
                page = parseInt(req.queryParams("page"));
            } catch (NumberFormatException e) {}
            try {
                limit = parseInt(req.queryParams("limit"));
            } catch (NumberFormatException e) {}

            List<DeckDocument> decks = new ArrayList<DeckDocument>();

            if (empty(keyword) && page >= 0 && limit >= 0) {
                decks = deckRepository.findAll(page, limit);
            }
            if (empty(keyword) && page >= 0 && limit < 0) {
                decks = deckRepository.findAll(page);
            }
            if (empty(keyword) && page < 0) {
                decks = deckRepository.findAll();
            }
            if (notEmpty(keyword) && page >= 0 && limit >= 0) {
                decks = deckRepository.search(keyword, page, limit);
            }
            if (notEmpty(keyword) && page >= 0 && limit < 0) {
                decks = deckRepository.search(keyword, page);
            }
            if (notEmpty(keyword) && page < 0) {
                decks = deckRepository.search(keyword);
            }

            setFavorites(decks, loggedUserId(req, res));
            setLanguage(decks);
            setMark(decks);

            int filter = 0;
            try {
                filter = parseInt(req.queryParams("filter"));
            } catch (NumberFormatException e) {}

            if(filter == 1) {
                decks.sort(bestMarkComparator);
            }
            if (filter == 2) {
                decks.sort(mostVotesComparator);
            }

            return toJson(decks);
        });

        get("/api/favorite/decks", (req, res) -> {
            List<DeckDocument> decks = deckRepository.findAll();
            setFavorites(decks, loggedUserId(req, res));
            setLanguage(decks);
            setMark(decks);
            decks = decks.stream()
                    .filter(DeckDocument::isFavorite)
                    .collect(toList());

            return toJson(decks);
        });

        get("/api/user/decks", (req, res) -> {
            List<DeckDocument> decks =  deckRepository.findByOwnerId(loggedUserId(req, res));
            setFavorites(decks, loggedUserId(req, res));
            setLanguage(decks);
            setMark(decks);
            return toJson(decks);
        });

        post("/api/decks/create", (req, resp)-> {
            String response = "";
            try{
                DeckDocument deck = new DeckDocument(parse(req.body()));

                //TODO do usuniecia po dodaniu pola w formularzu na froncie
                if (empty(deck.getLanguage())) {
                    deck.setLanguage(new LanguageRepository().findAll().get(0).getId());
                }

                int dif = deck.getDifficulty();
                if(notEmpty(deck.getName()) && dif > 0 && dif <= 5) {
                    deck.setOwnerId(loggedUserId(req, resp));
                    deck.setOwner(loggedUserName(req,resp));
                    deckRepository.save(deck);
                    resp.status(HTTP_OK);
                    response = deck.getId();
                }else {
                    resp.status(HTTP_BAD_REQUEST);
                    response = "Owner or name is missing";
                }
            } catch (ClassCastException exception){
                resp.status(HTTP_BAD_REQUEST);
                response = "Bad format of difficulty";
            }catch (Exception  e){
                resp.status(HTTP_INTERNAL_ERROR);
                response = e.getMessage();
            }

            return response;
        });

        post("/api/decks/:id/delete", (req, resp) -> {
            String result = "";
            String id = req.params("id");
            if(notEmpty(id)){
                try{
                    Object deck = deckRepository.delete(id);
                    CardRepository cardRepository = new CardRepository();
                    List<CardDocument> cards = cardRepository.findByDeckId(id);
                    for (CardDocument card: cards) {
                        cardRepository.delete(card);
                    }

                    if(deck != null){
                        resp.status(HTTP_OK);
                    }
                    else{
                        resp.status(HTTP_BAD_REQUEST);
                    }
                }catch (Exception e){
                    resp.status(HTTP_INTERNAL_ERROR);
                    result = e.getMessage();
                }
            }else {
                resp.status(HTTP_BAD_REQUEST);
                result = "Deck Id can not be null";
            }

            return result;
        });

        post("/api/decks/:id/mark", (request, response) -> {
            String result = "";
            try{
                MarkDocument mark = new MarkDocument(parse(request.body()));
                if(mark.getMark() >= 1 && mark.getMark() <= 5){
                    mark.setDeckId(request.params("id"));
                    mark.setOwnerId(loggedUserId(request, response));
                    if(markRepository.isExistingMark(mark.getDeckID(), mark.getOwerId())){
                        markRepository.updateMark(mark.getDeckID(), mark.getOwerId(), mark.getMark());
                        result = mark.toJson();
                    }else{
                        markRepository.save(mark);
                        result = mark.toJson();
                    }
                    response.status(HTTP_OK);
                }else {
                    response.status(HTTP_BAD_REQUEST);
                    result = "Mark must fit in range [1, 5]!";
                }

            }catch (Exception e){
                result = e.getMessage();
                response.status(HTTP_INTERNAL_ERROR);
            }

            return result;
        });

        get("/api/decks/:id/mark", (request, response) -> {
            String deckId = request.params("id");
            List<MarkDocument> marks = markRepository.findByDeckId(deckId);
            int mark = 0;
            for(int i = 0; i< marks.size(); i++) {
                mark += marks.get(i).getMark();
            }
            mark = marks.size()>0? mark/marks.size():0;

            int userMark = markRepository.findByDeckIdAndUserId(deckId, loggedUserId(request, response))
                    .filter(MarkDocument::isNotEmpty)
                    .map(MarkDocument::getMark)
                    .orElse(0);

            return new Document()
                    .append("mark", mark)
                    .append("votes", marks.size())
                    .append("userMark", userMark)
                    .toJson();
        });

        get("/api/marks", (request, response) -> toJson(markRepository.findAll()));
    }

    private List<DeckDocument> setFavorites(List<DeckDocument> decks, String userId) {
        List<String> favorites = favoriteRepository.findByUserId(userId)
                .stream()
                .map(FavoriteDocument::getDeckId)
                .collect(toList());

        for(DeckDocument deck : decks) {
            deck.setFavorite(favorites.contains(deck.getId()));
        }
        return decks;
    }

    private List<DeckDocument> setLanguage(List<DeckDocument> decks) {

        List<LanguageDocument> languages = languageRepository.findAll();

        for (DeckDocument deck : decks) {

            languages.stream()
                    .filter(language -> language.getId().equals(deck.getLanguage()))
                    .findFirst()
                    .ifPresent(language -> {
                        deck.setLanguage(language.getDocument());
                    });
        }
        return decks;
    }

    private List<DeckDocument> setMark(List<DeckDocument> decks) {

        List<MarkDocument> marks = markRepository.findAll();

        for (DeckDocument deck : decks) {

            int votes = Math.toIntExact(marks.stream()
                    .filter(m -> Objects.equals(deck.getId(), m.getDeckID()))
                    .count());

            double mark =  marks.stream()
                    .filter(m -> Objects.equals(deck.getId(), m.getDeckID()))
                    .mapToInt(MarkDocument::getMark)
                    .average()
                    .orElse(0);

            deck.setMark(mark);
            deck.setVotes(votes);
        }

        return decks;
    }

    private Comparator bestMarkComparator = new Comparator<DeckDocument>() {
        @Override
        public int compare(DeckDocument deck1, DeckDocument deck2) {
            return Double.compare(deck2.getMark(), deck1.getMark());
        }
    };

    private Comparator mostVotesComparator = new Comparator<DeckDocument>() {
        @Override
        public int compare(DeckDocument deck1, DeckDocument deck2) {
            return Integer.compare(deck2.getVotes(), deck1.getVotes());
        }
    };
}
