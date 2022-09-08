package dao;

import model.User;

public interface IUserDAO extends IDAO<User> {
    void updateUserInfo(User user) throws Exception;
}
