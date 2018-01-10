package dao;

import model.User;

public interface UserDao {
    User getUserbyUsername(String username);
    void registerUser(User user);
}
