package sample;
import com.google.gson.Gson;
import spark.Filter;

import static spark.Spark.*;

import java.io.File;
import java.net.URL;


public class hello {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        staticFileLocation("static/vue/dist");
        port(8080);
        // Site pages
        after((Filter) (req, res) -> {
            res.header("Acces-Control-Allow-Origin","*");
            res.header("Acces-Control-Allow-Methods", "GET");
        });
        get("/something" , (req,res) -> {
            res.type("application/html");
            res.redirect("index.html");
            return "";
        });
    }
}
