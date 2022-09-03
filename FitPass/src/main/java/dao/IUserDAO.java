package dao;

import model.User;

public interface IUserDAO extends IDao<User> {
    void updateUserInfo(User user) throws Exception;
}
