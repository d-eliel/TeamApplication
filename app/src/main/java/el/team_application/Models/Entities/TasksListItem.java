package el.team_application.Models.Entities;

import java.util.List;

import el.team_application.Models.ListItems.TeamMembersListItem;

/**
 * Created by Eliel on 6/18/2015.
 */
public class TasksListItem {
    String name;
    String creator;
    String startDate;
    String endDate;
    String Description;

    List<TeamMembersListItem> members;


}
