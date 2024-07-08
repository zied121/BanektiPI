package util;

import entite.User;

public class UserSession {

    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void initSession(User user) {
        if (instance == null) {
            instance = new UserSession(user);
        }
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void clearSession() {
        instance = null;
    }

    public int getUserId() {
        return 0;
    }
}
