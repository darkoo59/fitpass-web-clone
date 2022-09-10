package service;

import com.google.gson.Gson;
import dao.*;
import model.*;
import spark.Request;
import utils.others.Filter;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class ManagerService {
    private IDAO userDAO;
    private TrainingDAO trainingDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    private SportsFacilityDAO facilityDAO;

    private ArrayList<User> allUsers;
    private TrainingService trainingService;
    private CustomerService customerService;

    public ManagerService() throws IOException {
        userDAO = new UserDAO();
        trainingDAO = new TrainingDAO();
        trainingHistoryDAO = new TrainingHistoryDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
        trainingService = new TrainingService();
        customerService = new CustomerService();
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

    public SportsFacility getManagerFacility(Request request) throws ParseException, IOException {
        String managerId = RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(request));
        for(SportsFacility facility : facilityDAO.getAll())
        {
            if(facility.getManagerId().equals(managerId))
                return facility;
        }
        return null;
    }

    public ArrayList<TrainingHistory> filter(Request req) throws Exception {
        Gson gson = trainingService.getGsonBuilder().setPrettyPrinting().create();
        Filter filter = gson.fromJson(req.body(), Filter.class);
        return trainingService.managerFilter(req,filter);
    }

    public void addNewTraining(Request req) throws ParseException, IOException {
        trainingService.addNewTraining(req);
    }

    public ArrayList<User> getAllCoachesForFacility(Request req) throws IOException, ParseException {
        String managerId = RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(req));
        return trainingService.getAllCoachesForManager(managerId);
    }

    public ArrayList<Customer> getAllCustomersForFacility(Request req) throws ParseException, IOException {
        SportsFacility sportsFacility = getManagerFacility(req);
        String facilityId = sportsFacility.getId();
        return customerService.getAllCustomersWhoVisited(facilityId);
    }
}
