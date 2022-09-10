package controller;

import service.PromoCodesService;
import service.SportsFacilityService;

import static main.main.gson;
import static spark.Spark.get;
import static spark.Spark.post;

public class PromoCodesController {
    private static PromoCodesService promoCodesService;

    public static void initializeService(PromoCodesService service) {
        promoCodesService = service;
    }

    public static void getPromoCodes() {
        get("/promoCodes", (req, res) -> {
            res.type("application/json");
            return gson.toJson(promoCodesService.getPromoCodes());
        });
    }

    public static void getDecrementSelectedPromoCode() {
        get("decrementPromoCode", (req,res) -> {
            promoCodesService.getDecrementSelectedPromoCode(req);
            return "SUCCESS";
        });
    }

    public static void postAddPromoCode() {
        post("/addPromoCode","application/json", (req,res) -> {
            return promoCodesService.addPromoCode(req);
        });
    }

}
