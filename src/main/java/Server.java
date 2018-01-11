import config.WebConfig;
import service.AuthService;

import java.util.logging.Logger;


public class Server{
    private static final Logger logger = Logger.getLogger(Server.class.getCanonicalName());

    public static void main(String[] args) {

        AuthService authService = new AuthService(args);

        new WebConfig(authService);

    }
}
