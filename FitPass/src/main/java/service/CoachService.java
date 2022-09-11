package service;

import com.google.gson.*;
import dao.*;
import model.Coach;
import model.Training;
import model.TrainingHistory;
import model.User;
import spark.Request;
import utils.enums.RoleType;
import utils.others.Filter;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.util.Comparator.comparing;

public class CoachService {

    private IDAO userDAO;
    private TrainingDAO trainingDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    private SportsFacilityDAO facilityDAO;

    private ArrayList<User> allUsers;
    private TrainingService trainingService;

    public CoachService() throws IOException {
        userDAO = new UserDAO();
        trainingDAO = new TrainingDAO();
        trainingHistoryDAO = new TrainingHistoryDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
        trainingService = new TrainingService();
    }

    public ArrayList<Training> getAllTrainings(Request request) throws ParseException, IOException {
        ArrayList<Training> allTrainings =  trainingDAO.getAll();
        String coachId = RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(request));
        ArrayList<Training> coachTrainings = new ArrayList<>();
        for (Training training:allTrainings) {
            if(training.getCoachId().equals(coachId))
                coachTrainings.add(training);
        }
        return coachTrainings;
    }
    public ArrayList<TrainingHistory> getMyTrainingsHistory(Request request) throws ParseException, IOException {
        return trainingService.getMyTrainingsHistory(request);
    }

    public void cancelPersonalTraining(Request request) throws IOException {
        TrainingHistory trainingToCancel = trainingHistoryDAO.get(request.queryParams("id"));
        trainingHistoryDAO.delete(trainingToCancel);
        return;
    }

    public ArrayList<TrainingHistory> filter(Request req) throws Exception {
        Gson gson = trainingService.getGsonBuilder().setPrettyPrinting().create();
        Filter filter = gson.fromJson(req.body(), Filter.class);
        return trainingService.filter(req,filter);
    }

    public ArrayList<User> getAllCoaches() throws IOException {
        ArrayList<User> allCoaches = new ArrayList<User>();
        ArrayList<User> allUsers =  userDAO.getAll();
        for(User user:allUsers)
        {
            if(user.getRole() == RoleType.COACH)
                allCoaches.add(user);
        }
        return allCoaches;
    }
}
