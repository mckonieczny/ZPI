package security;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by DjKonik on 2016-11-05.
 */
public class PasswordHashTest {

    @Test
    public void shouldValidateHashedPassword() throws Exception {
        // given
        String password1 = "password";
        String password2 = "password";

        //when
        String hash = PasswordHash.createHash(password1);

        //then
        assertTrue(PasswordHash.validatePassword(password2, hash));
    }

    @Test
    public void shouldNotValidateHashedPassword() throws Exception {
        // given
        String password1 = "password1";
        String password2 = "password2";

        //when
        String hash = PasswordHash.createHash(password1);

        //then
        assertFalse(PasswordHash.validatePassword(password2, hash));
    }
}
