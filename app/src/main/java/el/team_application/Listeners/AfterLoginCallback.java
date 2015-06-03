package el.team_application.Listeners;

import com.parse.ParseUser;

import java.text.ParseException;

import el.team_application.Models.Entities.TeamMember;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public interface AfterLoginCallback{
    void loginSuccessful(TeamMember user);
    void loginFailed(Exception e);
    void usernameOrPasswordIsInvalid(Exception e);
}
