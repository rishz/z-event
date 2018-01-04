package db;

import java.util.List;
import java.util.UUID;

/**
 * Created by rishabhshukla on 05/01/18.
 */
public interface DbModel {
    UUID createUser(String username, String password);
    UUID createEvent(String eventName, String creator);
    List getAllEvents();
    List gettAllGoingUsers(UUID event);
    List getAllInterestedUsers(UUID event);

}
