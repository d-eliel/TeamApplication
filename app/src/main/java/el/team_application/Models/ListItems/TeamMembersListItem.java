package el.team_application.Models.ListItems;

import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/17/2015.
 */
public class TeamMembersListItem {
    String userId;
    String memberId;
    String email;
    String name;
    String phone;
    String joinDate;
    String jobTitle;
    String role;

    public TeamMembersListItem(User user, TeamMember member) {
        if(!user.getId().equals(member.getUserId())){
            return; // error
        }
        this.userId     = user.getId();
        this.email      = user.getEmail();
        this.name       = user.getName();
        this.phone      = user.getPhone();
        this.memberId   = member.getId();
        this.joinDate   = member.getJoinDate();
        this.jobTitle   = member.getJobTitle();
        TeamMember.Role role = member.getRole();
        if(role.equals(TeamMember.Role.EMPLOYEE)){
            this.role = "EMPLOYEE";
        }else{
            this.role = "MANAGER";
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
