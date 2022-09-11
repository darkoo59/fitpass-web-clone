package service;

import com.google.gson.reflect.TypeToken;
import dao.ContentDAO;
import dao.SportsFacilityDAO;
import model.Content;
import model.Location;
import model.SportsFacility;
import spark.Request;
import utils.enums.SportsFacilityStatus;
import utils.others.Address;
import utils.others.RequestsUtils;
import utils.others.WorkHour;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.util.ArrayList;

import static main.main.gson;

public class ContentService {

    SportsFacilityDAO facilityDAO;
    ContentDAO contentDAO;
    public ContentService()
    {
        facilityDAO = new SportsFacilityDAO();
        contentDAO = new ContentDAO();
    }

    private String getFacilityIdByManagerId(String managerId) throws IOException {
        for(SportsFacility facility : facilityDAO.getAll())
        {
            if(facility.getManagerId().equals(managerId))
                return facility.getManagerId();
        }
        return "";
    }
    public String addNewContent(Request req) throws Exception {
        String payload = RequestsUtils.getPayload(req);
        String managerId = RequestsUtils.getIdFromPayload(payload);
        Content content = new Content(req.queryParams("name"),req.queryParams("type"),req.queryParams("image"),
                req.queryParams("description"),req.queryParams("duration"),getFacilityIdByManagerId(managerId));
        content.setId(contentDAO.getNewId());
        ArrayList<Content> contents = contentDAO.getAll();
        for(Content contentt : contents)
        {
            if(contentt.getName().equals(req.queryParams("name")))
                return "NAME_EXISTS";
        }
        contents.add(content);
        contentDAO.save(contents);
        return "SUCCESS";
    }

    public String createNewContentImage(Request req, String name) throws Exception {
        String location = "";
        long maxFileSize = 100000000;
        long maxRequestSize = 100000000;
        int fileSizeThreshold = 1024;

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                location, maxFileSize, maxRequestSize, fileSizeThreshold);
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                multipartConfigElement);

        Part photo = req.raw().getPart("photo");
        String photoName = photo.getSubmittedFileName();
        String ext = photoName.substring(photoName.lastIndexOf(".") + 1);

        Path out = Paths.get("src/main/resources/static/vue/src/assets/images/" + name + "." + ext);
        try (final InputStream in = photo.getInputStream()) {
            Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
            photo.delete();
        }
        multipartConfigElement = null;
        photo = null;
        return name + "." + ext;
    }
}
