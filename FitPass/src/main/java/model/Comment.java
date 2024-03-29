package model;

import utils.enums.CommentStatus;
import utils.enums.Rating;

public class Comment {

    private String id;
    private String customerId;
    private String sportsFacilityId;
    private String text;
    private Rating rating;
    private CommentStatus status;
    private boolean deleted;

    public Comment(String customerId, String sportsFacilityId, String text, Rating rating, CommentStatus status) {
        this.customerId = customerId;
        this.sportsFacilityId = sportsFacilityId;
        this.text = text;
        this.rating = rating;
        this.status = status;
    }

    public java.lang.String getId() { return id; }

    public void setId(java.lang.String id) { this.id = id; }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
            this.customerId = customerId;
    }

    public String getSportsFacilityId() {
        return sportsFacilityId;
    }

    public void setSportsFacilityId(String sportsFacilityId) {
        this.sportsFacilityId = sportsFacilityId;
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

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
