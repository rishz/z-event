package db;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface DbModel {
    UUID createUser(String username, String password);
    UUID createEvent(String name, String creator, String description, Date date, List<String> categories);
    List getAllEvents();
    List gettAllGoingUsers(UUID event);
    List getAllInterestedUsers(UUID event);
    boolean existEvent(UUID event);

}
