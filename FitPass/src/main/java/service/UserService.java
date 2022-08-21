package service;

import dao.UserDAO;
import model.User;
import utils.enums.RoleType;

import java.io.IOException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public RoleType getRoleTypeByUsername(String username) throws IOException {
        for (User user : userDAO.getAll()) {
            if(user.getUsername().equals(username)){
            switch (user.getRole()) {
                case CUSTOMER:
                    return RoleType.CUSTOMER;
                case ADMINISTRATOR:
                    return RoleType.ADMINISTRATOR;
                case COACH:
                    return RoleType.COACH;
                case MANAGER:
                    return RoleType.MANAGER;
                default:
                    return null;
            }
            }
        }
        return null;
    }
}
