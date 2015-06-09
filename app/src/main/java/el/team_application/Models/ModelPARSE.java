package el.team_application.Models;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
import el.team_application.Listeners.UserAuth.GetSessionCallback;
import el.team_application.Listeners.UserAuth.GetUserByIdCallback;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class ModelPARSE {

    //region Team CRUD

    private JSONObject teamMemberToJsonObject(TeamMember member) {
        JSONObject object = new JSONObject();
        String role;
        if(member.getRole().equals(TeamMember.Role.MANAGER)){
            role = "MANAGER";
        }else{
            role = "EMPLOYEE";
        }
        try {
            object.put("role", role);
            object.put("memberId",member.getId());
            object.put("userId", member.getUserId());
            object.put("joinDate",member.getJoinDate());
            object.put("jobTitle", member.getJobTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void createTeam(Team team, CreateTeamListener createTeamListener){
        JSONArray membersArray  = new JSONArray();
        List<TeamMember> membersList = team.getMemberList();
        for (TeamMember member : membersList){
            JSONObject object = teamMemberToJsonObject(member);
            membersArray.put(object);
        }

        /* create new team */
        ParseObject parseObject = new ParseObject("Team");
        parseObject.put("teamId",team.getId());
        parseObject.put("name",team.getName());
        parseObject.put("members", membersArray);
        parseObject.put("tasks", JSONObject.NULL);
        parseObject.saveInBackground();

        /* add the new team to the user myTeam list */
        ParseUser loggedUser = ParseUser.getCurrentUser();
        JSONArray myArray = loggedUser.getJSONArray("myTeams");
        myArray.put(team.getId());
        loggedUser.put("myTeams",myArray);
        loggedUser.saveInBackground();

//        ParseQuery<ParseUser> query = new ParseQuery<>("User");
//        query.whereEqualTo("userId",loggedUser.getString("userId"));
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> list, ParseException e) {
//                ParseUser user = list.get(0);
//                user.put();
//            }
//        });
    }

    public void removeTeam(String id, RemoveTeamListener removeTeamListener){

    }

    public void editTeam(Team team, EditTeamListener editTeamListener){

    }

    public void getMyTeams(String userId, final GetMyTeamsListener listener){
        final ParseUser loggedUser = ParseUser.getCurrentUser();
        JSONArray teamsArr = loggedUser.getJSONArray("myTeams");
        List<String> teamsList = jsonStringArrayToList(teamsArr);

        ParseQuery<ParseObject> query = new ParseQuery("Team");
        query.whereContainedIn("teamId", teamsList);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<Team> myTeams = new LinkedList<Team>();
                for(ParseObject team : list){
                    // set team members
                    List<TeamMember> members = new LinkedList<TeamMember>();
                    JSONArray membersArr = team.getJSONArray("members");
                    for(int i=0; i<membersArr.length(); i++){
                        try {
                            JSONObject jsonMember = membersArr.getJSONObject(i);
                            TeamMember.Role role;
                            if(jsonMember.getString("role").equals("MANAGER")){
                                role = TeamMember.Role.MANAGER;
                            }else{
                                role = TeamMember.Role.EMPLOYEE;
                            }
                            TeamMember member = new TeamMember(
                                    jsonMember.getString("memberId"),
                                    loggedUser.getString("userId"),
                                    jsonMember.getString("joinDate"),
                                    jsonMember.getString("jobTitle"),
                                    role);
                            members.add(member);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    Team currTeam = new Team(
                            team.getString("teamId"),
                            team.getString("name"),
                            members);
                    myTeams.add(currTeam);
                }
                listener.onResult(myTeams,e);
            }
        });

    }


    public void getTeamById(String id, GetTeamByIdListener getTeamByIdListener){

    }

    //endregion

    //region TeamMember CRUD

    public void addMember(TeamMember member, AddMemberListener addMemberListener){
        ParseObject parseObject = teamMemberToParseObject(member);
        parseObject.saveInBackground();
    }


    private ParseObject teamMemberToParseObject(TeamMember member) {
        ParseObject parseObject = new ParseObject("TeamMember");
        String role;
        if(member.getRole().equals(TeamMember.Role.MANAGER)){
            role = "MANAGER";
        }else{
            role = "EMPLOYEE";
        }
        parseObject.put("role",role);
        parseObject.put("memberId",member.getId());
        parseObject.put("userId", member.getUserId());
        parseObject.put("joinDate",member.getJoinDate());
        return parseObject;
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
        String id = user.getString("userId");
        String name = user.getString("name");
        String phone = user.getString("phone");
        JSONArray teamsArr = user.getJSONArray("myTeams");

        User myUser = new User(id, email, name);

        List<String> teamsList = jsonStringArrayToList(teamsArr);

        myUser.setMyTeams(teamsList);
        myUser.setPhone(phone);

        return myUser;
    }

    private List<String> jsonStringArrayToList(JSONArray array) {
        List<String> list = new LinkedList<>();
        for (int i=0; i<array.length(); i++){
            try {
                list.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void getSession(final GetSessionCallback callback){
        callback.loggedInUser(null);
        try{
            ParseSession.getCurrentSessionInBackground(new GetCallback<ParseSession>() {
                @Override
                public void done(ParseSession parseSession, ParseException e) {
                    String token = parseSession.getSessionToken();
                    ParseUser user = parseSession.getParseUser(token);
                    callback.loggedInUser(normalizeParseUser(user));
                }
            });
        }catch (Exception e){
            callback.loggedInUser(null);
        }
    }

    public void getUserById(String id, final GetUserByIdCallback getUserByIdCallback){
        ParseQuery<ParseUser> query = new ParseQuery<ParseUser>("User");
        query.whereEqualTo("userId", id);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                ParseUser pUser = list.get(0);
                getUserByIdCallback.onResult(normalizeParseUser(pUser));
            }
        });
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

        parseUser.put("userId", user.getId());
        parseUser.put("name", user.getName());
        parseUser.put("phone", user.getPhone());
        parseUser.put("myTeams",new JSONArray());

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
