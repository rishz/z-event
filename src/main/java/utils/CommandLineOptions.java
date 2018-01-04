package utils;

import com.beust.jcommander.Parameter;

public class CommandLineOptions {

        @Parameter(names = "--debug")
        public boolean debug = false;

        @Parameter(names = {"--service-port"})
        public Integer servicePort = 4567;

        @Parameter(names = {"--database"})
        public String database = "eventmanager";

        @Parameter(names = {"--db-host"})
        public String dbHost = "localhost";

        @Parameter(names = {"--db-username"})
        public String dbUsername = "abcd1234";

        @Parameter(names = {"--db-password"})
        public String dbPassword = "abcd1234";

        @Parameter(names = {"--db-port"})
        public Integer dbPort = 5432;
}