package model;

import java.util.ArrayList;

public class Customer extends User{
    private Membership membership;
    private ArrayList<SportsFacility> visitedFacilities;
    private Double collectedPoints;
    private CustomerType type;
}
