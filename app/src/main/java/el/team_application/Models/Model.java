package el.team_application.Models;

import android.content.Context;

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

public class Model {

    // our db models
    User loggedInUser;
    ModelPARSE modelPARSE = new ModelPARSE();
    ModelSQL modelSQL = new ModelSQL();

    // singleton Model instance - exists only once in the memory
    private final static Model instance = new Model();
    final static int VERSION = 0;

    public static Model getInstance() { return instance; }

    private Model() {
    }

    public void init(Context context){
        // for sql migration checks
    }

    //region Team CRUD

    public void createTeam(Team team, CreateTeamListener createTeamListener){

        modelPARSE.createTeam(team, createTeamListener);

    }

    public void removeTeam(String id, RemoveTeamListener removeTeamListener){
        modelPARSE.removeTeam(id, removeTeamListener);
    }

    public void editTeam(Team team, EditTeamListener editTeamListener){
        modelPARSE.editTeam(team,editTeamListener);
    }

    public void getMyTeams(String userId, GetMyTeamsListener listener){

        modelPARSE.getMyTeams(userId, listener);
    }

    public void getTeamById(String id, GetTeamByIdListener getTeamByIdListener){
        modelPARSE.getTeamById(id, getTeamByIdListener);
    }

    //endregion

    //region TeamMember CRUD

    public void addMember(TeamMember member, AddMemberListener addMemberListener){
        modelPARSE.addMember(member, addMemberListener);
    }

    public void removeMember(String id, RemoveMemberListener removeMemberListener){
        modelPARSE.removeMember(id, removeMemberListener);
    }

    public void editMember(TeamMember member, EditMemberListener editMemberListener){
        modelPARSE.editMember(member, editMemberListener);
    }

    public void getMemberById(String id, GetMemberByIdListener getMemberByIdListener){
        modelPARSE.getMemberById(id,getMemberByIdListener);
    }

    public void getAllTeamMembers(String teamId, GetAllTeamMembersListener getAllTeamMembersListener){
        modelPARSE.getAllTeamMembers(teamId, getAllTeamMembersListener);
    }

    public void getAllTaskMembers(){
        modelPARSE.getAllTaskMembers();
    }

    //endregion

    //region Task CRUD

    public void addTask(Task task, AddTaskListener addTaskListener){
        modelPARSE.addTask(task, addTaskListener);
    }

    public void deleteTask(){

    }

    public void editTask(Task task, EditTaskListener editTaskListener){
        modelPARSE.editTask(task, editTaskListener);
    }

    public void getTask(String taskId, GetTaskListener getTaskListener){
        modelPARSE.getTask(taskId, getTaskListener);
    }

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

    public void newInvite(Invitation invitation, NewInviteListener newInviteListener){
        modelPARSE.newInvite(invitation,newInviteListener);
    }

    public void acceptInvite(Invitation invitation, AcceptInviteListener acceptInviteListener){
        modelPARSE.acceptInvite(invitation, acceptInviteListener);
    }

    public void declineInvite(Invitation invitation, DeclineInviteListener declineInviteListener){
        modelPARSE.declineInvite(invitation, declineInviteListener);
    }

    public void getInvitationsForUser(String userId, GetInvitationsForUserListener getInvitationsListener){
        modelPARSE.getInvitationsForUser(userId, getInvitationsListener);
    }
    //endregion

    //region LoggedInUser

    public User getLoggedInUser(){
        return this.loggedInUser;
    }

    public void setLoggedInUser(User user){
        this.loggedInUser = user;
    }

    //endregion

    //region User & Authentication

    // get current logged in user (saved session)
    public void getSession(GetSessionCallback callback){
        modelPARSE.getSession(callback);
    }

    public void getUserByEmail(String email, GetUserByEmailCallback getUserByEmailCallback){
        modelPARSE.getUserByEmail(email, getUserByEmailCallback);
    }

    public void getUsersToAdd(String teamId, GetUsersToAddCallback getUsersToAddCallback){
        modelPARSE.getUsersToAdd(teamId, getUsersToAddCallback);
    }

    public void getTeamUsers(String teamId, final GetTeamUsersCallback getTeamUsersCallback){
        modelPARSE.getTeamUsers(teamId, getTeamUsersCallback);
    }

    public void joinUserToTeam(String userid, String teamId, JoinUserToTeamCallback joinUserToTeamCallback){
        modelPARSE.joinUserToTeam(userid, teamId, joinUserToTeamCallback);
    }

    // model login method, creating instance of a table if there isn't
    public void login(String email, String password, AfterLoginCallback loginCallback) {
        // if MODEL PARSE
        modelPARSE.login(email, password, loginCallback);

    }

    public void register(User user, String password, AfterRegisterCallback registerCallback) {
        // user cannot register if he don't have internet access
        modelPARSE.register(user, password, registerCallback);
    }

    public void logout(){
        this.loggedInUser = null;
        modelPARSE.logout();
    }

    //endregion
}