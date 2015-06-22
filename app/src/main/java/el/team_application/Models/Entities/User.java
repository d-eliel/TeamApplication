package el.team_application.Models.Entities;

import java.util.List;

/**
 * Created by Eliel on 6/4/2015.
 */
public class User {
    String id;
    String email;
    String name;
    String phone;
    List<String> myTeams;

    // constructor
    public User(String id, String email, String name){
        this.id = id;
        this.email = email;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getMyTeams() {
        return myTeams;
    }

    public void setMyTeams(List<String> myTeams) {
        this.myTeams = myTeams;
    }

    @Override
    public boolean equals(Object obj){
        User user = (User) obj;
        if(this.getId().equals(user.getId())){
            return true;
        }else{
            return false;
        }
    }
}
