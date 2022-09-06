package service;

import dao.*;
import model.SportsFacility;
import model.Training;
import model.TrainingHistory;
import model.User;
import spark.Request;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class ManagerService {
    private IDao userDAO;
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
}
