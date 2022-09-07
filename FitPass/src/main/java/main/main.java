package main;

import com.google.gson.Gson;
import controller.*;
import service.*;
import utils.others.CustomGsonBuilder;

import java.io.IOException;

import static spark.Spark.*;

public class main {

    public static Gson gson;
    private static CustomGsonBuilder customGsonBuilder;
    private static AccountService accountService;
    private static SportsFacilityService facilitiesService;

    private static CustomerService customerService;

    private static AdministratorService administratorService;

    private static CoachService coachService;

    private static ManagerService managerService;
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
        AdministratorController.postCreateNewFacility();
        AdministratorController.postCreateNewFacilityLogo();

        SportsFacilityController.getSportsFacilites();
        SportsFacilityController.getSportFacility();
        SportsFacilityController.postSportsFacilitesFilter();

        CustomerController.getAllTrainings();
        CustomerController.getMyTrainingsHistory();
        CustomerController.getFacilityByTrainingId();
        CustomerController.postTrainingsFilter();

        CoachController.getMyTrainingsHistory();
        CoachController.getAllTrainings();
        CoachController.getCancelPersonalTraining();
        CoachController.postTrainingsFilter();

        ManagerController.getMyTrainingsHistory();
        ManagerController.getAllTrainings();
        ManagerController.getManagerFacilities();
        ManagerController.postTrainingsFilter();
    }

    private static void initializeContext() throws IOException {
        accountService = new AccountService();
        AccountController.initializeService(accountService);
        facilitiesService = new SportsFacilityService();
        SportsFacilityController.initializeService(facilitiesService);
        customGsonBuilder = new CustomGsonBuilder();
        administratorService = new AdministratorService();
        AdministratorController.initializeService(administratorService);
        customerService = new CustomerService();
        CustomerController.initializeService(customerService);
        coachService = new CoachService();
        CoachController.initializeService(coachService);
        managerService = new ManagerService();
        ManagerController.initializeService(managerService);

    }
}
