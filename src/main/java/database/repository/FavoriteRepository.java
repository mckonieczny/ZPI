package database.repository;

import database.document.FavoriteDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static database.document.FavoriteDocument.M_DECK_ID;
import static database.document.FavoriteDocument.M_USER_ID;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class FavoriteRepository extends MongoRepository<FavoriteDocument> {

    public final static String C_DECKS = "decks";


    public FavoriteRepository() {
        super(C_DECKS);
    }

    public boolean isFavorite(String userId, String deckId) {

        Optional<FavoriteDocument> favorite = findFavorite(userId, deckId);

        return !favorite.map(FavoriteDocument::isEmpty).orElse(true);
    }

    public FavoriteDocument save(String userId, String deckId) {

        FavoriteDocument favorite = new FavoriteDocument(userId, deckId);
        save(favorite);

        return favorite;
    }

    public void delete(String userId, String deckId) {

        Optional<FavoriteDocument> favorite = findFavorite(userId, deckId);

        favorite.ifPresent(fav -> delete(fav));

    }

    public List<FavoriteDocument> findByUserId(String userId) {

        List<FavoriteDocument> favorites = new ArrayList<>();
        getCollection()
                .find(eq(M_USER_ID, userId))
                .map(favorite -> new FavoriteDocument((Document) favorite))
                .into(favorites);

        return favorites;
    }

    private Optional<FavoriteDocument> findFavorite(String userId, String deckId) {

        List<FavoriteDocument> favorites = new ArrayList<>();

        getCollection()
                .find(and(eq(M_USER_ID, userId), eq(M_DECK_ID, deckId)))
                .map(card -> new FavoriteDocument((Document) card))
                .into(favorites);

        if (favorites.isEmpty()) {
            return Optional.ofNullable(null);
        }

        return Optional.ofNullable(favorites.get(0));
    }

}
