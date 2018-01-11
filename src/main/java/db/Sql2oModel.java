package db;

import model.Event;
import model.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import utils.RandomUuidGenerator;
import utils.UuidGenerator;

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
    public UUID createUser(User user) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userUuid = uuidGenerator.generate();
            conn.createQuery("insert into users(user_uuid, username, email, password) VALUES (:user_uuid, :username, :email, :password)")
                    .addParameter("user_uuid", userUuid)
                    .addParameter("username", user.getUsername())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword())
                    .executeUpdate();
            conn.commit();
            System.out.println(userUuid.toString());
            return userUuid;
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UUID createEvent(Event e) {
        try (Connection conn = sql2o.open()) {
            UUID event_uuid = uuidGenerator.generate();
            conn.createQuery("insert into events(event_uuid, name, organizer, description, date) VALUES (:event_uuid, :name, :organizer, :description, :date)")
                    .addParameter("event_uuid", event_uuid)
                    .addParameter("name", e.getName())
                    .addParameter("organizer", e.getOrganizer())
                    .addParameter("description", e.getDescription())
                    .addParameter("date", e.getDate())
                    .executeUpdate();
            e.getCategories().forEach((category) ->
                    conn.createQuery("insert into events_categories(event_uuid, category) VALUES (:event_uuid, :category)")
                            .addParameter("event_uuid", event_uuid)
                            .addParameter("category", category)
                            .executeUpdate());
            conn.commit();
            return event_uuid;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
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
    public List<String> getAllGoingUsers(UUID event) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select going_users from events where event_uuid=:event_uuid")
                    .addParameter("event_uuid", event)
                    .executeAndFetch(String.class);
        }
    }

    @Override
    public List<String> getAllInterestedUsers(UUID event) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select interested_users from events where event_uuid=:event_uuid")
                    .addParameter("event_uuid", event)
                    .executeAndFetch(String.class);
        }
    }

    @Override
    public boolean existUser(String username) {
        try (Connection conn = sql2o.open()) {
            User user =  conn.createQuery("select * from users where username=:username")
                    .addParameter("username", username)
                    .executeAndFetchFirst(User.class);
            return user!=null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        if(existUser(username)){
            try (Connection conn = sql2o.open()) {
                return conn.createQuery("select * from users where username=:username")
                        .addParameter("username", username)
                        .executeAndFetchFirst(User.class);
            }
        }else {
            System.out.println("User doesn't exist");
            return null;
        }
    }

    @Override
    public void addGoingUser(String username, UUID event) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into events(event_uuid, going_users) VALUES (:event_uuid, :going_users)")
                    .addParameter("event_uuid", event)
                    .addParameter("going_users", username)
                    .executeUpdate();
            conn.commit();;
        }
    }

    @Override
    public void addInterestedUser(String username, UUID event) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into events(event_uuid, interested_users) VALUES (:event_uuid, :interested_users)")
                    .addParameter("event_uuid", event)
                    .addParameter("interested_users", username)
                    .executeUpdate();
            conn.commit();;
        }
    }
}
