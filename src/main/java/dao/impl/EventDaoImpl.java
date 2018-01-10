package dao.impl;

import dao.EventDao;
import model.Event;
import model.User;

import java.util.List;

/**
 * Created by rishabhshukla on 10/01/18.
 */
public class EventDaoImpl implements EventDao {
    @Override
    public void insertEvent(Event e) {

    }

    @Override
    public List<Event> getPublicEvents() {
        return null;
    }

    @Override
    public List<Event> getUserEvents(User user) {
        return null;
    }
}
