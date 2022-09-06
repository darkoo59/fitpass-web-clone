package service;

import com.google.gson.*;
import com.nimbusds.jwt.SignedJWT;
import dao.*;
import model.SportsFacility;
import model.Training;
import model.TrainingHistory;
import model.User;
import org.apache.log4j.lf5.viewer.FilteredLogTableModel;
import spark.Request;
import utils.others.Filter;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.util.Comparator.comparing;
import static main.main.gson;

public class CoachService {

    private IDao userDAO;
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
        String payload = RequestsUtils.getPayload(request);
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        String coachId = "";
        ArrayList<TrainingHistory> allTrainings = new ArrayList<TrainingHistory>();
        for(User user :allUsers)
        {
            if(user.getUsername().equals(username))
            {
                coachId = user.getId();
                break;
            }
        }
        for(TrainingHistory training : trainingHistoryDAO.getAll())
        {
            if(training.getCoachId().equals(coachId))
                allTrainings.add(training);
        }
        return allTrainings;
    }

    public void cancelPersonalTraining(Request request) throws IOException {
        System.out.println("test : " + request.queryParams("id"));
        TrainingHistory trainingToCancel = trainingHistoryDAO.get(request.queryParams("id"));
        trainingHistoryDAO.delete(trainingToCancel);
        return;
    }

    public ArrayList<TrainingHistory> filter(Request req) throws Exception {
        Gson gson = trainingService.getGsonBuilder().setPrettyPrinting().create();
        Filter filter = gson.fromJson(req.body(), Filter.class);
        return trainingService.filter(req,filter);
    }
}
