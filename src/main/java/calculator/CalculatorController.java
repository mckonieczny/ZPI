package calculator;/*
 * Created by Michał Konieczny on 2017-04-28.
 */

import controllers.AbstractController;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import java.util.Collection;

import static spark.Spark.get;
import static spark.Spark.post;

public class CalculatorController  extends AbstractController {

    Calculator calculator = new Calculator();

    @Override
    public void registerRestApi() {

        get("/calculator", (req, res) ->
            "<form method=\"POST\" enctype=\"multipart/form-data\">" +
            "<div>input file: <input type=\"file\" name=\"in\" /></div>" +
            "<div>output file: <input type=\"file\" name=\"out\" /></div>" +
            "<div><input type=\"submit\" value=\"send\"/></div>" +
            "</form>"
        );

        post("/calculator", "multipart/form-data", (request, response) -> {

            String location = "image";          // the directory location where files will be stored
            long maxFileSize = 100000000;       // the maximum size allowed for uploaded files
            long maxRequestSize = 100000000;    // the maximum size allowed for multipart/form-data requests
            int fileSizeThreshold = 1024;       // the size threshold after which files will be written to disk

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    location, maxFileSize, maxRequestSize, fileSizeThreshold);
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                    multipartConfigElement);

            return calculator.calculate(request.raw().getPart("in"), request.raw().getPart("out"));
        });
    }
}
