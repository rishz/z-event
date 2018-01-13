import config.WebConfig;
import service.AuthService;

import java.util.logging.Logger;


public class Server{
    private static final Logger logger = Logger.getLogger(Server.class.getCanonicalName());

    public static void main(String[] args) {

        AuthService authService = new AuthService(args);

        new WebConfig(authService);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
