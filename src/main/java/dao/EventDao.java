package dao;

import model.Event;
import model.User;

import java.util.List;

public interface EventDao {
    void insertEvent(Event e);

    public List<Event> getPublicEvents();

    public List<Event> getUserEvents(User user);
}
