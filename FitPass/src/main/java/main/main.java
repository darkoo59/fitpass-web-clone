package main;

import com.google.gson.Gson;
import io.jsonwebtoken.*;
import controller.AccountController;
import dao.SportsFacilityDAO;
import model.SportsFacility;
import service.AccountService;
import com.google.gson.GsonBuilder;
import model.User;
import service.SportsFacilityService;
import utils.enums.SportsFacilityStatus;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;
import utils.others.LocalTimeConverter;
import utils.others.WorkHour;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static spark.Spark.*;

public class main {
    private static Gson gson;
    private static AccountService accountService;
    private static SportsFacilityService facilitiesService;
    public static void main(String[] args) throws Exception {

        initializeContext();

        staticFiles.location("/static/vue/dist");
        port(8081);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeConverter());
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

        get("/home" , (req,res) -> {
            res.type("application/html");
            res.redirect("index.html");
            return "";
        });

        post("/register", (request, response) -> {
            AccountService service = new AccountService();
            service.register(request);
            response.redirect("http://localhost:8080");
            return "success";
        });

        get("/login", (req, res) -> {
            return "Uspesno ulogovan!";
        });

//        get("/user", (req, res) -> {
//            res.redirect("http://localhost:8080/");
//            return "BORA KONJ";
//        });
        get("/user", AccountController::getUser);

        get("/sportsFacilities", (req, res) -> {
            res.type("application/json");
            return gson.toJson(facilitiesService.getAll());
        });

        post("/login","application/json", (req,res) -> {
            System.out.println(req.body());
            User user = accountService.loginUser(req);
            res.type("application/json");
            System.out.println(gson.toJson(user));
            return gson.toJson(user);
        });
    }

    private static void initializeContext() {
        AccountService accountService = new AccountService();
        AccountController.initializeService(accountService);
    }
}
