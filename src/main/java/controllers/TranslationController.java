package controllers;

import database.document.TranslationDocument;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static spark.Spark.post;
import static org.bson.Document.parse;


/**
 * Created by Andrzej on 2017-01-11.
 */
public class TranslationController extends AbstractController {
    @Override
    public void registerRestApi() {
        post("/api/translate", ((request, response) -> {
            TranslationDocument translation = new TranslationDocument(parse(request.body()));
            String url = "https://translate.googleapis.com/translate_a/single?"+
                    "client=gtx&"+
                    "sl=" + translation.getLanguageForm() +
                    "&tl=" + translation.getLanguageTo() +
                    "&dt=t&q=" + URLEncoder.encode(translation.getmWord(), "UTF-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer translationResponse = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                translationResponse.append(inputLine);
            }
            in.close();

            String[] transTab = translationResponse.toString().split("\"");
            translation.setTranslation(transTab[1]);
            return translation.toJson();
        }));
    }
}
