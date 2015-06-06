package el.team_application.Listeners.Teams;

import java.util.List;

import el.team_application.Models.Entities.Team;

/**
 * Created by Eliel on 6/6/2015.
 */
public interface GetTeamByIdListener {
    void onResult(Team team, Exception e);
}
