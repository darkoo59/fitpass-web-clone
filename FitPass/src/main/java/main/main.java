package main;

import com.google.gson.Gson;
import model.SportsFacility;
import service.AccountService;
import com.google.gson.GsonBuilder;
import model.User;
import service.AccountService;
import service.SportsFacilityService;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;
import utils.others.LocalTimeDeserializer;

import java.awt.image.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static spark.Spark.*;

public class main {
    private static Gson gson;
    private static AccountService accountService;
    private static SportsFacilityService facilitiesService;
    public static void main(String[] args) throws Exception {
        staticFiles.location("/static/vue/dist");
        port(8081);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class,new LocalTimeDeserializer());
        gson = gsonBuilder.setPrettyPrinting().create();
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
        facilitiesService = new SportsFacilityService();
        for(SportsFacility sf: facilitiesService.getAll())
        {
            System.out.println(sf.getName());
        }

        get("/home" , (req,res) -> {
            res.type("application/html");
            res.redirect("index.html");
            return "";
        });

        get("/register", (request, response) -> "");

        post("/register", (request, response) -> {
            AccountService service = new AccountService();
            service.register(request);
            return "success";
        });

        get("/login", (req, res) -> {
            return "Uspesno ulogovan!";
        });

        get("/user", (req, res) -> {
            res.redirect("http://localhost:8080/");
            return "BORA KONJ";
        });

        get("/sportsFacilities", (req, res) -> {
            res.type("application/json");
            return gson.toJson(facilitiesService.getAll());
        });

        post("/login", (req,res) -> {
            System.out.println("DOSLO JEEEE");
            User user = accountService.loginUser(req);
            res.type("application/json");
            res.body("Username = "+user.getUsername() + " and password = "+user.getPassword() + "BORA KONJ");
            String str = gson.toJson(user);
            res.redirect("http://localhost:8080/login");
            return str;
        });
    }
}
