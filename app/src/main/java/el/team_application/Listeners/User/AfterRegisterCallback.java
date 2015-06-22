package el.team_application.Listeners.User;

import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/4/2015.
 */
public interface AfterRegisterCallback {
    void registerSuccessful(User user);
    void registerFailed(Exception e);
}
