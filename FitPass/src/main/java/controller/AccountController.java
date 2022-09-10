package controller;

import service.AccountService;
import static main.main.gson;

import static spark.Spark.*;

public class AccountController {
    private static AccountService accountService;

    public static void initializeService(AccountService accService) {
        accountService = accService;
    }

    public static void postRegister() {
        post("/register", (req, res) -> {
            accountService.register(req);
            return "SUCCESS";
        });
    }

    public static void postLogin() {
        post("/login","application/json", (req,res) -> accountService.loginUser(req).toString());
    }

    public static void getUserRole() {
        get("/userRole", (req, res) -> accountService.getLoggedUserRole(req).toString());
    }

    public static void getUserInfo() {
        get("/userInfo", (req, res) -> gson.toJson(accountService.getUserInfo(req)));
    }

    public static void getUsername() {
        get("/user/:id", (req, res) -> gson.toJson(accountService.getUsername(req)));
    }

    public static void postUserInfoEdit() {
        post("/userInfo/edit", (req, res) -> {
            accountService.editUser(req);
            return "SUCCESS";
        });
    }

    public static void getUserId() {
        get("/userId", (req, res) -> accountService.getUserId(req));
    }
    
    public static void getFilteredProfiles() {
        post("/searchProfiles", (req, res) -> {
            return gson.toJson(accountService.filter(req));
        });
    }
}
