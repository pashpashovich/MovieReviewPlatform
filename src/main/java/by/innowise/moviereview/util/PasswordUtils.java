package by.innowise.moviereview.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verify(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
