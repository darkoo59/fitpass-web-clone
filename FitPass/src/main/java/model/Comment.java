package model;

import utils.enums.Rating;

public class Comment {
    private Customer customer;
    private SportsFacility sportsFacility;
    private String text;
    private Rating rating;

    public Comment(Customer customer, SportsFacility sportsFacility, String text, Rating rating) {
        this.customer = customer;
        this.sportsFacility = sportsFacility;
        this.text = text;
        this.rating = rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SportsFacility getSportsFacility() {
        return sportsFacility;
    }

    public void setSportsFacility(SportsFacility sportsFacility) {
        this.sportsFacility = sportsFacility;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
