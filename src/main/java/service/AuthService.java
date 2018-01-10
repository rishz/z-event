package service;

import dao.UserDao;
import model.LoginResult;
import model.User;
import utils.PasswordUtil;

/**
 * Created by rishabhshukla on 10/01/18.
 */
public class AuthService {

    private UserDao userDao;

    public LoginResult checkUser(User user) {
        LoginResult result = new LoginResult();
        User userFound = userDao.getUserbyUsername(user.getUsername());
        if(userFound == null) {
            result.setError("Invalid username");
        } else if(!PasswordUtil.verifyPassword(user.getPassword(), userFound.getPassword())) {
            result.setError("Invalid password");
        } else {
            result.setUser(userFound);
        }

        return result;
    }

    public void registerUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDao.registerUser(user);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserbyUsername(String username) {
        return userDao.getUserbyUsername(username);
    }
}
