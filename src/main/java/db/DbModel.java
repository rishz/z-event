package db;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface DbModel {
    UUID createUser(String username, String password);
    UUID createEvent(String eventName, String creator, String name, String description, Date date);
    List getAllEvents();
    List gettAllGoingUsers(UUID event);
    List getAllInterestedUsers(UUID event);
    boolean existEvent();

}
