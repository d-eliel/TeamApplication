package el.team_application.Listeners.Members;

import java.util.List;

import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/6/2015.
 */
public interface GetAllTeamMembersListener {
    void onResult(List<TeamMember> members, Exception e);
}
