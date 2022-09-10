package model;

public class Content {
    private String id;
    private String name;
    private String type;
    private String image;
    private String description;
    private String duration;
    private String facilityId;

    public Content(String name, String type, String image, String description,String duration,String facilityId) {
        this.name = name;
        this.type = type;
        this.image = image;
        this.description = description;
        this.duration = duration;
        this.facilityId = facilityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }
}
