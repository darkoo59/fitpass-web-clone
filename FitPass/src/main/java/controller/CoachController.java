package controller;

import service.CoachService;

import static main.main.gson;
import static spark.Spark.get;
import static spark.Spark.post;

public class CoachController {
    private static CoachService coachService;

    public static void initializeService(CoachService service) {
        coachService = service;
    }

    public static void getMyTrainingsHistory() {
        get("/coachTrainingsHistory", (req, res) -> {
            res.type("application/json");

            return gson.toJson(coachService.getMyTrainingsHistory(req));
        });
    }

    public static void getAllTrainings() {
        get("/coachTrainingsInfo", (req, res) -> {
            res.type("application/json");
            return gson.toJson(coachService.getAllTrainings(req));
        });
    }

    public static void getCancelPersonalTraining() {
        get("/cancelPersonalTraining", (req,res) -> {
            coachService.cancelPersonalTraining(req);
            return "SUCCESS";
        });
    }

    public static void postTrainingsFilter() {
        post("/coachTrainings/filter", (req, res) -> {
            return gson.toJson(coachService.filter(req));
        });
    }
}
