package controller;

import model.Content;
import service.AdministratorService;
import service.ContentService;

import static spark.Spark.post;

public class ContentController {

    private static ContentService contentService;

    public static void initializeService(ContentService service) {
        contentService = service;
    }

    public static void postCreateNewContentImage() {
        post("/addNewContent/logo/:name", (req, res) -> {
            String name = req.params(":name");
            return contentService.createNewContentImage(req, name);
        });
    }

    public static void postAddNewContent()
    {
        post("/addNewContent",(req,res) -> {
            return contentService.addNewContent(req);
        });
    }
}
