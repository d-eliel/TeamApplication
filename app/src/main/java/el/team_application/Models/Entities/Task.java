package el.team_application.Models.Entities;

import java.util.Date;
import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class Task {

    public enum Status {NOTHING, STARTED, HALF, ALMOST, FINISH}; //values are 0-4 accordingly

    //members - (default protected)
    String Name;
    String TaskId;
    TeamMember Creator;
    Date StartDate;
    Date EndDate;
    List<String> ListOfSubTasks; //list of Strings, each string is the ID  primary key of the Task that is a subtask
    List<TeamMember> MemberList;
    Status status = Status.NOTHING; //starts with 0%
    String Description;
    String Association;
    String Id;


    //getters and setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public TeamMember getCreator() {
        return Creator;
    }

    public void setCreator(TeamMember creator) {
        Creator = creator;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public List<String> getListOfSubTasks() {
        return ListOfSubTasks;
    }

    public void setListOfSubTasks(List<String> listOfSubTasks) {
        ListOfSubTasks = listOfSubTasks;
    }

    public List<TeamMember> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<TeamMember> memberList) {
        MemberList = memberList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAssociation() {
        return Association;
    }

    public void setAssociation(String association) {
        Association = association;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    //C'tor
    public Task(Date startDate, TeamMember creator, String name) {
        StartDate = startDate;
        Creator = creator;
        Name = name;
    }

}
