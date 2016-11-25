package security;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.profile.facebook.FacebookProfile;

/**
 * Created by DjKonik on 2016-11-25.
 */
public class CustomFacebookClient extends FacebookClient {

    public CustomFacebookClient(String key, String secret) {
        super(key, secret);
    }

    public FacebookProfile retrieveUserProfileFromToken(String token) {
        try {
            return retrieveUserProfileFromToken(new OAuth2AccessToken(token));
        } catch(Exception e) {
            return null;
        }
    }
}
