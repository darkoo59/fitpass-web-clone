package service;

import com.nimbusds.jwt.SignedJWT;
import dao.*;
import model.SportsFacility;
import model.Training;
import model.TrainingHistory;
import model.User;
import spark.Request;
import utils.enums.RoleType;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class CustomerService {

    private IDao userDAO;
    private TrainingDAO trainingDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    private SportsFacilityDAO facilityDAO;

    private ArrayList<User> allUsers;

    public CustomerService() throws IOException {
        userDAO = new UserDAO();
        trainingDAO = new TrainingDAO();
        trainingHistoryDAO = new TrainingHistoryDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
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
        String payload = getPayload(request);
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

    private String getPayload(Request request) throws ParseException {
        String[] splittedHeader = request.headers("Authorization").split("\\s+");
        String jwtToken = splittedHeader[1];
        SignedJWT decodedJWT = SignedJWT.parse(jwtToken);
        String payload = decodedJWT.getPayload().toString();
        return payload;
    }
}
