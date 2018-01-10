import com.beust.jcommander.JCommander;
import config.WebConfig;
import dao.EventDao;
import dao.UserDao;
import db.Sql2oModel;
import model.Event;
import model.User;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import service.AuthService;
import utils.CommandLineOptions;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.port;


public class Server implements EventDao, UserDao{
    private static final Logger logger = Logger.getLogger(Server.class.getCanonicalName());

    private static final int HTTP_BAD_REQUEST = 400;

    private static Sql2oModel dbModel;

    public static void main(String[] args) {

        CommandLineOptions options = new CommandLineOptions();
        new JCommander(options, args);

        logger.finest("Options.debug = " + options.debug);
        logger.finest("Options.database = " + options.database);
        logger.finest("Options.dbHost = " + options.dbHost);
        logger.finest("Options.dbUsername = " + options.dbUsername);
        logger.finest("Options.dbPort = " + options.dbPort);
        logger.finest("Options.servicePort = " + options.servicePort);

        port(options.servicePort);

        Sql2o sql2o = new Sql2o("jdbc:postgresql://" + options.dbHost + ":" + options.dbPort + "/" + options.database,
                options.dbUsername, options.dbPassword, new PostgresQuirks() {
            {
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        dbModel = new Sql2oModel(sql2o);

        AuthService authService = new AuthService();

        new WebConfig(authService);

    }

    @Override
    public User getUserbyUsername(String username) {
        return null;
    }

    @Override
    public void registerUser(User user) {
        dbModel.createUser(user);
    }

    @Override
    public void insertEvent(Event e) {
        dbModel.createEvent(e);
    }

    @Override
    public List<Event> getPublicEvents() {
        return dbModel.getAllEvents();
    }

    @Override
    public List<Event> getUserEvents(User user) {
        return null;
    }

    @Override
    public void addGoingUser(User user, Event e) {
        dbModel.addGoingUser(user.getUsername(), e.getEvent_uuid());
    }

    @Override
    public void addInterestedUser(User user, Event e) {
        dbModel.addInterestedUser(user.getUsername(), e.getEvent_uuid());

    }
}
