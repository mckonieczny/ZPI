package database.mongo;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by DjKonik on 2016-10-08.
 */
public abstract class MongoDocument {

    public final static String M_ID = "_id";

    private Document document;


    public MongoDocument() {
        this.document = new Document();
    }

    public MongoDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public ObjectId getObjectId() {
        return (ObjectId) document.get(M_ID);
    }

    public boolean isEmpty() {
        return document == null;
    }

    public String getId() {
        return getObjectId().toString();
    }

    public String toJson() {return document.toJson();}
}
