package model;

import utils.enums.SportsFacilityStatus;
import utils.others.WorkHour;

import java.awt.*;
import java.util.List;

public class SportsFacility {
    private String name;
    private String type;
    private List<String> content;
    private SportsFacilityStatus status;
    private Location location;
    private Image logo;
    private double averageRating;
    private WorkHour workHour;
}
