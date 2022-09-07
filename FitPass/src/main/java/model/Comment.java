package model;

import utils.enums.Rating;

public class Comment {

    private java.lang.String id;
    private String customer;
    private SportsFacility sportsFacility;
    private java.lang.String text;
    private Rating rating;

    public Comment(String customer, SportsFacility sportsFacility, java.lang.String text, Rating rating) {
        this.customer = customer;
        this.sportsFacility = sportsFacility;
        this.text = text;
        this.rating = rating;
    }

    public java.lang.String getId() { return id; }

    public void setId(java.lang.String id) { this.id = id; }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public SportsFacility getSportsFacility() {
        return sportsFacility;
    }

    public void setSportsFacility(SportsFacility sportsFacility) {
        this.sportsFacility = sportsFacility;
    }

    public java.lang.String getText() {
        return text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
