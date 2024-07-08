package util;

public class UserSession {

    private static UserSession instance;
    private int userId;
    private  String role;
    private UserSession() {
        // private constructor to prevent instantiation
        userId = 0; // Or initialize with a default value
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public  int getUserId() {
        return userId;
    }

    public void setUserRole(String role) {
    }
    public String getUserRole(){
        return role;
    }
}