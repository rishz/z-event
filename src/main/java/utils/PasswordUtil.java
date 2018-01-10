package utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by rishabhshukla on 10/01/18.
 */
public class PasswordUtil {

    public static String hashPassword(String pwd) {
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());

        return hashed;
    }

    public static boolean verifyPassword(String pwd, String hash) {
        boolean b = BCrypt.checkpw(pwd, hash);

        return b;
    }
}
