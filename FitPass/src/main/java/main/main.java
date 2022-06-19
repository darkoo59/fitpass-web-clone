package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.User;
import service.AccountService;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;

import java.time.LocalDate;

import static spark.Spark.*;

public class main {
    private static Gson gson = new Gson();
    private static AccountService accountService;
    public static void main(String[] args) throws Exception {
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

        accountService = new AccountService();

        get("/home" , (req,res) -> {
            res.type("application/html");
            res.redirect("index.html");
            return "";
        });

        get("/login", (req, res) -> {
            return "Uspesno ulogovan!";
        });

        get("/user", (req, res) -> {
            res.redirect("http://localhost:8080/");
            return "BORA KONJ";
        });

        post("/login", (req,res) -> {
            System.out.println("DOSLO JEEEE");
            User user = accountService.loginUser(req);
            res.type("application/json");
            res.body("Username = "+user.getUsername() + " and password = "+user.getPassword() + "BORA KONJ");
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
            Gson gson2 = gsonBuilder.setPrettyPrinting().create();
            String str = gson2.toJson(user);
            res.redirect("http://localhost:8080/login");
            return str;
        });
    }
}
