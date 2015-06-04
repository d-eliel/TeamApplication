package el.team_application.Models.Entities;

import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class Team {

    // members - (default protected)
    String id;
    List<TeamMember> memberList;
    TeamMember manager;
    List<Task> taskList;


    // constructor
    public Team(TeamMember manager) {
        this.manager = manager;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TeamMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<TeamMember> memberList) {
        this.memberList = memberList;
    }

    public TeamMember getManager() {
        return manager;
    }

    public void setManager(TeamMember manager) {
        this.manager = manager;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
