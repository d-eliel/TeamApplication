package el.team_application.Listeners.Members;

import java.util.List;

import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/21/2015.
 */
public interface GetMembersForTaskCallback {
    void onResult(List<TeamMember> members, Exception e);
}
