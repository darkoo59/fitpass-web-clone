package sample;
import com.google.gson.Gson;

import static spark.Spark.*;

import java.io.File;


public class hello {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        staticFileLocation("static");
        //staticFiles.externalLocation(new File("./static").getCanonicalPath());
        port(8080);
        // Site pages
        get("/index" , (req,res) -> {
            res.redirect("index.html");
            return null;
        });
    }
}
