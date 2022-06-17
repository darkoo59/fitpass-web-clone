package model;

import utils.enums.CustomerTypeName;

public class CustomerType {
    private CustomerTypeName type;
    private double percentage;
    private double points;

    public CustomerType(CustomerTypeName type, double percentage, double points) {
        this.type = type;
        this.percentage = percentage;
        this.points = points;
    }

    public CustomerTypeName getType() {
        return type;
    }

    public void setType(CustomerTypeName type) {
        this.type = type;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
