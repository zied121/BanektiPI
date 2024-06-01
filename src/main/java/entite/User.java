package entite;

public class User {
    private String id;
    private String username;
    private String dateCreation;
    private String email;

    // Constructors
    public User() {}

    public User(String id, String username, String dateCreation, String email) {
        this.id = id;
        this.username = username;
        this.dateCreation = dateCreation;
        this.email = email;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}