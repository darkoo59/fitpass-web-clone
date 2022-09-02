package controller;

import service.AdministratorService;

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
              administratorService.register(req);
              res.redirect("http://localhost:8080/");
              return "SUCCESS";
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
}
