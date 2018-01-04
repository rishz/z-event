package db;

import org.sql2o.Sql2o;

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
        return null;
    }

    @Override
    public UUID createEvent(String eventName, String creator) {
        return null;
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
