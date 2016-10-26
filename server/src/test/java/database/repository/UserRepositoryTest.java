package database.repository;

import database.document.UserDocument;
import org.junit.Before;
import org.junit.Test;

import static java.util.UUID.randomUUID;

/**
 * Created by DjKonik on 2016-10-08.
 */
public class UserRepositoryTest {

    private UserRepository userRepository;
    private final static String T_USERNAME = randomUUID().toString();
    private final static String T_PASSWORD = randomUUID().toString();

    @Before
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void shouldAddUser() {
        //given
        UserDocument user = new UserDocument(T_USERNAME, T_PASSWORD);

        //when
        userRepository.save(user);

        //then
        assert(user.getId() != null);
    }

    @Test
    public void shouldFindUserByName() {
        //given

        //when
        UserDocument result = userRepository.findByName(T_USERNAME);

        //then
        assert(result.getDocument() != null);
    }

    @Test
    public void shouldDeleteUser() {
        //given
        UserDocument user = userRepository.findByName(T_USERNAME);

        //when
        userRepository.delete(user);
        UserDocument result = userRepository.findByName(T_USERNAME);

        //then
        assert(result.getDocument() == null);
    }
}