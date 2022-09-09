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
            return gson.toJson(sportsFacilityService.getAllSorted());
        });
    }

    public static void getSportFacility() {
        get("/facility", (req,res) -> {
            res.type("application/json");
            return gson.toJson(sportsFacilityService.getSportFacilityById(req));
        });
    }

    public static void postSportsFacilitesFilter() {
        post("sportsFacilities/filter", (req, res) ->
                gson.toJson(sportsFacilityService.filter(req)));
    }

    public static void postSportFacilityComments() {
        post("/facility/:id/comments", (req,res) ->
                gson.toJson(sportsFacilityService.getSportFacilityComments(req)));
    }

    public static void postSportFacilityAddComment() {
        post("/facility/:id/comments/add", (req,res) -> {
            sportsFacilityService.sportFacilityAddComment(req);
            return "SUCCESS";
        });
    }

    public static void getSportFacilityAddCommentAllowed() {
        get("/facility/:id/comments/add/allowed", (req, res) ->
                gson.toJson(sportsFacilityService.getSportFacilityAddCommentAllowed(req)));
    }
}
