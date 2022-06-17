package model;

import java.awt.*;
import java.sql.Time;

public class Training {

    private String id;
    private String name;
    private String type;
    private SportsFacility facility;
    private Time duration;
    private Coach coach;
    private String description;
    private Image image;

    public Training(String name, String type, SportsFacility facility, Time duration, Coach coach, String description, Image image) {
        this.name = name;
        this.type = type;
        this.facility = facility;
        this.duration = duration;
        this.coach = coach;
        this.description = description;
        this.image = image;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SportsFacility getFacility() {
        return facility;
    }

    public void setFacility(SportsFacility facility) {
        this.facility = facility;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
