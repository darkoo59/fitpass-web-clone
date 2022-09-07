package controller;

import service.CoachService;
import service.ManagerService;

import static main.main.gson;
import static spark.Spark.get;
import static spark.Spark.post;

public class ManagerController {

    private static ManagerService managerService;

    public static void initializeService(ManagerService service) {managerService=service;}

    public static void getMyTrainingsHistory() {
        get("/managerTrainingsHistory", (req, res) -> {
            res.type("application/json");

            return gson.toJson(managerService.getMyTrainingsHistory(req));
        });
    }

    public static void getAllTrainings() {
        get("/managerTrainingsInfo", (req, res) -> {
            res.type("application/json");
            return gson.toJson(managerService.getAllTrainings(req));
        });
    }

    public static void getManagerFacilities() {
        get("/managerFacilities", (req, res) -> {
            res.type("application/json");
            return gson.toJson(managerService.getManagerFacilities(req));
        });
    }
    ///managerTrainings/filter

    public static void postTrainingsFilter() {
        post("/managerTrainings/filter", (req, res) -> {
            return gson.toJson(managerService.filter(req));
        });
    }
}
