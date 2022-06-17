package model;

import utils.enums.RoleType;

import java.util.Date;

public class Manager extends User{
    //TODO sportski objekAT
    private SportsFacility sportFacility;

    public Manager(String username, String password, String name, String surname, String gender, Date birthDate,
                   RoleType role, SportsFacility sportFacility) {
        super(username, password, name, surname, gender, birthDate, role);
        this.sportFacility = sportFacility;
    }

    public SportsFacility getSportFacility() {
        return sportFacility;
    }

    public void setSportFacility(SportsFacility sportFacility) {
        this.sportFacility = sportFacility;
    }
}
