package el.team_application.Listeners.User;

import el.team_application.Models.Entities.User;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public interface AfterLoginCallback{
    void loginSuccessful(User user);
    void loginFailed(Exception e);
    void usernameOrPasswordIsInvalid(Exception e);
}
