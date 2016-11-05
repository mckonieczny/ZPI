package security;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;

import java.util.HashMap;

import static server.SparkUtils.templateEngine;
import static spark.Spark.halt;

/**
 * Created by DjKonik on 2016-11-01.
 */
public class SecurityHttpActionAdapter extends DefaultHttpActionAdapter {

    @Override
    public Object adapt(int code, SparkWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
            halt(401, templateEngine().render(new ModelAndView(new HashMap<>(), "error401.ftl")));
        } else if (code == HttpConstants.FORBIDDEN) {
            halt(403, templateEngine().render(new ModelAndView(new HashMap<>(), "error403.ftl")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}