package el.team_application.Models;

import el.team_application.Listeners.User.AfterLoginCallback;
import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Models.Entities.User;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class ModelSQL {

    //region Team CRUD

    public void createTeam(){

    }

    public void removeTeam(){

    }

    public void editTeam(){

    }

    public void getMyTeams(){

    }

    public void getTeamById(){

    }

    //endregion

    //region TeamMember CRUD

    public void addMember(){

    }

    public void removeMember(){

    }

    public void editMember(){

    }

    public void getMember(){

    }

    public void getAllTeamMembers(){

    }

    public void getAllTaskMembers(){

    }

    //endregion

    //region Task CRUD

    public void addTask(){

    }

    public void deleteTask(){

    }

    public void editTask(){

    }

    public void getTask(){

    }

    public void getTeamTasks(){

    }

    public void getUserTasks(){

    }

    public void getTeamMemberTasks(){

    }

    //endregion

    //region User Authentication

    // get current logged in user (saved session)
    public void getCurrentUser(final GetSessionCallback callback){
    }

    // model login method, creating instance of a table if there isn't
    public void login(String email, String password, final AfterLoginCallback loginCallback) {

    }

    public void register(User user, String password, final AfterRegisterCallback registerCallback) {
    }

    public void logout(){
    }

    //endregion

}
