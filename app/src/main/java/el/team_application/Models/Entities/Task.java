package el.team_application.Models.Entities;

import java.util.Date;
import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class Task {

    public enum Status {NOTHING, STARTED, HALF, ALMOST, FINISH}; //values are 0-4 accordingly

    String id;
    String name;
    TeamMember creator;
    Date startDate;
    Date endDate;
    Status status;
    String description;
    String association;
    List<Task> listOfSubTasks;
    List<TeamMember> memberList;

    // constructor
    public Task(String id, Date startDate, TeamMember creator, String name) {
        this.id = id;
        this.startDate = startDate;
        this.creator = creator;
        this.name = name;
        this.status = Status.NOTHING; //starts with 0%
    }

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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Task> getListOfSubTasks() {
        return listOfSubTasks;
    }

    public void setListOfSubTasks(List<Task> listOfSubTasks) {
        this.listOfSubTasks = listOfSubTasks;
    }

    public List<TeamMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<TeamMember> memberList) {
        this.memberList = memberList;
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
}
