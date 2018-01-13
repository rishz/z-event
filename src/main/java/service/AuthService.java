package service;

import dao.AppController;
import model.Event;
import model.LoginResult;
import model.User;
import utils.PasswordUtil;

import java.util.List;

public class AuthService {

    private AppController appController;

    public AuthService(String[] args){
        appController = new AppController(args);
    }

    public LoginResult checkUser(User user) {
        LoginResult result = new LoginResult();
        User userFound = appController.getUserbyUsername(user.getUsername());
        if(userFound == null) {
            result.setError("Invalid username");
        } else if(!PasswordUtil.verifyPassword(user.getPassword(), userFound.getPassword())) {
            result.setError("In" +
                    "valid password");
        } else {
            result.setUser(userFound);
        }

        return result;
    }

    public List<Event> getPublicEvents(){
        return appController.getPublicEvents();
    }

    public List<Event> getUserEvents(User user){
        return appController.getUserEvents(user);
    }

    public void registerUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        System.out.println(user.getPassword());
        appController.registerUser(user);
    }

    public User getUserbyUsername(String username) {
        return appController.getUserbyUsername(username);
    }

    public void addEvent(Event event) {
        System.out.println("ADD EVENT CALLED");
        appController.insertEvent(event);
    }
}
