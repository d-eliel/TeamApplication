package el.team_application.Models.Entities;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class Task {

    public enum Status {NOTHING, STARTED, HALF, ALMOST, FINISH}; //values are 0-4 accordingly

    String id;
    String name;
    TeamMember creator;
    String startDate;
    String endDate;
    Status status;
    String description;
    String association;
//    List<Task> listOfSubTasks;
    List<String> membersIdList;
    String teamId;

    // constructor
    public Task(String id, String startDate, TeamMember creator, String name) {
        this.id = id;
        this.startDate = startDate;
        this.creator = creator;
        this.name = name;
        this.status = Status.NOTHING; //starts with 0%
//        listOfSubTasks = new LinkedList<>();
        membersIdList = new LinkedList<>();
    }

    /* constructor for sql model
    public Task(String id, String name, TeamMember creator, String startDate, String endDate, String description, String association ) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = Status.NOTHING; //starts with 0%
        this.description = description;
        listOfSubTasks = new LinkedList<>();
        membersIdList = new LinkedList<>();
    }
    */

    // getters and setters
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

    public TeamMember getCreator() {
        return creator;
    }

    public void setCreator(TeamMember creator) {
        this.creator = creator;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

//    public List<Task> getListOfSubTasks() {
//        return listOfSubTasks;
//    }
//
//    public void setListOfSubTasks(List<Task> listOfSubTasks) {
//        this.listOfSubTasks = listOfSubTasks;
//    }

    public List<String> getMemberList() {
        return membersIdList;
    }

    public void setMemberList(List<String> memberList) {
        this.membersIdList = memberList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

//    public List<String> getMembersIdList() {
//        return membersIdList;
//    }
//
//    public void setMembersIdList(List<String> membersIdList) {
//        this.membersIdList = membersIdList;
//    }

    @Override
    public boolean equals(Object obj){
        Task task = (Task) obj;
        if(this.getId().equals(task.getId())){
            return true;
        }else{
            return false;
        }
    }

}
