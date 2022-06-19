package service;

import dao.IDao;
import dao.UserDAO;
import model.User;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.ArrayList;

public class AccountService {
    private IDao userDAO;

    public AccountService()
    {
        userDAO = new UserDAO();
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
