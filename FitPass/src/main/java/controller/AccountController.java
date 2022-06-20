package controller;

import service.AccountService;
import spark.Request;
import spark.Response;

public class AccountController {
    private static  AccountService accountService;

    public static void initializeService(AccountService accService) {
        accountService = accService;
    }

    public static String getUser(Request req, Response res) {
        res.redirect("http://localhost:8080/");
        return "BORA KONJ";
    }
}
