package model;

import utils.enums.SportsFacilityStatus;
import utils.others.WorkHour;

import java.awt.*;
import java.util.List;

public class SportsFacility {
    String name;
    String type;
    List<String> content;
    SportsFacilityStatus status;
//    Location location;
    Image logo;
    double averageRating;
    WorkHour workHour;
}
