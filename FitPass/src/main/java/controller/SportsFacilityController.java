package controller;

import service.SportsFacilityService;

import static main.main.gson;
import static spark.Spark.*;

public class SportsFacilityController {
    private static SportsFacilityService sportsFacilityService;

    public static void initializeService(SportsFacilityService sfService) {
        sportsFacilityService = sfService;
    }

    public static void getSportsFacilites() {
        get("/sportsFacilities", (req, res) -> {
            res.type("application/json");
            return gson.toJson(sportsFacilityService.getAll());
        });
    }
}