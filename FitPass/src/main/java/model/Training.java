package model;

import utils.others.WorkHour;

public class Training {

    private String id;
    private String name;
    private String type;
    private String facilityId;
    private WorkHour duration;
    private String coachId;
    private String description;
    private String image;

    public Training(java.lang.String name, java.lang.String type, String facility, WorkHour duration, String coachId, java.lang.String description, java.lang.String image) {
        this.name = name;
        this.type = type;
        this.facilityId = facility;
        this.duration = duration;
        this.coachId = coachId;
        this.description = description;
        this.image = image;
    }

    public java.lang.String getId() { return id; }

    public void setId(java.lang.String id) { this.id = id; }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public WorkHour getDuration() {
        return duration;
    }

    public void setDuration(WorkHour duration) {
        this.duration = duration;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getImage() {
        return image;
    }

    public void setImage(java.lang.String image) {
        this.image = image;
    }
}
