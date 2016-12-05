package database.repository;

import database.document.PathDocument;
import database.mongo.MongoRepository;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Andrzej on 2016-12-01.
 */
public class PathRepository extends MongoRepository<PathDocument> {

    public final static String C_PATHS = "paths";

    public PathRepository() {
        super(C_PATHS);
    }

    public List<PathDocument> findByOwnerId(String ownerId) {

        List<PathDocument> paths = new ArrayList<>();
        getCollection()
                .find(eq(PathDocument.M_OWNER_ID, ownerId))
                .map(path -> new PathDocument((Document) path))
                .into(paths);

        return paths;
    }

    public PathDocument findById(String id){
        List<PathDocument> paths = new ArrayList<>();
        getCollection()
                .find(eqId(id))
                .map(path -> new PathDocument((Document) path))
                .into(paths);
        return paths.size() > 0 ?  paths.get(0) : null;
    }
}
