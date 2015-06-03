package el.team_application.Listeners;

import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/4/2015.
 */
public interface AfterRegisterCallback {
    void registerSuccessful(TeamMember user);
    void registerFailed(Exception e);
    void usernameOrPasswordIsInvalid(Exception e);
}
