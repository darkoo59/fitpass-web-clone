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

    public static void deleteComment() {
        delete("/comment/delete/:id", (req, res) -> {
            commentService.deleteComment(req);
            return "SUCCESS";
        });
    }

    public static void putAcceptComment() {
        put("/comment/accept/:id", (req, res) -> {
            commentService.acceptComment(req);
            return "SUCCESS";
        });
    }

    public static void putRejectComment() {
        put("/comment/reject/:id", (req, res) -> {
            commentService.rejectComment(req);
            return "SUCCESS";
        });
    }
}
