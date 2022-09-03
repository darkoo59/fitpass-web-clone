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
            res.redirect("http://localhost:8081");
            return "SUCCESS";
        });
    }

    public static void postLogin() {
        post("/login","application/json", (req,res) -> {
            return accountService.loginUser(req);
        });
    }

    public static void getUser() {
        get("/user", (req, res) -> {
            res.redirect("http://localhost:8080/");
            return "SUCCESS";
        });
    }

    public static void getUserRole() {
        get("/userRole", (req, res) -> accountService.getLoggedUserRole(req).toString());
    }

    public static void getUserInfo() {
        get("/userInfo", (req, res) -> gson.toJson(accountService.getUserInfo(req)));
    }

    public static void postUserInfoEdit() {
        post("/userInfo/edit", (req, res) -> {
            accountService.editUser(req);
            return "SUCCESS";
        });
    }
}
