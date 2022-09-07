package controller;

import service.MembershipService;
import service.SportsFacilityService;

import static main.main.gson;
import static spark.Spark.get;

public class MembershipController {
    private static MembershipService membershipService;

    public static void initializeService(MembershipService service) {
        membershipService = service;
    }

    public static void getExistingMemberships() {
        get("/existingMemberships", (req, res) -> {
            res.type("application/json");
            return gson.toJson(membershipService.getExistingMemberships());
        });
    }

    public static void getActiveMembership() {
        get("/activeMembership", (req, res) -> {
            res.type("application/json");
            return gson.toJson(membershipService.getActiveMembership(req));
        });
    }

    //getExistingMembership
}
