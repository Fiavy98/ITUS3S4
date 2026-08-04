package ui;
import db.*;

public class Session {
    private static int userId;
    private static String username;

    public static void start(int id, String name) {
        userId = id;
        username = name;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    public static void destroy() {
        userId = 0;
        username = null;
    }
}

