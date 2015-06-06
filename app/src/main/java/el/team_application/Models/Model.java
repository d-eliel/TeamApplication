package el.team_application.Models;

import android.content.Context;

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
//TEST COMMENT ELIEL ARIEL

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

    }

    public void editTeam(Team team, EditTeamListener editTeamListener){

    }

    public void getMyTeams(String userId, GetMyTeamsListener listener){
        modelPARSE.getMyTeams(userId, listener);
    }

    public void getTeamById(String id, GetTeamByIdListener getTeamByIdListener){

    }

    //endregion

    //region TeamMember CRUD

    public void addMember(TeamMember member, AddMemberListener addMemberListener){
        modelPARSE.addMember(member, addMemberListener);
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

    //region LoggedInUser

    public User getLoggedInUser(){
        return this.loggedInUser;
    }

    public void setLoggedInUser(User user){
        this.loggedInUser = user;
    }

    //endregion

    //region User Authentication

    // get current logged in user (saved session)
    public void getCurrentUser(GetCurrentUserCallback callback){
        modelPARSE.getCurrentUser(callback);
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
        modelPARSE.logout();
    }

    //endregion
}