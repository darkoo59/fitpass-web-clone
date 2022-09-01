package service;

import controller.AdministratorController;
import dao.IDao;
import dao.UserDAO;
import model.SportsFacility;
import model.User;
import spark.*;
import utils.enums.GenderType;
import utils.enums.RoleType;
import java.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;

public class AdministratorService {
    private IDao userDAO;
    public AdministratorService()
    {
        userDAO = new UserDAO();
    }
    public void register(Request req) throws IOException {
        String name = req.queryParams("name");
        String surname = req.queryParams("surname");
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String membership = req.queryParams("membership");
        String date = req.queryParams("date");
        LocalDate parsedDate = LocalDate.parse(date);
        String sex = req.queryParams("sexRadioOptions");
        GenderType sexType = sex.equals("male") ? GenderType.MALE : GenderType.FEMALE;
        String role = req.queryParams("roleRadioOptions");
        RoleType roleType = role.equals("manager") ? RoleType.MANAGER : RoleType.COACH;
        User newUser = new User(username, password, name, surname, sexType, parsedDate, roleType);
        ArrayList<User> users = userDAO.getAll();
        users.add(newUser);
        userDAO.save(users);
    }

    public ArrayList<User> getAllProfiles() throws IOException {
        return userDAO.getAll();
    }

}
