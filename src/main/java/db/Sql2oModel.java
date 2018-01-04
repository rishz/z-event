package db;

import model.Event;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import utils.RandomUuidGenerator;
import utils.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Sql2oModel implements DbModel {

    private Sql2o sql2o;
    private UuidGenerator uuidGenerator;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
        uuidGenerator = new RandomUuidGenerator();
    }

    @Override
    public UUID createUser(String username, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userUuid = uuidGenerator.generate();
            conn.createQuery("insert into users(user_uuid, username, password) VALUES (:user_uuid, :username, :password)")
                    .addParameter("user_uuid", userUuid)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();
            return userUuid;
        }
    }

    @Override
    public UUID createEvent(String name, String creator, String description, Date date, List<String> categories) {
        try (Connection conn = sql2o.open()) {
            UUID event_uuid = uuidGenerator.generate();
            conn.createQuery("insert into events(event_uuid, name, creator, description, date) VALUES (:event_uuid, :name, :creator, :description, :date)")
                    .addParameter("event_uuid", event_uuid)
                    .addParameter("name", name)
                    .addParameter("creator", creator)
                    .addParameter("description", description)
                    .addParameter("date", date)
                    .executeUpdate();
            categories.forEach((category) ->
                    conn.createQuery("insert into events_categories(event_uuid, category) VALUES (:event_uuid, :category)")
                            .addParameter("event_uuid", event_uuid)
                            .addParameter("category", category)
                            .executeUpdate());
            conn.commit();
            return event_uuid;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try (Connection conn = sql2o.open()) {
            List<Event> events = conn.createQuery("select * from events")
                    .executeAndFetch(Event.class);
            events.forEach((event) -> event.setCategories(getCategoriesFor(conn, event.getEvent_uuid())));
            return events;
        }
    }

    private List<String> getCategoriesFor(Connection conn, UUID event_uuid) {
        return conn.createQuery("select category from events_categories where event_uuid=:event_uuid")
                .addParameter("event_uuid", event_uuid)
                .executeAndFetch(String.class);
    }

    @Override
    public List gettAllGoingUsers(UUID event) {
        return null;
    }

    @Override
    public List getAllInterestedUsers(UUID event) {
        return null;
    }

    @Override
    public boolean existEvent(UUID event) {
        try (Connection conn = sql2o.open()) {
            List<Event> events = conn.createQuery("select * from posts where post_uuid=:post")
                    .addParameter("event", event)
                    .executeAndFetch(Event.class);
            return events.size() > 0;
        }
    }
}
