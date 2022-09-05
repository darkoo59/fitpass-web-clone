package service;

import com.nimbusds.jwt.SignedJWT;
import dao.*;
import model.SportsFacility;
import model.Training;
import model.TrainingHistory;
import model.User;
import spark.Request;
import utils.others.Filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import static main.main.gson;

public class CoachService {

    private IDao userDAO;
    private TrainingDAO trainingDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    private SportsFacilityDAO facilityDAO;

    private ArrayList<User> allUsers;

    public CoachService() throws IOException {
        userDAO = new UserDAO();
        trainingDAO = new TrainingDAO();
        trainingHistoryDAO = new TrainingHistoryDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
    }

    public ArrayList<Training> getAllTrainings(Request request) throws ParseException, IOException {
        ArrayList<Training> allTrainings =  trainingDAO.getAll();
        String coachId = getIdFromPayload(getPayload(request));
        ArrayList<Training> coachTrainings = new ArrayList<>();
        for (Training training:allTrainings) {
            if(training.getCoachId().equals(training.getCoachId()))
                coachTrainings.add(training);
        }
        return coachTrainings;
    }

    public void cancelPersonalTraining(Request request) throws IOException {
        System.out.println("test : " + request.queryParams("id"));
        TrainingHistory trainingToCancel = trainingHistoryDAO.get(request.queryParams("id"));
        trainingHistoryDAO.delete(trainingToCancel);
        return;
    }

    public ArrayList<TrainingHistory> getMyTrainingsHistory(Request request) throws ParseException, IOException {
        String coachId = getIdFromPayload(getPayload(request));
        ArrayList<TrainingHistory> allTrainings = new ArrayList<>();
        for(TrainingHistory training : trainingHistoryDAO.getAll())
        {
            if(training.getCoachId().equals(coachId))
                allTrainings.add(training);
        }
        return allTrainings;
    }

    private String getPayload(Request request) throws ParseException {
        String[] splittedHeader = request.headers("Authorization").split("\\s+");
        String jwtToken = splittedHeader[1];
        SignedJWT decodedJWT = SignedJWT.parse(jwtToken);
        String payload = decodedJWT.getPayload().toString();
        return payload;
    }

    private String getIdFromPayload(String payload) {
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
        return coachId;
    }

    public ArrayList<TrainingHistory> filter(Request req) throws Exception {
        Filter filter = gson.fromJson(req.body(), Filter.class);
        ArrayList<TrainingHistory> trainingHistories = getMyTrainingsHistory(req);
        ArrayList<TrainingHistory> filtered = new ArrayList<>();
        for (TrainingHistory training : trainingHistories) {
            if (name(filter,training) && !filtered.contains(training)) {
                    filtered.add(training);
            }
            if (!facilityType(filter, training)) {
                if (filtered.contains(training)) {
                    filtered.remove(training);
                }
            }
            if (!trainingType(filter, training)) {
                if (filtered.contains(training)) {
                    filtered.remove(training);
                }
            }
        }
        return filtered;
    }

    private Boolean name(Filter filter, TrainingHistory training) throws IOException {
        return trainingDAO.get(training.getTrainingId()).getName().toLowerCase().contains(filter.searchInput.toLowerCase());
    }

    private Boolean facilityType(Filter filter, TrainingHistory training) throws IOException {
        if (filter.facilityType.equals("Facility type"))
            return true;
        SportsFacility facility = facilityDAO.get((trainingDAO.get(training.getTrainingId()).getFacilityId()));
        return facility.getType().equals(filter.facilityType);
    }

    private Boolean trainingType(Filter filter, TrainingHistory training) throws IOException {
        if (filter.trainingType.equals("Training type"))
            return true;
        Training trainingToCheck = trainingDAO.get(training.getTrainingId());
        return trainingToCheck.getType().equals(filter.trainingType);
    }
}
