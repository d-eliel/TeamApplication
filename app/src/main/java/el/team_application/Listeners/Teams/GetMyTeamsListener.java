package el.team_application.Listeners.Teams;

import java.util.List;

import el.team_application.Models.Entities.Team;

/**
 * Created by Eliel on 6/6/2015.
 */
public interface GetMyTeamsListener {
    void onResult(List<Team> myTeams, Exception e);
}
