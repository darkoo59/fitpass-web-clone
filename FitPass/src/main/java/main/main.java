package main;

import com.google.gson.Gson;
import controller.AccountController;
import dao.UserDAO;
import service.AccountService;
import com.google.gson.GsonBuilder;
import service.SportsFacilityService;
import utils.others.LocalDateDeserializer;
import utils.others.LocalDateSerializer;
import utils.others.LocalTimeConverter;

import java.time.LocalDate;
import java.time.LocalTime;

import static spark.Spark.*;

public class main {
    public static Gson gson;
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

        facilitiesService = new SportsFacilityService();

        get("/home" , (req,res) -> {
            res.type("application/html");
            res.redirect("index.html");
            return "";
        });


        AccountController.postRegister();
        AccountController.postLogin();
        AccountController.getUser();

        get("/sportsFacilities", (req, res) -> {
            res.type("application/json");
            return gson.toJson(facilitiesService.getAll());
        });

        get("/userInfo",(req,res) -> accountService.getLoggedUserRole(req).toString());
    }

    private static void initializeContext() {
        accountService = new AccountService();
        AccountController.initializeService(accountService);
    }
}
