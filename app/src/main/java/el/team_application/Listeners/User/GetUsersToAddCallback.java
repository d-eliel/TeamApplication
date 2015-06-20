package el.team_application.Listeners.User;

import java.util.List;

import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/18/2015.
 */
public interface GetUsersToAddCallback {
    void onResult(List<User> users, Exception e);
}
