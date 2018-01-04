package db;

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
//            categories.forEach((category) ->
//                    conn.createQuery("insert into posts_categories(post_uuid, category) VALUES (:post_uuid, :category)")
//                            .addParameter("post_uuid", postUuid)
//                            .addParameter("category", category)
//                            .executeUpdate());
            conn.commit();
            return userUuid;
        }
    }

    @Override
    public UUID createEvent(String eventName, String creator, String name, String description, Date date) {
        try (Connection conn = sql2o.open()) {
            UUID event_uuid = uuidGenerator.generate();
            conn.createQuery("insert into events(event_uuid, name, creator, description, date) VALUES (:event_uuid, :name, :creator, :description, :date)")
                    .addParameter("event_uuid", event_uuid)
                    .addParameter("name", name)
                    .addParameter("creator", creator)
                    .addParameter("description", description)
                    .addParameter("approved", false)
                    .addParameter("date", date)
                    .executeUpdate();
            return event_uuid;
        }
    }

    @Override
    public List getAllEvents() {
        return null;
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
    public boolean existEvent() {
        return false;
    }
}
