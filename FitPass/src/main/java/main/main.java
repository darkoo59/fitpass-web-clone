package main;

import com.google.gson.Gson;
import controller.AccountController;
import controller.AdministratorController;
import controller.CORSController;
import controller.SportsFacilityController;
import service.AccountService;
import service.AdministratorService;
import service.SportsFacilityService;
import utils.others.CustomGsonBuilder;

import java.io.IOException;

import static spark.Spark.*;

public class main {

    public static Gson gson;
    private static CustomGsonBuilder customGsonBuilder;
    private static AccountService accountService;
    private static SportsFacilityService facilitiesService;

    private static AdministratorService administratorService;
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

        AdministratorController.postRegister();
        AdministratorController.getAllProfiles();
        AdministratorController.getManagersForNewFacility();
        AdministratorController.getSearchedProfiles();

        SportsFacilityController.getSportsFacilites();
    }

    private static void initializeContext() throws IOException {
        accountService = new AccountService();
        AccountController.initializeService(accountService);
        facilitiesService = new SportsFacilityService();
        SportsFacilityController.initializeService(facilitiesService);
        customGsonBuilder = new CustomGsonBuilder();
        administratorService = new AdministratorService();
        AdministratorController.initializeService(administratorService);

    }
}
