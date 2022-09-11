package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.*;
import model.*;
import spark.Request;
import utils.others.Filter;
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
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;

import static main.main.gson;

public class ManagerService {
    private IDAO userDAO;
    private TrainingDAO trainingDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    private SportsFacilityDAO facilityDAO;

    private ArrayList<User> allUsers;
    private TrainingService trainingService;

    public ManagerService() throws IOException {
        userDAO = new UserDAO();
        trainingDAO = new TrainingDAO();
        trainingHistoryDAO = new TrainingHistoryDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
        trainingService = new TrainingService();
    }

    public ArrayList<TrainingHistory> getMyTrainingsHistory(Request request) throws ParseException, IOException {
        return trainingService.getMyTrainingsHistory(request);
    }

    public ArrayList<Training> getAllTrainings(Request request) throws ParseException, IOException {
        ArrayList<Training> allTrainings =  trainingDAO.getAll();
        String managerId = RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(request));
        ArrayList<Training> managerTrainings = new ArrayList<>();
        for (Training training:allTrainings) {
            if(facilityDAO.get(training.getFacilityId()).getManagerId().equals(managerId))
                managerTrainings.add(training);
        }
        return managerTrainings;
    }

    public ArrayList<SportsFacility> getManagerFacilities(Request request) throws ParseException, IOException {
        String managerId = RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(request));
        return facilityDAO.getFacilitiesByManagerId(managerId);
    }

    public ArrayList<TrainingHistory> filter(Request req) throws Exception {
        Gson gson = trainingService.getGsonBuilder().setPrettyPrinting().create();
        Filter filter = gson.fromJson(req.body(), Filter.class);
        return trainingService.managerFilter(req,filter);
    }

    public void addNewTraining(Request req) throws ParseException, IOException {
        trainingService.addNewTraining(req);
    }

    public String editTrainingImage(Request req) throws Exception {
        String name = req.params(":name");
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

    public void editTraining(Request req) throws Exception {
        String facId = req.params(":id");
        ArrayList<String> payload = gson.fromJson(req.body(), new TypeToken<ArrayList<String>>(){}.getType());
        String id = payload.get(0);
        String name = payload.get(1);
        String type = payload.get(2);
        String image = payload.get(3);
        String description = payload.get(4);
        int price = Integer.parseInt(payload.get(5));
        LocalTime durationStart = LocalTime.parse(payload.get(6));
        LocalTime durationEnd= LocalTime.parse(payload.get(7));
        WorkHour workHour = new WorkHour(durationStart, durationEnd);
        String coachId = payload.get(8);
        Training training = new Training(name, type, facId, workHour, coachId, description, image, price);
        trainingService.editTraining(id, training);
    }

    public ArrayList<Content> getContent(Request req) throws Exception {
        String id = RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(req));
        SportsFacilityService sportsFacilityService = new SportsFacilityService();
        SportsFacility sportsFacility = sportsFacilityService.getSportsFacilityByManagerId(id);
        ContentDAO contentDAO = new ContentDAO();
        ArrayList<Content> content = new ArrayList<>();
        for (Content con : contentDAO.getAll()) {
            if (sportsFacility.getId().equals(con.getId())) {
                content.add(con);
            }
        }
        return content;
    }

}
