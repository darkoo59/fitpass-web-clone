package controller;

import service.MembershipService;
import service.SportsFacilityService;

import static main.main.gson;
import static spark.Spark.get;
import static spark.Spark.post;

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

    public static void getTodayTermsNum() {
        get("/getTermsNum", (req, res) -> {
            return membershipService.getTodayTermsNum(req);
        });
    }

    public static void postCreateMembership() {
        post("createMembership", (req, res) -> {
            membershipService.postCreateMembership(req);
            return "SUCCESS";
        });
    }

    public static void getExistingMembershipById() {
        get("membershipById", (req,res) -> {
            res.type("application/json");
            return gson.toJson(membershipService.getExistingMembershipById(req));
        });
    }

    //membershipById
}
