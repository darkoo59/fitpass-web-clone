package service;

import com.google.gson.*;
import dao.SportsFacilityDAO;
import dao.TrainingDAO;
import dao.TrainingHistoryDAO;
import dao.UserDAO;
import model.*;
import org.apache.commons.lang.time.DateUtils;
import spark.Request;
import utils.enums.RoleType;
import utils.others.Filter;
import utils.others.RequestsUtils;
import utils.others.WorkHour;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static java.util.Comparator.comparing;

public class TrainingService {
    private TrainingDAO trainingDAO;
    private SportsFacilityDAO facilityDAO;
    private TrainingHistoryDAO trainingHistoryDAO;
    private UserDAO userDAO;

    public TrainingService()
    {
        this.trainingDAO = new TrainingDAO();
        this.facilityDAO = new SportsFacilityDAO();
        this.trainingHistoryDAO = new TrainingHistoryDAO();
        this.userDAO = new UserDAO();
    }

    public void addTrainingHistory(Request req) throws ParseException, IOException {
        String payload = RequestsUtils.getPayload(req);
        String userId = RequestsUtils.getIdFromPayload(payload);
        String applicationDateTime = req.queryParams("applicationDateTime");
        String trainingId = req.queryParams("trainingId");
        System.out.println("Date : "+applicationDateTime + ",id : "+trainingId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(applicationDateTime, formatter);
        ArrayList<TrainingHistory> allTrainings = trainingHistoryDAO.getAll();
        for(TrainingHistory trainingHistory:allTrainings)
        {
            if(trainingHistory.getTrainingId().equals(trainingId))
            {
                LocalDate date = dateTime.toLocalDate();
                ArrayList<LocalDate> dates = trainingHistory.getTrainingDates();
                dates.add(date);
                trainingHistory.setTrainingDates(dates);
                trainingHistoryDAO.save(allTrainings);
                return;
            }
        }
        ArrayList<LocalDate> trainDates = new ArrayList<LocalDate>();
        trainDates.add(dateTime.toLocalDate());
        TrainingHistory trainingHistory = new TrainingHistory(dateTime,trainingId,userId,trainingDAO.get(trainingId).getCoachId(),
                trainDates,null);
        ArrayList<TrainingHistory> allTrain = trainingHistoryDAO.getAll();
        allTrain.add(trainingHistory);
        trainingHistoryDAO.save(allTrain);
        return;
    }

    public int getTodayTrainingsNum(String customerId) throws IOException {
        int todayTrainigs = 0;
        for(TrainingHistory training : trainingHistoryDAO.getAll())
        {
            if(training.getCustomerId().equals(customerId))
            {
                for (LocalDate date : training.getTrainingDates())
                {
                    if(DateUtils.isSameDay(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
                        ,Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())))
                        todayTrainigs++;
                }
            }
        }
        System.out.println(todayTrainigs);
        return todayTrainigs;
    }

    public int getUsedTermsInInterval(String customerId, LocalDate from, LocalDate to) throws IOException {
        int termsNum = 0;
        for(TrainingHistory training : trainingHistoryDAO.getAll())
        {
            if(training.getCustomerId().equals(customerId))
            {
                for (LocalDate date : training.getTrainingDates())
                {
                    if(date.isAfter(from) && date.isBefore(to))
                        termsNum++;
                }
            }
        }
        System.out.println("Terms useddd : "+termsNum);
        return termsNum;
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

    public ArrayList<TrainingHistory> managerFilter(Request req,Filter filter) throws Exception {
        ArrayList<TrainingHistory> trainingHistories = getMyTrainingsHistory(req);
        ArrayList<TrainingHistory> filtered = new ArrayList<>();
        for (TrainingHistory training : trainingHistories) {
            if (name(filter,training) && !filtered.contains(training)) {
                filtered.add(training);
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
        return managerSort(filter,filtered);
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

    public String addNewTraining(Request req) throws ParseException, IOException {
        String payload = RequestsUtils.getPayload(req);
        String managerId = RequestsUtils.getIdFromPayload(payload);
        System.out.println("Start : "+ req.queryParams("startTime") + ", end : "+ req.queryParams("endTime"));
        if(req.queryParams("startTime").isEmpty() || req.queryParams("endTime").isEmpty())
            return "ERROR_TIME";
        WorkHour hours = new WorkHour(LocalTime.parse(req.queryParams("startTime")),
                LocalTime.parse(req.queryParams("endTime")));
        Training training = new Training(req.queryParams("name"),req.queryParams("type"),getFacilityIdByManagerId(managerId),
                hours,req.queryParams("coachId"),req.queryParams("description"),req.queryParams("image"),Integer.parseInt(
                        req.queryParams("price")));
        training.setId(trainingDAO.getNewId());
        if(req.queryParams("coachId") == null)
            return "ERROR_COACH";
        ArrayList<Training> trainings = trainingDAO.getAll();
        trainings.add(training);
        trainingDAO.save(trainings);
        return "SUCCESS";
    }

    private String getFacilityIdByManagerId(String managerId) throws IOException {
        for(SportsFacility facility : facilityDAO.getAll())
        {
            if(facility.getManagerId().equals(managerId))
                return facility.getManagerId();
        }
        return "";
    }

    public void editTraining(String id, Training training) throws Exception {
        ArrayList<Training> trainings = trainingDAO.getAll();
        for (Training train : trainings) {
            if (train.getId().equals(id)) {
                train.setName(training.getName());
                train.setType(training.getType());
                train.setImage(training.getImage());
                train.setDescription(training.getDescription());
                train.setPrice(training.getPrice());
                train.setDuration(training.getDuration());
                train.setCoachId(training.getCoachId());
                train.setFacilityId(training.getFacilityId());
            }
        }
        trainingDAO.save(trainings);
    }

    public ArrayList<User> getAllCoachesForManager(String managerId) throws IOException {
        String facilityId = getFacilityIdByManagerId(managerId);
        ArrayList<String> coachesIds = new ArrayList<String>();
        for(Training training : trainingDAO.getAll())
        {
            if(training.getFacilityId().equals(facilityId))
                coachesIds.add(training.getCoachId());
        }
        ArrayList<User> coaches = new ArrayList<User>();
        for(String id : coachesIds)
        {
            coaches.add(userDAO.get(id));
        }
        return coaches;
    }
}
