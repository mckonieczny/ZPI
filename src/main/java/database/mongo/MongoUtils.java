package database.mongo;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.join;

/**
 * Created by DjKonik on 2016-10-30.
 */
public class MongoUtils {

    public static String toJson(List<? extends MongoDocument> list) {

        List<String> jsonList = list.stream()
                .map(MongoDocument::toJson)
                .collect(Collectors.toList());

        return "[" + join(",", jsonList) + "]";
    }
}
