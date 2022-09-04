package controller;

import org.glassfish.jersey.internal.inject.Custom;
import service.AdministratorService;
import service.CustomerService;

import static main.main.gson;
import static spark.Spark.get;
import static spark.Spark.post;

public class CustomerController {

    private static CustomerService customerService;

    public static void initializeService(CustomerService custService) {
        customerService = custService;
    }

    public static void getMyTrainingsHistory() {
        get("/myTrainings", (req, res) -> {
            res.type("application/json");

            return gson.toJson(customerService.getMyTrainingsHistory(req));
        });
    }

    public static void getAllTrainings() {
        get("/myTrainingsInfo", (req, res) -> {
            res.type("application/json");
            return gson.toJson(customerService.getAllTrainings(req));
        });
    }

    public static void getFacilityByTrainingId()
    {
        get("/facilityByTrainingId", (req, res) -> {
            res.type("application/json");
            return gson.toJson(customerService.getFacilityByTrainingId(req));
        });
    }
}
