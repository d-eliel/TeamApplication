package el.team_application.Models;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import el.team_application.Listeners.Invitations.AcceptInviteListener;
import el.team_application.Listeners.Invitations.DeclineInviteListener;
import el.team_application.Listeners.Invitations.GetInvitationsForUserListener;
import el.team_application.Listeners.Invitations.NewInviteListener;
import el.team_application.Listeners.Members.AddMemberListener;
import el.team_application.Listeners.Members.EditMemberListener;
import el.team_application.Listeners.Members.GetAllTeamMembersListener;
import el.team_application.Listeners.Members.GetMemberByIdListener;
import el.team_application.Listeners.Members.RemoveMemberListener;
import el.team_application.Listeners.Tasks.AddTaskListener;
import el.team_application.Listeners.Tasks.EditTaskListener;
import el.team_application.Listeners.Tasks.GetTaskListener;
import el.team_application.Listeners.Teams.CreateTeamListener;
import el.team_application.Listeners.Teams.EditTeamListener;
import el.team_application.Listeners.Teams.GetMyTeamsListener;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.Teams.RemoveTeamListener;
import el.team_application.Listeners.User.AfterLoginCallback;
import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Listeners.User.GetUserByEmailCallback;
import el.team_application.Listeners.User.GetUserByIdCallback;
import el.team_application.Listeners.User.GetUsersToAddCallback;
import el.team_application.Listeners.User.JoinUserToTeamCallback;
import el.team_application.Models.Entities.Invitation;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;

/**
 * Created by ariel-mac on 24/05/2015.
 */
public class ModelPARSE {

    //region Team CRUD

   public void createTeam(Team team, CreateTeamListener createTeamListener){
        /* team to parse object */
        ParseObject parseObject = teamToParseObject(team);
        parseObject.saveInBackground();

        /* add the new team to the user's myTeams list */
        ParseUser loggedUser = ParseUser.getCurrentUser();
        JSONArray myArray = loggedUser.getJSONArray("myTeams");
        myArray.put(team.getId());
        loggedUser.put("myTeams",myArray);
        loggedUser.saveInBackground();
    }

    public void removeTeam(String id, RemoveTeamListener removeTeamListener){

    }

    /***
     * Get Team Object and update its - members, tasks and name
     * @param team
     * @param editTeamListener
     */
    public void editTeam(Team team, final EditTeamListener editTeamListener){
        final ParseObject newTeam = teamToParseObject(team);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Team");
        query.whereEqualTo("teamId", team.getId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.put("name", newTeam.get("name"));
                parseObject.put("members", newTeam.get("members"));
                parseObject.put("tasks", newTeam.get("tasks"));
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        editTeamListener.onResult(e);
                    }
                });
            }
        });
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
                for (ParseObject team : list) {
                    myTeams.add(parseObjectToTeam(team));
                }
                listener.onResult(myTeams, e);
            }
        });

    }

    public void getTeamById(String teamId, final GetTeamByIdListener getTeamByIdListener){
        ParseQuery<ParseObject> query = new ParseQuery("Team");
        query.whereEqualTo("teamId", teamId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject poTeam, ParseException e) {
                getTeamByIdListener.onResult(parseObjectToTeam(poTeam), e);
            }
        });
    }

    private ParseObject teamToParseObject (Team team){
        JSONArray membersArray  = new JSONArray();
        List<TeamMember> membersList = team.getMemberList();
        for (TeamMember member : membersList){
            JSONObject object = teamMemberToJsonObject(member);
            membersArray.put(object);
        }

        JSONArray tasksArray  = new JSONArray();
        List<Task> tasksList = team.getTaskList();
        for (Task task : tasksList){
            JSONObject object = taskToJsonObject(task);
            tasksArray.put(object);
        }

        /* new ParseObject-team */
        ParseObject parseObject = new ParseObject("Team");
        parseObject.put("teamId",team.getId());
        parseObject.put("name",team.getName());
        parseObject.put("members", membersArray);
        parseObject.put("tasks", tasksArray);
        return parseObject;
    }

    private Team parseObjectToTeam(ParseObject team) {
        // set team members
        List<TeamMember> members = new LinkedList<>();
        JSONArray membersArr = team.getJSONArray("members");
        for(int i=0; i<membersArr.length(); i++){
            try {
                members.add(jsonObjectToTeamMember(membersArr.getJSONObject(i)));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        // set team tasks
        List<Task> tasks = new LinkedList<>();
        JSONArray tasksArr = team.getJSONArray("tasks");
        for(int i=0; i<tasksArr.length(); i++){
            try {
                tasks.add(jsonObjectToTask(tasksArr.getJSONObject(i)));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        Team newTeam = new Team(
                team.getString("teamId"),
                team.getString("name"),
                members);
        newTeam.setTaskList(tasks);
        return newTeam;
    }

    private TeamMember jsonObjectToTeamMember(JSONObject jsonMember){
        TeamMember member = null;
        try {
            TeamMember.Role role;
            if(jsonMember.getString("role").equals("MANAGER")){
                role = TeamMember.Role.MANAGER;
            }else{
                role = TeamMember.Role.EMPLOYEE;
            }
            member = new TeamMember(
                    jsonMember.getString("memberId"),
                    jsonMember.getString("userId"),
                    jsonMember.getString("joinDate"),
                    jsonMember.getString("jobTitle"),
                    role);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return member;
    }

    private Task jsonObjectToTask(JSONObject jsonObject) {
        Task task = null;
        try{
            task = new Task(
                    jsonObject.getString("taskId"),
                    jsonObject.getString("startDate"),
                    jsonObjectToTeamMember(jsonObject.getJSONObject("creator")),
                    jsonObject.getString("name"));
            task.setEndDate(jsonObject.getString("endDate"));
            task.setAssociation(jsonObject.getString("association"));
            task.setDescription(jsonObject.getString("description"));
            task.setStatus(stringToTaskStatus(jsonObject.getString("status")));
            task.setMemberList(jsonStringArrayToList(jsonObject.getJSONArray("membersIds")));
            return task;
        } catch(JSONException e1){
            e1.printStackTrace();
        }
        return task;
    }

    //endregion


    //region TeamMember CRUD

    public void addMember(TeamMember member, final AddMemberListener addMemberListener){
        ParseObject parseObject = teamMemberToParseObject(member);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                addMemberListener.onResult(e);
            }
        });
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
        parseObject.put("jobTitle", member.getJobTitle());
        return parseObject;
    }

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

    public void removeMember(String id, RemoveMemberListener removeMemberListener){

    }

    public void editMember(TeamMember member, EditMemberListener editMemberListener){

    }

    public void getMemberById(String id, GetMemberByIdListener getMemberByIdListener){

    }

    public void getAllTeamMembers(String teamId, final GetAllTeamMembersListener getAllTeamMembersListener){
        ParseQuery<ParseObject> query = new ParseQuery("Team");
        query.whereEqualTo("teamId",teamId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<TeamMember> teamMembers = new LinkedList<TeamMember>();
                for(ParseObject member : list){
                    TeamMember.Role role;
                    String strRole = member.getString("role");
                    if(strRole.equals("MANAGER")){
                        role = TeamMember.Role.MANAGER;
                    }else{
                        role = TeamMember.Role.EMPLOYEE;
                    }
                    String id = member.getString("memberId");
                    String userId = member.getString("userId");
                    String joinDate = member.getString("joinDate");
                    String jobTitle = member.getString("jobTitle");
                    TeamMember teamMember = new TeamMember(id,userId,joinDate,jobTitle,role);
                    teamMembers.add(teamMember);
                }
                getAllTeamMembersListener.onResult(teamMembers,e);
            }
        });
    }

    public void getAllTaskMembers(){

    }

    //endregion

    //region Task CRUD

    public void addTask(Task task, final AddTaskListener addTaskListener ){
        ParseObject parseObject = taskToParseObject(task);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                addTaskListener.onResult(e);
            }
        });
    }

    private JSONObject taskToJsonObject(Task task) {
        JSONObject object = new JSONObject();
        String status = taskStatusToString(task.getStatus());
        try {
            object.put("taskId", task.getId());
            object.put("creator", teamMemberToJsonObject(task.getCreator()));
            object.put("name", task.getName());
            object.put("status",status);
            object.put("startDate", task.getStartDate());
            object.put("endDate", task.getEndDate());
            object.put("association", task.getAssociation());
            object.put("description", task.getDescription());

            // add members
            JSONArray membersIdArray  = new JSONArray();
            List<String> membersIdList = task.getMemberList();
            for (String memberId : membersIdList){
                membersIdArray.put(memberId);
            }
            object.put("membersIds",membersIdArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private ParseObject taskToParseObject(Task task) {
        ParseObject parseObject = new ParseObject("Task");
        String status = taskStatusToString(task.getStatus());
        parseObject.put("taskId", task.getId());
        parseObject.put("creator", teamMemberToJsonObject(task.getCreator()));
        parseObject.put("name", task.getName());
        parseObject.put("status",status);
        parseObject.put("startDate", task.getStartDate());
        parseObject.put("endDate", task.getEndDate());
        parseObject.put("association", task.getAssociation());
        parseObject.put("description", task.getDescription());

        //        // add subtasks
//        JSONArray tasksArray  = new JSONArray();
//        List<Task> subTasksList = task.getListOfSubTasks();
//        for (Task subTask : subTasksList){
//            JSONObject object = taskToJsonObject(subTask);
//            tasksArray.put(object);
//        }
//        parseObject.put("subTasks",tasksArray);

        /* add members */
        JSONArray membersIdArray  = new JSONArray();
        List<String> membersIdList = task.getMemberList();
        for (String memberId : membersIdList){
            membersIdArray.put(memberId);
        }
        parseObject.put("membersIds",membersIdArray);

        // object is ready
        return parseObject;
    }

    private String taskStatusToString(Task.Status status) {
        switch (status){
            case NOTHING:
                return "NOTHING";
            case STARTED:
                return "STARTED";
            case HALF:
                return "HALF";
            case ALMOST:
                return "ALMOST";
            case FINISH:
                return "FINISH";
            default:
                break;
        }
        return "NOTHING";
    }

    private Task.Status stringToTaskStatus(String status){
        switch (status){
            case "NOTHING":
                return Task.Status.NOTHING;
            case "STARTED":
                return Task.Status.STARTED;
            case "HALF":
                return Task.Status.HALF;
            case "ALMOST":
                return Task.Status.ALMOST;
            case "FINISH":
                return Task.Status.FINISH;
            default:
                break;
        }
        return Task.Status.NOTHING;
    }

    public void editTask(Task task, final EditTaskListener editTaskListener){

    }

    public void getTask(String taskId, final GetTaskListener getTaskListener){

    }

//    public void deleteTask(){
//
//    }
//
//    public void getTeamTasks(){
//
//    }
//
//    public void getUserTasks(){
//
//    }
//
//    public void getTeamMemberTasks(){
//
//    }

    //endregion

    //region Invitation

    public void newInvite (Invitation invitation, final NewInviteListener newInviteListener){
        ParseObject poInvite = new ParseObject("Invitation");
        poInvite.put("invitationId",invitation.getId());
        poInvite.put("inviteeId",invitation.getInviteeUserId());
        poInvite.put("inviteeTitle",invitation.getInviteeTitle());
        poInvite.put("inviterId",invitation.getInviterUserId());
        poInvite.put("inviterName",invitation.getInviterName());
        poInvite.put("inviterEmail",invitation.getInviterEmail());
        poInvite.put("teamId",invitation.getTeamId());
        poInvite.put("teamName",invitation.getTeamName());
        poInvite.put("accepted", JSONObject.NULL);
        poInvite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                newInviteListener.onResult(e);
            }
        });
    }

    /***
     * When user Accept invitation :
     * add the team id that he invited to to his myTeam list and
     * create new TeamMember object and add it to the team
     * @param invitation
     * @param acceptInviteListener
     */
    public void acceptInvite(final Invitation invitation, final AcceptInviteListener acceptInviteListener){
        joinUserToTeam(invitation.getInviteeUserId(), invitation.getTeamId(), new JoinUserToTeamCallback() {
            @Override
            public void onResult(User user, Exception e) {
                if (e != null) {
                    acceptInviteListener.onResult(e);
                    return;
                }

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Invitation");
                query.whereEqualTo("invitationId", invitation.getId());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e != null) {
                            acceptInviteListener.onResult(e);
                            return;
                        }
//                        parseObject.put("accepted", true);
                        parseObject.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                acceptInviteListener.onResult(e);
                            }
                        });
                    }
                });
            }
        });

        getTeamById(invitation.getTeamId(), new GetTeamByIdListener() {
            @Override
            public void onResult(Team team, Exception e) {
                if (e != null) {
                    acceptInviteListener.onResult(e);
                    return;
                }
                List<TeamMember> members = team.getMemberList();
                TeamMember newMember = new TeamMember(
                        UUID.randomUUID().toString(),
                        invitation.getInviteeUserId(),
                        new Date().toString(),
                        invitation.getInviteeTitle(),
                        TeamMember.Role.EMPLOYEE);
                members.add(newMember);
                team.setMemberList(members);
                editTeam(team, new EditTeamListener() {
                    @Override
                    public void onResult(Exception e) {
                        if (e != null) {
                            acceptInviteListener.onResult(e);
                            return;
                        }
                    }
                });
            }
        });

    }

    public void declineInvite(Invitation invitation, final DeclineInviteListener declineInviteListener){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Invitation");
        query.whereEqualTo("invitationId", invitation.getId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e != null){
                    declineInviteListener.onResult(e);
                    return;
                }
//                parseObject.put("accepted",false);
                parseObject.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        declineInviteListener.onResult(e);
                    }
                });
            }
        });
    }

    public void getInvitationsForUser(String userId, final GetInvitationsForUserListener getInvitationsListener){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Invitation");
        query.whereEqualTo("inviteeId", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e != null){
                    getInvitationsListener.onResult(null,e);
                    return;
                }
                List<Invitation> invitationsList = new LinkedList<Invitation>();
                for(ParseObject po : list){
                    String id = po.getString("invitationId");
                    String inviteeId = po.getString("inviteeId");
                    String inviteeTitle = po.getString("inviteeTitle");
                    String inviterId = po.getString("inviterId");
                    String inviterName = po.getString("inviterName");
                    String inviterEmail = po.getString("inviterEmail");
                    String teamId = po.getString("teamId");
                    String teamName = po.getString("teamName");
                    boolean accepted = po.getBoolean("accepted");

                    Invitation newInvitation = new Invitation(
                                                id,
                                                inviteeId,
                                                inviterId,
                                                inviterEmail,
                                                inviterName,
                                                teamId,
                                                teamName,
                                                inviteeTitle,
                                                accepted);

                    invitationsList.add(newInvitation);
                }
                getInvitationsListener.onResult(invitationsList, e);
            }
        });
    }


    //endregion

    //region User & Authentication

    /***
     * Get ParseUser Object and normalize it to our User Object
     * @param user
     * @return
     */
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

    /***
     * Change JsonArray<String> to List<String>
     * @param array
     * @return
     */
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

    /***
     * Return the current user that login to parse
     * @param callback
     */
    public void getSession(final GetSessionCallback callback){
        ParseUser user = ParseUser.getCurrentUser();
        if(user == null){
            callback.loggedInUser(null);
        }else{
            callback.loggedInUser(normalizeParseUser(user));
        }
    }

    /***
     * Get single user object by email
     * @param email
     * @param getUserByEmailCallback
     */
    public void getUserByEmail(String email, final GetUserByEmailCallback getUserByEmailCallback){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                getUserByEmailCallback.onResult(normalizeParseUser(parseUser),e);
            }
        });
    }

    /***
     * Get all the users that are not in the given team - so we can join them.
     * The opposite from {@link this#getTeamUsers }
     * @param teamId
     * @param getUsersToAddCallback
     */
    public void getUsersToAdd(String teamId, final GetUsersToAddCallback getUsersToAddCallback) {
        ParseQuery query = ParseUser.getQuery();
        query.whereNotEqualTo("myTeams", teamId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                List<User> users = new LinkedList<User>();
                for (ParseUser pu : list) {
                    users.add(normalizeParseUser(pu));
                }
                getUsersToAddCallback.onResult(users, e);
            }
        });
    }

    /***
     * Get User Objects of the given team members.
     * The opposite from {@link this#getUsersToAdd}
     * @param teamId
     * @param getTeamUsersCallback
     */
    public void getTeamUsers(String teamId, final GetTeamUsersCallback getTeamUsersCallback){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("myTeams", teamId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                List<User> users = new LinkedList<User>();
                for (ParseUser pu : list) {
                    users.add(normalizeParseUser(pu));
                }
                getTeamUsersCallback.onResult(users, e);
            }
        });
    }

    /***
     * Add the team-id to the user's myTeams Array
     * @param teamId
     * @param userId
     * @param joinUserToTeamCallback
     */
    public void joinUserToTeam(String userId, final String teamId, final JoinUserToTeamCallback joinUserToTeamCallback){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("userId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (e != null) {
                    joinUserToTeamCallback.onResult(null, e);
                    return;
                }
                JSONArray myteams = parseUser.getJSONArray("myTeams");
                myteams.put(teamId);
                parseUser.put("myTeams", myteams);
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        joinUserToTeamCallback.onResult(normalizeParseUser(parseUser), e);
                    }
                });
            }
        });
    }

    /***
     * Login to parse by email and password
     * @param email
     * @param password
     * @param callback
     */
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

    /***
     * Register new user
     * @param user
     * @param password
     * @param registerCallback
     */
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

    /***
     * Logout from parse
     */
    public void logout(){
        //ParseUser.getCurrentUser().logOutInBackground();
        ParseUser.logOutInBackground();
    }

    //endregion
}
