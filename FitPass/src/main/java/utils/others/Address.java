package utils.others;

public class Address {
    private String streetAndNumber;
    private String place;
    private String zipCode;

    public Address(String streetAndNumber, String place, String zipCode) {
        this.streetAndNumber = streetAndNumber;
        this.place = place;
        this.zipCode = zipCode;
    }

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
