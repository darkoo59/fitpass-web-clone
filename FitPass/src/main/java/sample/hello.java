package sample;

import com.google.gson.Gson;
import dataHandler.UsersDataHandler;
import model.User;

import java.util.ArrayList;

import static spark.Spark.*;

public class hello {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        UsersDataHandler dh = new UsersDataHandler();
        ArrayList<User> users = dh.loadDataFromFile();
        for (User user:users) {
            System.out.println(user.getBirthDate());
        }
        staticFiles.location("/static/vue/dist");
        port(8081);
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        get("/index" , (req,res) -> {
            res.type("application/html");
            res.redirect("index.html");
            return "";
        });

        post("/forma", (req, res) -> {
            return req.queryParams("polje");
        });
    }
}
