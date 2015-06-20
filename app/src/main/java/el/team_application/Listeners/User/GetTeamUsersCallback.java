package el.team_application.Listeners.User;

import java.util.List;

import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/17/2015.
 */
public interface GetTeamUsersCallback {
    void onResult(List<User> users ,Exception e);
}
