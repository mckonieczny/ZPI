package database.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class CardRepositoryTest {

    private CardRepository cardRepository;

    @Before
    public void setUp() {
        cardRepository = new CardRepository();
    }

    @Test
    public void should() {

        /*
        cardRepository.save(new CardDocument("1","alpha", "ahpla"));
        cardRepository.save(new CardDocument("2","beta", "ateb"));
        cardRepository.save(new CardDocument("2","gamma", "ammag"));
        cardRepository.save(new CardDocument("3","epsilon", "nolispe"));
        cardRepository.save(new CardDocument("3","lambda", "adbmal"));
        cardRepository.save(new CardDocument("3","omega", "agemo"));
         */

        List a = cardRepository.findByDeckId("2");

        System.out.println("");

    }

}