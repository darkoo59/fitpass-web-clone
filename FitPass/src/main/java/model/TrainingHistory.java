package model;

import java.time.LocalDateTime;

public class TrainingHistory {
    private LocalDateTime applicationDateTime;
    private Training training;
    private Customer customer;
    private Coach coach;

    public TrainingHistory(LocalDateTime applicationDateTime, Training training, Customer customer, Coach coach) {
        this.applicationDateTime = applicationDateTime;
        this.training = training;
        this.customer = customer;
        this.coach = coach;
    }

    public LocalDateTime getApplicationDateTime() {
        return applicationDateTime;
    }

    public void setApplicationDateTime(LocalDateTime applicationDateTime) {
        this.applicationDateTime = applicationDateTime;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}
