package el.team_application.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
import el.team_application.Listeners.ModelInitListener;
import el.team_application.Listeners.Tasks.AddTaskListener;
import el.team_application.Listeners.Tasks.EditTaskListener;
import el.team_application.Listeners.Tasks.GetTaskListener;
import el.team_application.Listeners.Tasks.RemoveTaskListener;
import el.team_application.Listeners.Teams.CreateTeamListener;
import el.team_application.Listeners.Teams.EditTeamListener;
import el.team_application.Listeners.Teams.GetMyTeamsListener;
import el.team_application.Listeners.Teams.GetTeamByIdListener;
import el.team_application.Listeners.Teams.RemoveTeamListener;
import el.team_application.Listeners.User.AfterLoginCallback;
import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Listeners.User.GetSessionUserCallback;
import el.team_application.Listeners.User.GetTeamUsersCallback;
import el.team_application.Listeners.User.GetUserByEmailCallback;
import el.team_application.Listeners.User.GetUserByIdCallback;
import el.team_application.Listeners.User.GetUsersToAddCallback;
import el.team_application.Listeners.User.JoinUserToTeamCallback;
import el.team_application.Models.Entities.Invitation;
import el.team_application.Models.Entities.Session;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.Unsynced;
import el.team_application.Models.Entities.User;

/**
 * Created by ariel-mac on 24/05/2015.
 */

public class Model {
    boolean isLocal;
    User loggedInUser;
    Context myContext;
    ModelSQL modelSQL       = new ModelSQL();
    ModelPARSE modelPARSE   = new ModelPARSE();

    List<Team> myTeams = new LinkedList<Team>();
    Session mySession;

    // singleton Model instance - exists only once in the memory
    private final static Model instance = new Model();
    final static int VERSION = 1;

    public static Model getInstance() { return instance; }

    private Model() { }

    public void init(Context context, final ModelInitListener modelInitListener){
        // for sql migration checks
        myContext = context;
        modelSQL.init(context);
        modelPARSE.init(context);

        isLocal = !isActiveNetwork(); // boolean for network flag

        modelSQL.getSession(new GetSessionCallback() {
            @Override
            public void onResult(Session session) {
                if(session != null){
                    if(isLocal){ // no internet
                        modelSQL.getSessionUser(session.getUserId(), session.getToken(), new GetSessionUserCallback() {
                            @Override
                            public void onResult(User user) {
                                modelInitListener.onResult(user);
                                loggedInUser = user;
                                return;
                            }
                        });
                    }else { // internet access
                        modelPARSE.getSessionUser(session.getUserId(), session.getToken(), new GetSessionUserCallback() {
                            @Override
                            public void onResult(User user) {
                                modelInitListener.onResult(user);
                                loggedInUser = user;
                                syncLocalToParse();
                                getMyDataFromParse();
                                return;
                            }
                        });
                    }
                }else{
                    // no session - login / register is required
                    // -> handle this in the next step
                    modelInitListener.onResult(null);
                }
            }
        });
    }


    //region Internet Detection
    public boolean isActiveNetwork() {
        ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }
    //endregion


    //region Sync Local Data & Parse Data

    public void syncLocalToParse(){
        List<Unsynced> data = modelSQL.getUnsynced();
        if(data == null){
            return;
        }
        for (Unsynced row : data){

            // Action == CREATE
            if(row.getAction() == Unsynced.Action.CREATE){

                switch(row.getTable()){

                    // CREATE MEMBER
                    case MEMBER:
                        modelSQL.getMemberById(row.getChangedObjectId(), new GetMemberByIdListener() {
                            @Override
                            public void onResult(TeamMember member, Exception e) {
                                modelPARSE.addMember(member, new AddMemberListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace(); // TODO debug
                                    }
                                });
                            }
                        });
                        break;

                    // CREATE TASK
                    case TASK:
                        modelSQL.getTaskById(row.getChangedObjectId(), new GetTaskListener() {
                            @Override
                            public void onResult(Task task, Exception e) {
                                modelPARSE.addTask(task, new AddTaskListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace(); // TODO debug
                                    }
                                });
                            }
                        });
                        break;

                    // CREATE TEAM
                    case TEAM:
                        modelSQL.getTeamById(row.getChangedObjectId(), new GetTeamByIdListener() {
                            @Override
                            public void onResult(Team team, Exception e) {
                                modelPARSE.createTeam(team, new CreateTeamListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace(); // TODO debug
                                    }
                                });
                            }
                        });
                        break;
                }

            // Action == EDIT
            }else if(row.getAction() == Unsynced.Action.EDIT){

                switch(row.getTable()){

                    // EDIT MEMBER
                    case MEMBER:
                        modelSQL.getMemberById(row.getChangedObjectId(), new GetMemberByIdListener() {
                            @Override
                            public void onResult(TeamMember member, Exception e) {
                                modelPARSE.editMember(member, new EditMemberListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                        break;

                    // EDIT TASK
                    case TASK:
                        modelSQL.getTaskById(row.getChangedObjectId(), new GetTaskListener() {
                            @Override
                            public void onResult(Task task, Exception e) {
                                modelPARSE.editTask(task, new EditTaskListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                        break;

                    // EDIT TEAM
                    case TEAM:
                        modelSQL.getTeamById(row.getChangedObjectId(), new GetTeamByIdListener() {
                            @Override
                            public void onResult(Team team, Exception e) {
                                modelPARSE.editTeam(team, new EditTeamListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                        break;
                }


            // Action == REMOVE
            }else if(row.getAction() == Unsynced.Action.REMOVE){

                switch(row.getTable()){
                    // EDIT MEMBER
                    case MEMBER:
                        modelSQL.getMemberById(row.getChangedObjectId(), new GetMemberByIdListener() {
                            @Override
                            public void onResult(TeamMember member, Exception e) {
                                modelPARSE.removeMember(member, new RemoveMemberListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                        break;

                    // EDIT TASK
                    case TASK:
                        modelSQL.getTaskById(row.getChangedObjectId(), new GetTaskListener() {
                            @Override
                            public void onResult(Task task, Exception e) {
                                modelPARSE.removeTask(task, new RemoveTaskListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                        break;

                    // EDIT TEAM
                    case TEAM:
                        modelSQL.getTeamById(row.getChangedObjectId(), new GetTeamByIdListener() {
                            @Override
                            public void onResult(Team team, Exception e) {
                                modelPARSE.removeTeam(team, new RemoveTeamListener() {
                                    @Override
                                    public void onResult(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }
    }

    public void getMyDataFromParse(){
        modelPARSE.getMyTeams(loggedInUser.getId(), new GetMyTeamsListener() {
            @Override
            public void onResult(List<Team> myTeams, Exception e) {
                updateLocalFromParse(myTeams);
            }
        });
    }

    public void updateLocalFromParse(List<Team> myTeams){
        for(final Team team : myTeams){
            modelSQL.createTeamSync(team);
        }
    }

    //endregion


    //region Session User - LoggedInUser

    public User getLoggedInUser(){
        return this.loggedInUser;
    }

    public void setLoggedInUser(User user){
        this.loggedInUser = user;
    }

    //endregion


    //region Team CRUD

    public void createTeam(Team team, CreateTeamListener createTeamListener){
        if(isLocal){
            modelSQL.createTeam(team, null);
            modelSQL.addUnsynced(new Unsynced(UUID.randomUUID().toString(), Unsynced.Action.CREATE, Unsynced.Table.TEAM, team.getId()));
        }else{
            modelPARSE.createTeam(team, createTeamListener);
            modelSQL.createTeam(team, null);
        }
    }

    public void removeTeam(Team team, RemoveTeamListener removeTeamListener){
        if(isLocal){
            modelSQL.removeTeam(team, null);
            modelSQL.addUnsynced(new Unsynced(UUID.randomUUID().toString(), Unsynced.Action.REMOVE, Unsynced.Table.TEAM, team.getId()));
        }else{
            modelPARSE.removeTeam(team, removeTeamListener);
            modelSQL.removeTeam(team, null);
        }
    }

    public void editTeam(Team team, EditTeamListener editTeamListener){
        if(isLocal){
            modelSQL.editTeam(team, null);
            modelSQL.addUnsynced(new Unsynced(UUID.randomUUID().toString(), Unsynced.Action.EDIT, Unsynced.Table.TEAM, team.getId()));
        }else{
            modelPARSE.editTeam(team, editTeamListener);
            modelSQL.editTeam(team, null);
        }
    }

    public void getMyTeams(String userId, GetMyTeamsListener listener){
        if(isLocal){
            modelSQL.getMyTeams(userId, listener);
        }else{
            modelPARSE.getMyTeams(userId, listener);
        }
    }

    public void getTeamById(String teamId, GetTeamByIdListener getTeamByIdListener){
        if(isLocal){
            modelSQL.getTeamById(teamId, getTeamByIdListener);
        }else{
            modelPARSE.getTeamById(teamId, getTeamByIdListener);
        }
    }

    //endregion


    //region TeamMember CRUD

    public void addMember(TeamMember member, AddMemberListener addMemberListener){
        modelPARSE.addMember(member, addMemberListener);
    }

    public void removeMember(TeamMember member, RemoveMemberListener removeMemberListener){
        modelPARSE.removeMember(member, removeMemberListener);
    }

    public void editMember(TeamMember member, EditMemberListener editMemberListener){
        modelPARSE.editMember(member, editMemberListener);
    }

    public void getMemberById(String id, GetMemberByIdListener getMemberByIdListener){
        modelPARSE.getMemberById(id, getMemberByIdListener);
    }

    public void getMembersForTeam(String teamId, GetAllTeamMembersListener getAllTeamMembersListener){
        modelPARSE.getMembersForTeam(teamId, getAllTeamMembersListener);
    }

    //endregion


    //region Task CRUD

    public void addTask(Task task, AddTaskListener addTaskListener){
        modelPARSE.addTask(task, addTaskListener);
    }

    public void removeTask(){
        // TODO

    }

    public void editTask(Task task, EditTaskListener editTaskListener){
        modelPARSE.editTask(task, editTaskListener);
    }

    public void getTaskById(String taskId, GetTaskListener getTaskListener){
        modelPARSE.getTaskById(taskId, getTaskListener);
    }

    public void getTeamTasks(){
        // TODO

    }

    public void getUserTasks(){
        // TODO

    }

    public void getTeamMemberTasks(){
        // TODO

    }

    //endregion


    //region Invitation Operations

    public void newInvite(Invitation invitation, NewInviteListener newInviteListener){
        if(!isLocal){
            newInviteListener.onResult(new Exception("No network available"));
        }else{
            modelPARSE.newInvite(invitation,newInviteListener);
        }
    }

    public void acceptInvite(Invitation invitation, AcceptInviteListener acceptInviteListener){
        if(!isLocal){
            acceptInviteListener.onResult(new Exception("No network available"));
        }else{
            modelPARSE.acceptInvite(invitation, acceptInviteListener);
        }
    }

    public void declineInvite(Invitation invitation, DeclineInviteListener declineInviteListener){
        if(!isLocal){
            declineInviteListener.onResult(new Exception("No network available"));
        }else{
            modelPARSE.declineInvite(invitation, declineInviteListener);
        }
    }

    public void getInvitationsForUser(String userId, GetInvitationsForUserListener getInvitationsListener){
        if(!isLocal){
            getInvitationsListener.onResult(null,new Exception("No network available"));
        }else{
            modelPARSE.getInvitationsForUser(userId, getInvitationsListener);
        }
    }

    //endregion


    //region Users & Authentication


    public void getSessionUser(String userId, String token, GetSessionUserCallback getSessionUserCallback){
        if(isLocal){
            modelSQL.getSessionUser(userId, token, getSessionUserCallback);
        }else {
            modelPARSE.getSessionUser(userId, token, getSessionUserCallback);
        }
    }

    // get current logged in user (saved session)
    public void getSession(final GetSessionCallback callback){
        modelSQL.getSession(new GetSessionCallback() {
            @Override
            public void onResult(Session session) {
                callback.onResult(session);
            }
        });
    }

    public void getUserByEmail(String email, GetUserByEmailCallback getUserByEmailCallback){
      if(isLocal){
          modelSQL.getUserByEmail(email,getUserByEmailCallback);
      }else{
          modelPARSE.getUserByEmail(email, getUserByEmailCallback);
      }
    }

//    public void getUsersToAdd(String teamId, GetUsersToAddCallback getUsersToAddCallback){
//        modelPARSE.getUsersToAdd(teamId, getUsersToAddCallback);
//    }

    public void getTeamUsers(String teamId, final GetTeamUsersCallback getTeamUsersCallback){
        if(isLocal){
            modelSQL.getTeamUsers(teamId,getTeamUsersCallback);
        }
        else{
            modelPARSE.getTeamUsers(teamId, getTeamUsersCallback);
        }
    }

    public void joinUserToTeam(String userid, String teamId, JoinUserToTeamCallback joinUserToTeamCallback){
        if(isLocal){
            modelSQL.joinUserToTeam(userid,teamId,joinUserToTeamCallback);
        }else {
            modelSQL.joinUserToTeam(userid,teamId,joinUserToTeamCallback);
            modelPARSE.joinUserToTeam(userid, teamId, joinUserToTeamCallback);
        }
    }

    // model login method, creating instance of a table if there isn't
    public void login(String email, String password, final AfterLoginCallback loginCallback) {
        if(isLocal){
            loginCallback.loginFailed(new Exception("Cannot Login when you offline"));
            return;
        }
        modelPARSE.login(email, password, new AfterLoginCallback() {
            @Override
            public void loginSuccessful(final User user) {
                modelPARSE.getSession(new GetSessionCallback() {
                    @Override
                    public void onResult(Session session) {
                        loginCallback.loginSuccessful(user);
                        modelSQL.setSession(session);
                        modelSQL.emptyTables();
                        getMyDataFromParse();
                        modelSQL.setSessionUser(user); // set local session
                    }
                });
            }
            @Override
            public void loginFailed(Exception e) {
                loginCallback.loginFailed(e);
            }

            @Override
            public void usernameOrPasswordIsInvalid(Exception e) {
                loginCallback.usernameOrPasswordIsInvalid(e);
            }
        });
    }

    public void register(final User user, String password, final AfterRegisterCallback registerCallback) {
        // user cannot register if he don't have internet access
        if(isLocal){
            registerCallback.registerFailed(new Exception("Register is not possible when offline"));
            return;
        }
        modelPARSE.register(user, password, new AfterRegisterCallback() {
            @Override
            public void registerSuccessful(final User user) {
                modelPARSE.getSession(new GetSessionCallback() {
                    @Override
                    public void onResult(Session session) {
                        registerCallback.registerSuccessful(user);
                        modelSQL.setSession(session);
                        modelSQL.emptyTables();
                        getMyDataFromParse();
                        modelSQL.setSessionUser(user); // set local session
                    }
                });
            }

            @Override
            public void registerFailed(Exception e) {
                registerCallback.registerFailed(e);
            }
        });

    }

    public void logout(){
        this.loggedInUser = null;
        modelSQL.logout();
//        modelPARSE.logout();
    }

    //endregion

}