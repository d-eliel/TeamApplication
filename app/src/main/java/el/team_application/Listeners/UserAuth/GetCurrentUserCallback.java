package el.team_application.Listeners.UserAuth;

import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/6/2015.
 */
public interface GetCurrentUserCallback {
    void loggedInUser(User user);
}
