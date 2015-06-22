package el.team_application.Listeners.Members;

import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/6/2015.
 */
public interface GetMemberByIdListener {
    void onResult(TeamMember member ,Exception e);
}
