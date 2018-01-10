package service;

import dao.EventDao;
import dao.UserDao;
import model.Event;
import model.LoginResult;
import model.User;
import utils.PasswordUtil;

import java.util.List;


public class AuthService {

    private UserDao userDao;

    private EventDao eventDao;

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

    public List<Event> getPublicEvents(){
        return eventDao.getPublicEvents();
    }

    public List<Event> getUserEvents(User user){
        return eventDao.getUserEvents(user);
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
