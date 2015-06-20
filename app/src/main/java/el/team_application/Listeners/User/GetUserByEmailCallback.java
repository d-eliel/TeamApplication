package el.team_application.Listeners.User;

import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/19/2015.
 */
public interface GetUserByEmailCallback {
    void onResult(User user, Exception e);
}
