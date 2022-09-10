package model;

import utils.enums.GenderType;
import utils.enums.RoleType;

import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends User{
    private String membershipId;
    private ArrayList<String> visitedFacilitiesIds;
    private Double collectedPoints;
    private CustomerType type;

    public Customer(java.lang.String username, java.lang.String password, java.lang.String name, java.lang.String surname, GenderType gender, LocalDate birthDate,
                  RoleType role, String membershipId, ArrayList<String> visitedFacilities,
                  Double collectedPoints, CustomerType type) {
        super(username, password, name, surname, gender, birthDate, role);
        this.membershipId = membershipId;
        this.visitedFacilitiesIds = visitedFacilities;
        this.collectedPoints = collectedPoints;
        this.type = type;
    }

    public String getMembership() {
        return membershipId;
    }

    public void setMembership(String membership) {
        this.membershipId = membership;
    }

    public ArrayList<String> getVisitedFacilities() {
        return visitedFacilitiesIds;
    }

    public void setVisitedFacilities(ArrayList<String> visitedFacilities) {
        this.visitedFacilitiesIds = visitedFacilities;
    }

    public Double getCollectedPoints() {
        return collectedPoints;
    }

    public void setCollectedPoints(Double collectedPoints) {
        this.collectedPoints = collectedPoints;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }
}
