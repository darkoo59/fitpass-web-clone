package controller;

import service.AdministratorService;
import spark.Request;

import static main.main.gson;
import static spark.Spark.get;
import static spark.Spark.post;

public class AdministratorController {

    private static AdministratorService administratorService;

    public static void initializeService(AdministratorService adminService) {
        administratorService = adminService;
    }
    public static void postRegister() {
        post("/administratorCreateProfile", (req, res) -> {
              return administratorService.register(req);
        });
    }

    public static void getAllProfiles() {
        get("/allProfiles", (req, res) -> {
            res.type("application/json");
            return gson.toJson(administratorService.getAllProfiles());
        });
    }

    public static void getManagersForNewFacility() {
        get("/managersForNewFacility", (req, res) -> {
            res.type("application/json");
            return gson.toJson(administratorService.getManagersForNewFacility());
        });
    }

    public static void getSearchedProfiles() {
        post("/sarchProfiles",(req,res) -> {
            res.type("application/json");
            return administratorService.getSearchedProfiles(req);
        });
    }

    public static void postCreateNewFacility()
    {
        post("/createFacility",(req,res) -> {
            administratorService.createNewFacility(req);
            return "SUCCESS";
        });
    }

    public static void postCreateNewFacilityLogo() {
        post("/createFacility/logo/:name", (req, res) -> {
            String name = req.params(":name");
            return administratorService.createNewFacilityLogo(req, name);
        });
    }
}
