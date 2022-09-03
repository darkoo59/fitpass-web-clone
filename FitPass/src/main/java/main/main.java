package main;

import com.google.gson.Gson;
import controller.AccountController;
import controller.CORSController;
import controller.SportsFacilityController;
import service.AccountService;
import service.SportsFacilityService;
import utils.others.CustomGsonBuilder;
import static spark.Spark.*;

public class main {

    public static Gson gson;
    private static CustomGsonBuilder customGsonBuilder;
    private static AccountService accountService;
    private static SportsFacilityService facilitiesService;

    public static void main(String[] args) throws Exception {

        initializeContext();
        gson = customGsonBuilder.getCustomGsonBuilder().create();
        staticFiles.location("/static/vue/dist");
        port(8081);

        CORSController.enableCORS();

        get("/home" , (req,res) -> "index.html");

        AccountController.postRegister();
        AccountController.postLogin();
        AccountController.getUser();
        AccountController.getUserRole();
        AccountController.getUserInfo();
        AccountController.postUserInfoEdit();

        SportsFacilityController.getSportsFacilites();
    }

    private static void initializeContext() {
        accountService = new AccountService();
        AccountController.initializeService(accountService);
        facilitiesService = new SportsFacilityService();
        SportsFacilityController.initializeService(facilitiesService);
        customGsonBuilder = new CustomGsonBuilder();
    }
}
