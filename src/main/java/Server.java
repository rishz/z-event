import com.beust.jcommander.JCommander;
import db.DbModel;
import db.Sql2oModel;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import utils.CommandLineOptions;

import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.port;


public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getCanonicalName());

    private static final int HTTP_BAD_REQUEST = 400;

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

        DbModel dbModel = new Sql2oModel(sql2o);

    }

}
