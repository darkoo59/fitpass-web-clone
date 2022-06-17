package model;

import utils.others.Address;

public class Location {
    private String id;
    private Double longitude;
    private Double latitude;
    private Address address;

    public Location(Double longitude, Double latitude, Address address) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
