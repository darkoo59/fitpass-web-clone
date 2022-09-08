package controller;

import service.CommentService;

import static main.main.gson;
import static spark.Spark.*;

public class CommentController {
    private static CommentService commentService;

    public static void initializeService(CommentService commService) { commentService = commService; }

    public static void getAllComments() {
        get("/comments", (req, res) -> gson.toJson(commentService.getAllComments()));
    }
}
