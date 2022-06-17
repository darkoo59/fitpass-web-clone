package model;

import utils.enums.RoleType;

import java.util.ArrayList;
import java.util.Date;

public class Customer extends User{
    private Membership membership;
    private ArrayList<SportsFacility> visitedFacilities;
    private Double collectedPoints;
    private CustomerType type;

    public Customer(String username, String password, String name, String surname, String gender, Date birthDate,
                    RoleType role, Membership membership, ArrayList<SportsFacility> visitedFacilities,
                    Double collectedPoints, CustomerType type) {
        super(username, password, name, surname, gender, birthDate, role);
        this.membership = membership;
        this.visitedFacilities = visitedFacilities;
        this.collectedPoints = collectedPoints;
        this.type = type;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public ArrayList<SportsFacility> getVisitedFacilities() {
        return visitedFacilities;
    }

    public void setVisitedFacilities(ArrayList<SportsFacility> visitedFacilities) {
        this.visitedFacilities = visitedFacilities;
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
