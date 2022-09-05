package model;

import utils.others.Address;

public class Location {
    private String id;
    private Double latitude;
    private Double longitude;
    private Address address;

    public Location(Double latitude, Double longitude, Address address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Double getLatitude() { return latitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
