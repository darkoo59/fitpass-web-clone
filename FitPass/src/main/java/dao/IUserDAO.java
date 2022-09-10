package dao;

import model.User;

import java.io.IOException;
import java.util.ArrayList;

public interface IUserDAO extends IDAO<User> {
    ArrayList<User> getAllAndDeleted() throws IOException;
    void updateUserInfo(User user) throws Exception;
}
