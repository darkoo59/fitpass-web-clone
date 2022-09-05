package model;

import utils.others.WorkHour;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

public class TrainingHistory {

    private String id;
    private LocalDateTime applicationDateTime;
    private String trainingId;
    private String customerId;
    private String coachId;
    private ArrayList<LocalDate> trainingDates;
    private LocalDate nextTrainingDate;

    public TrainingHistory(LocalDateTime applicationDateTime, String training, String customer, String coach,ArrayList<LocalDate> dates, LocalDate startDate) {
        this.applicationDateTime = applicationDateTime;
        this.trainingId = training;
        this.customerId = customer;
        this.coachId = coach;
        this.trainingDates = dates;
        this.nextTrainingDate = startDate;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public LocalDateTime getApplicationDateTime() {
        return applicationDateTime;
    }

    public void setApplicationDateTime(LocalDateTime applicationDateTime) {
        this.applicationDateTime = applicationDateTime;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coach) {
        this.coachId = coach;
    }

    public ArrayList<LocalDate> getTrainingDates() {
        return trainingDates;
    }

    public void setTrainingDates(ArrayList<LocalDate> trainingDates) {
        this.trainingDates = trainingDates;
    }

    public LocalDate getNextTrainingDate() {
        return nextTrainingDate;
    }

    public void setNextTrainingDate(LocalDate startDate) {
        this.nextTrainingDate = startDate;
    }
}
