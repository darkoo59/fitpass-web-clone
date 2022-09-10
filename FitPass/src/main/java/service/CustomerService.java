package service;

import com.google.gson.Gson;
import dao.*;
import model.*;
import spark.Request;
import utils.others.Filter;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomerService {

    private IDAO userDAO;
    private TrainingDAO trainingDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    private SportsFacilityDAO facilityDAO;

    private ArrayList<User> allUsers;
    private TrainingService trainingService;
    private CustomerDAO customerDAO;

    public CustomerService() throws IOException {
        userDAO = new UserDAO();
        trainingDAO = new TrainingDAO();
        trainingHistoryDAO = new TrainingHistoryDAO();
        facilityDAO = new SportsFacilityDAO();
        trainingService = new TrainingService();
        allUsers = userDAO.getAll();
        customerDAO = new CustomerDAO();
    }

    public ArrayList<Customer> getAllCustomers() throws IOException {
        return customerDAO.getAll();
    }

    public ArrayList<Training> getAllTrainings(Request request) throws ParseException, IOException {
        return trainingDAO.getAll();
    }

    public SportsFacility getFacilityByTrainingId(Request request) throws IOException {
        for(SportsFacility facility : facilityDAO.getAll())
        {
            if(facility.getId().equals(request.queryParams("facilityId"))) {
                return facility;
            }
        }
        return null;
    }

    public ArrayList<TrainingHistory> getMyTrainingsHistory(Request request) throws ParseException, IOException {
        String payload = RequestsUtils.getPayload(request);
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        String customerId = "";
        ArrayList<TrainingHistory> allTrainings = new ArrayList<TrainingHistory>();
        for(User user :allUsers)
        {
            if(user.getUsername().equals(username))
            {
                customerId = user.getId();
                break;
            }
        }
        for(TrainingHistory training : trainingHistoryDAO.getAll())
        {
            if(training.getCustomerId().equals(customerId))
                allTrainings.add(training);
        }
        return allTrainings;
    }

    public ArrayList<TrainingHistory> filter(Request req) throws Exception {
        Gson gson = trainingService.getGsonBuilder().setPrettyPrinting().create();
        Filter filter = gson.fromJson(req.body(), Filter.class);
        return trainingService.filter(req,filter);
    }

    public void addTrainingHistory(Request req) throws ParseException, IOException {
        trainingService.addTrainingHistory(req);
        String payload = RequestsUtils.getPayload(req);
        String username = payload.substring(8,payload.lastIndexOf("\",\"exp\":"));
        String applicationDateTime = req.queryParams("applicationDateTime");
        String trainingId = req.queryParams("trainingId");
        System.out.println("Date : "+applicationDateTime + ",id : "+trainingId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(applicationDateTime, formatter);

    }

    public Customer getCustomer(Request req) throws IOException, ParseException {
        String payload = RequestsUtils.getPayload(req);
        String id = RequestsUtils.getIdFromPayload(payload);
        return customerDAO.get(id);
    }
}
