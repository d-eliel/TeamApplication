package el.team_application.Models;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import el.team_application.Listeners.AfterLoginCallback;
import el.team_application.Models.Entities.TeamMember;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class ModelPARSE {

    public void login (String email, String password, final AfterLoginCallback callback){
        ParseUser.logInInBackground(email,password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null && user != null) {
                    String id = user.getObjectId();
                    String name = user.getUsername();
                    String email = user.getEmail();
//                    TeamMember.Role role = TeamMember.Role.values()[user.getInt("role")];
                    TeamMember member = new TeamMember(id, name, email, TeamMember.Role.MANAGER);
                   callback.loginSuccessful(member);
                } else if (user == null) {
                    callback.usernameOrPasswordIsInvalid(e);
                }
                else {
                    callback.loginFailed(e);
                }
            }
            });
        }
}
