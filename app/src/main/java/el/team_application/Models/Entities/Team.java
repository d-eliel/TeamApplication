package el.team_application.Models.Entities;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class Team {
    String id;
    String name;
    List<TeamMember> memberList;
//    TeamMember manager;
    List<Task> taskList;


    // constructor
    public Team(String id, String name, List<TeamMember> members) {
        this.id = id;
        this.name = name;
        this.memberList = new LinkedList<TeamMember>();
        this.memberList.addAll(members);
        this.taskList = new LinkedList<Task>();
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeamMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<TeamMember> memberList) {
        this.memberList = memberList;
    }

//    public TeamMember getManager() {
//        return manager;
//    }
//
//    public void setManager(TeamMember manager) {
//        this.manager = manager;
//    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
