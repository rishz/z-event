package db;

import model.Event;
import model.User;

import java.util.List;
import java.util.UUID;


public interface DbModel {
    UUID createUser(User user);
    UUID createEvent(Event e);
    List getAllEvents();
    List getAllGoingUsers(UUID event);
    List getAllInterestedUsers(UUID event);
    boolean existUser(String username);
    User getUserByUsername(String username);
    void addGoingUser(String username, UUID event);
    void addInterestedUser(String username, UUID event);
}
