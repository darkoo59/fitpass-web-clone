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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
        }).create();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
            }
        }).create();
        Gson gsonn = gsonBuilder.setPrettyPrinting().create();
        Filter filter = gsonn.fromJson(req.body(), Filter.class);
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
            if(!price(filter,training)) {
                if(filtered.contains(training)) {
                    filtered.remove(training);
                }
            }
            if(!applicationDate(filter,training)) {
                if(filtered.contains(training)) {
                    filtered.remove(training);
                }
            }
        }
        return sort(filter,filtered);
    }

    private Boolean name(Filter filter, TrainingHistory training) throws IOException {
        return facilityDAO.get(trainingDAO.get(training.getTrainingId()).getFacilityId()).getName().toLowerCase().contains(filter.
                searchInput.toLowerCase());
    }

    private Boolean price(Filter filter,TrainingHistory training) throws IOException {
        int price = trainingDAO.get(training.getTrainingId()).getPrice();
        switch (filter.price) {
            case "1":
                if (price == 0)
                    return true;
                break;
            case "2":
                if (0 <= price && price <= 1000)
                    return true;
                break;
            case "3":
                if (1000 <= price && price <= 3000)
                    return true;
                break;
            case "4":
                if (3000 <= price && price <= 5000)
                    return true;
                break;
            case "5":
                if (price >= 5000)
                    return true;
                break;
            default:
                return true;
        }
        return false;
    }

    private Boolean applicationDate(Filter filter,TrainingHistory training) throws IOException {
        if(filter.fromApplicationDate == null || filter.toApplicationDate == null)
            return true;
        LocalDateTime applicationDate = training.getApplicationDateTime();
        if(applicationDate.toLocalDate().isAfter(filter.fromApplicationDate) && applicationDate.toLocalDate().isBefore(filter.toApplicationDate)) {
            return true;
        }
        return false;
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

    private ArrayList<TrainingHistory> sort(Filter filter, ArrayList<TrainingHistory> filtered) throws IOException {
        switch (filter.sort) {
            case "1":
                Collections.sort(filtered, comparing(TrainingHistory::getTrainingId, (t1,t2) -> {
                    try {
                        Training training1 = trainingDAO.get(t1);
                        Training training2 = trainingDAO.get(t2);
                        SportsFacility sportsFacility1 = facilityDAO.get(training1.getFacilityId());
                        SportsFacility sportsFacility2 = facilityDAO.get(training2.getFacilityId());
                        return sportsFacility1.getName().compareTo(sportsFacility2.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
                break;
            case "2":
                Collections.sort(filtered, comparing(TrainingHistory::getTrainingId, (t1,t2) -> {
                    try {
                        Training training1 = trainingDAO.get(t1);
                        Training training2 = trainingDAO.get(t2);
                        SportsFacility sportsFacility1 = facilityDAO.get(training1.getFacilityId());
                        SportsFacility sportsFacility2 = facilityDAO.get(training2.getFacilityId());
                        return sportsFacility1.getName().compareTo(sportsFacility2.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).reversed());
                break;
            case "3":
                Collections.sort(filtered, comparing(TrainingHistory::getTrainingId, (t1,t2) -> {
                    try {
                        Training training1 = trainingDAO.get(t1);
                        Training training2 = trainingDAO.get(t2);
                        return training1.getPrice() - training2.getPrice();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
                break;
            case "4":
                Collections.sort(filtered, comparing(TrainingHistory::getTrainingId, (t1,t2) -> {
                    try {
                        Training training1 = trainingDAO.get(t1);
                        Training training2 = trainingDAO.get(t2);
                        return training2.getPrice() - training1.getPrice();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
                break;
            case "5":
                Collections.sort(filtered, Comparator.comparing(TrainingHistory::getApplicationDateTime));
                break;
            case "6":
                Collections.sort(filtered, Comparator.comparing(TrainingHistory::getApplicationDateTime,Comparator.reverseOrder()));
                break;
            default:
                return filtered;
        }
        return filtered;
    }
}
