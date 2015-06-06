package el.team_application.Models;

import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import el.team_application.Listeners.Members.AddMemberListener;
import el.team_application.Listeners.Members.EditMemberListener;
import el.team_application.Listeners.Members.GetAllTeamMembersListener;
import el.team_application.Listeners.Members.GetMemberByIdListener;
import el.team_application.Listeners.Members.RemoveMemberListener;
import el.team_application.Listeners.Teams.CreateTeamListener;
import el.team_application.Listeners.Teams.EditTeamListener;
import el.team_application.Listeners.Teams.GetMyTeamsListener;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.Teams.RemoveTeamListener;
import el.team_application.Listeners.UserAuth.AfterLoginCallback;
import el.team_application.Listeners.UserAuth.AfterRegisterCallback;
import el.team_application.Listeners.UserAuth.GetCurrentUserCallback;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class ModelPARSE {

    //region Team CRUD

    public void createTeam(Team team, CreateTeamListener createTeamListener){
        JSONArray membersArray  = new JSONArray();
        List<TeamMember> membersList = new LinkedList<>();
        for (TeamMember member : membersList){
            membersArray.put(member.getId());
        }

        ParseObject parseObject = new ParseObject("Team");
        parseObject.put("id",team.getId());
        parseObject.put("name",team.getName());
        parseObject.put("members",membersArray);
        parseObject.put("tasks", JSONObject.NULL);
        parseObject.saveInBackground();
    }

    public void removeTeam(String id, RemoveTeamListener removeTeamListener){

    }

    public void editTeam(Team team, EditTeamListener editTeamListener){

    }

    public void getMyTeams(String userId, final GetMyTeamsListener listener){
        ParseQuery<ParseObject> teamMembersInnerQuery = ParseQuery.getQuery("Member");
        teamMembersInnerQuery.whereEqualTo("userId",userId);
        ParseQuery<ParseObject> teamsQuery = ParseQuery.getQuery("Team");
        //teamsQuery.include("members");
        teamsQuery.whereMatchesQuery("members", teamMembersInnerQuery);

        teamsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e != null){
                    listener.onResult(null, e);
                }else if(list != null){
                    List<Team> teams = new LinkedList<Team>();
                    for (ParseObject obj : list){
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        Team team = new Team(id, name, null);
                        teams.add(team);
                    }
                    listener.onResult(teams,null);

                }else{
                    listener.onResult(null,new Exception("Teams Doesn't Exists"));
                }
            }
        });

        // Retrieve the most recent ones
//        query.orderByDescending("createdAt");
    }


    public void getTeamById(String id, GetTeamByIdListener getTeamByIdListener){

    }

    //endregion

    //region TeamMember CRUD

    public void addMember(TeamMember member, AddMemberListener addMemberListener){
        String role;
        if(member.getRole().equals(TeamMember.Role.MANAGER)){
            role = "MANAGER";
        }else{
            role = "EMPLOYEE";
        }
        ParseObject parseObject = new ParseObject("TeamMember");
        parseObject.put("role",role);
        parseObject.put("id",member.getId());
        parseObject.put("userId", member.getUserId());
        parseObject.put("joinDate",member.getJoinDate());
        parseObject.saveInBackground();
    }

    public void removeMember(String id, RemoveMemberListener removeMemberListener){

    }

    public void editMember(TeamMember member, EditMemberListener editMemberListener){

    }

    public void getMemberById(String id, GetMemberByIdListener getMemberByIdListener){

    }

    public void getAllTeamMembers(String teamId, GetAllTeamMembersListener getAllTeamMembersListener){

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

    // getting ParseUser Object and normalize it to our User Object
    private User normalizeParseUser(ParseUser user) {
        String email = user.getUsername(); // we use email as username
        String id = user.getString("id");
        String name = user.getString("name");
        String phone = user.getString("phone");
        User myUser = new User(id, email, name);
        myUser.setPhone(phone);
        return myUser;
    }

    public void getCurrentUser(final GetCurrentUserCallback callback){
        try{
            ParseUser user = ParseUser.getCurrentUser();
            callback.loggedInUser(normalizeParseUser(user));
        }catch (Exception e){
            callback.loggedInUser(null);
        }
    }

    public void login (String email, String password, final AfterLoginCallback callback){
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null && user != null) {
                    callback.loginSuccessful(normalizeParseUser(user));
                } else if (user == null) {
                    callback.usernameOrPasswordIsInvalid(e);
                } else {
                    callback.loginFailed(e);
                }
            }
        });
    }

    public void register (User user, String password, final AfterRegisterCallback registerCallback){
        ParseUser parseUser = new ParseUser();

        parseUser.setUsername(user.getEmail());
        parseUser.setPassword(password);
        parseUser.setEmail(user.getEmail());

        parseUser.put("id",user.getId());
        parseUser.put("name", user.getName());
        parseUser.put("phone", user.getId());

        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    registerCallback.registerSuccessful();
                } else {
                    registerCallback.registerFailed(e);
                }
            }
        });
    }

    public void logout(){
        ParseUser.getCurrentUser().logOutInBackground();
    }

    //endregion
}
