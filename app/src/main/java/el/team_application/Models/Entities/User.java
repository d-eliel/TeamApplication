package el.team_application.Models.Entities;

/**
 * Created by Eliel on 6/4/2015.
 */
public class User {
    String id;
    String email;

    // constructor
    public User(String id, String email){
        this.id = id;
        this.email = email;
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
