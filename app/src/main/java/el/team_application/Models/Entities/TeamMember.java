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
    String teamId;


    // getters and setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getTeamId(){
        return teamId;
    }

    public void setTeamId(String teamId){
        this.teamId = teamId;
    }

    // constructor
    public TeamMember(String id, String userId, String joinDate, String jobTitle, Role role) {
        super(id, userId, joinDate, jobTitle);
        if(role == null)
            this.role = Role.EMPLOYEE;
        else
            this.role = role;

        if(jobTitle == null)
            this.jobTitle = role.toString();
    }

    @Override
    public boolean equals(Object obj){
        TeamMember member = (TeamMember) obj;
        if(this.getId().equals(member.getId())){
            return true;
        }else{
            return false;
        }
    }
}
