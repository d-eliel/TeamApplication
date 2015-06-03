package el.team_application.Models.Entities;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class TeamMember extends Member {
    public enum Role {MANAGER, EMPLOYEE}; //values are 1-2 accordingly
    //def protedted

    //members - (default protected)
    Role role = Role.EMPLOYEE; //default Role is Employee


    //getters and setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //C'tor
    public TeamMember(String id, String name, String emailAddress, Role role) {
        super(id, name, emailAddress);
        this.role = role;
    }
}
