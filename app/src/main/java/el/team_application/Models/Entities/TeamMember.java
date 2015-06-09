package el.team_application.Models.Entities;

import java.util.Date;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class TeamMember extends Member {
    // values are 0-1 accordingly
    public enum Role {MANAGER, EMPLOYEE}

    // members - (default protected)
    Role role = Role.EMPLOYEE; //default Role is Employee

    // getters and setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // constructor
    public TeamMember(String id, String userId, String joinDate, String jobTitle, Role role) {
        super(id, userId, jobTitle, joinDate);
        this.role = role;
    }
}
