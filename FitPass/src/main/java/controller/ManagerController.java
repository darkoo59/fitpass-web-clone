package controller;

import service.ManagerService;

import static main.main.gson;
import static spark.Spark.*;

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

    public static void getManagerFacility() {
        get("/managerFacility", (req, res) -> {
            res.type("application/json");
            return gson.toJson(managerService.getManagerFacility(req));
        });
    }
    ///managerTrainings/filter

    public static void postTrainingsFilter() {
        post("/managerTrainings/filter", (req, res) -> {
            return gson.toJson(managerService.filter(req));
        });
    }

    public static void postAddNewTraining() {
        post("/addNewTraining", (req, res) -> {
            managerService.addNewTraining(req);
            return "SUCCESS";
        });
    }

    public static void putEditTrainingImage() {
        put("/managerTrainings/edit/:name/logo", (req, res) -> managerService.editTrainingImage(req));
    }

    public static void putEditTraining() {
        put("/managerTrainings/edit/:id", (req, res) -> {
            managerService.editTraining(req);
            return "SUCCESS";
        });
    }

    public static void getContent() {
        get("/managerContent", (req, res) -> gson.toJson(managerService.getContent(req)));
    }

    public static void getAllCoachesForFacility() {
        get("/facilityCoaches", (req, res) -> {
            res.type("application/json");
            return gson.toJson(managerService.getAllCoachesForFacility(req));
        });
    }

    public static void getAllCustomersForFacility() {
        get("/facilityCustomers", (req, res) -> {
            res.type("application/json");
            return gson.toJson(managerService.getAllCustomersForFacility(req));
        });
    }

    public static void putEditContentImage() {
        put("/managerContent/edit/:name/image", (req, res) -> managerService.editContentImage(req));
    }

    public static void putEditContent() {
        put("/managerContent/edit/:id", (req, res) -> {
            managerService.editContent(req);
            return "SUCCESS";
        });
    }
}
