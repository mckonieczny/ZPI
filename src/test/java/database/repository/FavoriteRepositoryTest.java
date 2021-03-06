package database.repository;

import database.document.FavoriteDocument;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class FavoriteRepositoryTest {

    private FavoriteRepository favoriteRepository;

    @Before
    public void setUp() {
        favoriteRepository = new FavoriteRepository();
    }

    @Test
    public void shouldNotBeFavorite() {
        //given
        String userId = randomUUID().toString();
        String deckId = randomUUID().toString();

        //when
        boolean isFavorite = favoriteRepository.isFavorite(userId, deckId);

        //then
        assertFalse(isFavorite);
    }

    @Test
    public void shouldAddFavorite() {
        //given
        String userId = randomUUID().toString();
        String deckId = randomUUID().toString();

        //when
        FavoriteDocument result = favoriteRepository.save(userId, deckId);

        //then
        assertNotNull(result.getDocument());
        assertNotNull(result.getId());
        assertNotEquals(result.getId(), "");

        favoriteRepository.delete(result);
    }

    @Test
    public void shouldFindFavorite() {
        //given
        String userId = randomUUID().toString();
        String deckId = randomUUID().toString();

        favoriteRepository.save(new FavoriteDocument(userId, deckId));

        //when
        boolean isFavorite = favoriteRepository.isFavorite(userId, deckId);

        //then
        assertTrue(isFavorite);
    }

    @Test
    public void shouldDeleteFavorite() {
        //given
        String userId = randomUUID().toString();
        String deckId = randomUUID().toString();

        favoriteRepository.save(new FavoriteDocument(userId, deckId));

        //when
        favoriteRepository.delete(userId, deckId);

        //then
        boolean isFavorite = favoriteRepository.isFavorite(userId, deckId);
        assertFalse(isFavorite);
    }

    @Test
    public void shouldFindFavoriteById() {
        //given
        String userId = randomUUID().toString();
        String deckId = randomUUID().toString();

        FavoriteDocument favorite = new FavoriteDocument(userId, deckId);
        favoriteRepository.save(favorite);

        //when
        Optional<FavoriteDocument> result = favoriteRepository.findById(favorite.getId());

        //then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertFalse(result.get().isEmpty());
    }

    @Test
    public void shouldDeleteFavoriteById() {
        //given
        String userId = randomUUID().toString();
        String deckId = randomUUID().toString();

        FavoriteDocument favorite = new FavoriteDocument(userId, deckId);
        favoriteRepository.save(favorite);

        //when
        favoriteRepository.deleteById(favorite.getId());

        //then
        boolean isFavorite = favoriteRepository.isFavorite(userId, deckId);
        assertFalse(isFavorite);
    }
}