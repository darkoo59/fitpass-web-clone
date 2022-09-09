package service;

import com.google.gson.*;
import dao.SportsFacilityDAO;
import dao.TrainingDAO;
import dao.TrainingHistoryDAO;
import model.SportsFacility;
import model.Training;
import model.TrainingHistory;
import spark.Request;
import spire.math.prime.SieveUtil;
import utils.enums.RoleType;
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

public class TrainingService {
    private TrainingDAO trainingDAO;
    private SportsFacilityDAO facilityDAO;
    private TrainingHistoryDAO trainingHistoryDAO;

    public TrainingService()
    {
        this.trainingDAO = new TrainingDAO();
        this.facilityDAO = new SportsFacilityDAO();
        this.trainingHistoryDAO = new TrainingHistoryDAO();
    }

    public ArrayList<TrainingHistory> getMyTrainingsHistory(Request request) throws ParseException, IOException {
        System.out.println(RequestsUtils.getPayload(request));
        String payload = RequestsUtils.getPayload(request);
        String userId = RequestsUtils.getIdFromPayload(payload);
        RoleType userRole = RequestsUtils.getRoleFromPayload(payload);
        ArrayList<TrainingHistory> allTrainings = new ArrayList<>();
        for(TrainingHistory training : trainingHistoryDAO.getAll())
        {
            if(userRole == RoleType.COACH) {
                if (training.getCoachId().equals(userId))
                    allTrainings.add(training);
            }else if(userRole == RoleType.CUSTOMER) {
                if(training.getCustomerId().equals(userId))
                    allTrainings.add(training);
            }else if(userRole == RoleType.MANAGER) {
                System.out.println(training.getTrainingId());
                if(facilityDAO.get(trainingDAO.get(training.getTrainingId()).getFacilityId()).getManagerId().equals(userId))
                    allTrainings.add(training);
            }
        }
        return allTrainings;
    }

    public ArrayList<TrainingHistory> filter(Request req,Filter filter) throws Exception {
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
    public GsonBuilder getGsonBuilder()
    {
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
        return gsonBuilder;
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
                Collections.sort(filtered, comparing(TrainingHistory::getTrainingId, (t1, t2) -> {
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

    private ArrayList<TrainingHistory> managerSort(Filter filter, ArrayList<TrainingHistory> filtered) throws IOException {
        switch (filter.sort) {
            case "1":
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
            case "2":
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
            case "3":
                Collections.sort(filtered, Comparator.comparing(TrainingHistory::getApplicationDateTime));
                break;
            case "4":
                Collections.sort(filtered, Comparator.comparing(TrainingHistory::getApplicationDateTime,Comparator.reverseOrder()));
                break;
            default:
                return filtered;
        }
        return filtered;
    }

    public ArrayList<TrainingHistory> getCustomerTrainingHistoryFromFacility(String customerId, String facilityId) throws Exception {
        ArrayList<TrainingHistory> trainingHistoryFromAllFacilities = new ArrayList<>();
        for (TrainingHistory training : trainingHistoryDAO.getAll()) {
            if (training.getCustomerId().equals(customerId)) {
                trainingHistoryFromAllFacilities.add(training);
            }
        }
        ArrayList<TrainingHistory> trainingHistory = new ArrayList<>();
        for (TrainingHistory tHistory : trainingHistoryFromAllFacilities) {
            for (Training training : trainingDAO.getAll()) {
                if (tHistory.getTrainingId().equals(training.getId()) && training.getFacilityId().equals(facilityId)) {
                    trainingHistory.add(tHistory);
                }
            }
        }
        return trainingHistory;
    }
}
