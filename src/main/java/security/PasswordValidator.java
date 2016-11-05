package security;

import org.pac4j.core.credentials.password.PasswordEncoder;

/**
 * Created by DjKonik on 2016-11-05.
 */
public class PasswordValidator implements PasswordEncoder {

    @Override
    public String encode(String password) {
        try {
            return PasswordHash.createHash(password);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean matches(String plainPassword, String encodedPassword) {
        try {
            return PasswordHash.validatePassword(plainPassword, encodedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
