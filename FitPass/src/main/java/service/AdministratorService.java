package service;

import controller.AdministratorController;
import dao.IDao;
import dao.SportsFacilityDAO;
import dao.UserDAO;
import model.SportsFacility;
import model.User;
import spark.*;
import utils.enums.GenderType;
import utils.enums.RoleType;

import java.lang.reflect.Array;
import java.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;

public class AdministratorService {
    private IDao userDAO;
    private SportsFacilityDAO facilityDAO;
    private ArrayList<User> allUsers;
    public AdministratorService() throws IOException {
        userDAO = new UserDAO();
        facilityDAO = new SportsFacilityDAO();
        allUsers = userDAO.getAll();
    }
    public void register(Request req) throws IOException {
        String name = req.queryParams("name");
        String surname = req.queryParams("surname");
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String date = req.queryParams("date");
        LocalDate parsedDate = LocalDate.parse(date);
        String sex = req.queryParams("sexRadioOptions");
        GenderType sexType = sex.equals("male") ? GenderType.MALE : GenderType.FEMALE;
        String role = req.queryParams("roleRadioOptions");
        RoleType roleType = role.equals("manager") ? RoleType.MANAGER : RoleType.COACH;
        User newUser = new User(username, password, name, surname, sexType, parsedDate, roleType);
        newUser.setId(String.valueOf(userDAO.getNewId()));
        ArrayList<User> users = userDAO.getAll();
        users.add(newUser);
        userDAO.save(users);
    }

    public ArrayList<User> getSearchedProfiles(Request req) throws IOException {
        ArrayList<User> searchedUsers = new ArrayList<User>();
        for(User user : allUsers)
        {
            if(user.getName().contains(req.body()) || user.getSurname().contains(req.body()) || user.getUsername().contains(req.body()))
                searchedUsers.add(user);
        }
        for(User user : searchedUsers)
            System.out.println(user.getUsername());
        return searchedUsers;
    }

    public ArrayList<User> getAllProfiles() throws IOException {
        return userDAO.getAll();
    }

    public ArrayList<User> getManagersForNewFacility() throws IOException {
        ArrayList<User> managers = new ArrayList<User>();
        ArrayList<SportsFacility> allFacilities = facilityDAO.getAll();
        ArrayList<User> allUsers = userDAO.getAll();
        ArrayList<String> managersWithFacilityId = new ArrayList<String>();
        for(SportsFacility facility : allFacilities)
            managersWithFacilityId.add(facility.getManagerId());
        for(User user:allUsers)
        {
            if(user.getRole() == RoleType.MANAGER && !managersWithFacilityId.contains(user.getId()))
            {
                managers.add(user);
            }
        }
        return managers;
    }

}
