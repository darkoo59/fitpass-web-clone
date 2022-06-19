package service;

import dao.IDao;
import dao.UserDAO;
import model.User;
import spark.Request;
import utils.enums.GenderType;
import utils.enums.RoleType;

import java.io.IOException;
import java.time.LocalDate;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.ArrayList;

public class AccountService {
    private IDao userDAO;

    public AccountService() {
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
        String sex = req.queryParams("inlineRadioOptions");
        GenderType type = sex.equals("male") ? GenderType.MALE : GenderType.FEMALE;
        User newUser = new User(username, password, name, surname, type, parsedDate, RoleType.CUSTOMER);
        ArrayList<User> users = userDAO.getAll();
        users.add(newUser);
        userDAO.save(users);
    }

    public User loginUser(Request request) throws IOException {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        System.out.println("Username = "+username+" password = "+password);
        ArrayList<User> users = userDAO.getAll();
        for (User user:users) {
            if(user.getUsername().equals(username) && user.getPassword().equals(password))
                return user;
        }
        return null;
    }
}
