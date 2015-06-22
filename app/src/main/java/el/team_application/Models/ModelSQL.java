package el.team_application.Models;

import el.team_application.ActivityViews.Activities.AddTeamMemberActivity;
import el.team_application.Listeners.Invitations.AcceptInviteListener;
import el.team_application.Listeners.Invitations.DeclineInviteListener;
import el.team_application.Listeners.Invitations.GetInvitationsForUserListener;
import el.team_application.Listeners.Invitations.NewInviteListener;
import el.team_application.Listeners.Members.AddMemberListener;
import el.team_application.Listeners.Members.EditMemberListener;
import el.team_application.Listeners.Members.GetAllTeamMembersListener;
import el.team_application.Listeners.Members.GetMemberByIdListener;
import el.team_application.Listeners.Members.GetMembersForTaskCallback;
import el.team_application.Listeners.Members.RemoveMemberListener;
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
import el.team_application.Listeners.User.GetUsersToAddCallback;
import el.team_application.Listeners.User.JoinUserToTeamCallback;
import el.team_application.Models.Entities.Invitation;
import el.team_application.Models.Entities.Member;
import el.team_application.Models.Entities.Session;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.Unsynced;
import el.team_application.Models.Entities.User;
import el.team_application.Models.QueriesSQL.JoinSQL;
import el.team_application.Models.QueriesSQL.MembersTaskSQL;
import el.team_application.Models.QueriesSQL.SessionSQL;
import el.team_application.Models.QueriesSQL.TaskSQL;
import el.team_application.Models.QueriesSQL.TeamMemberSQL;
import el.team_application.Models.QueriesSQL.TeamSQL;
import el.team_application.Models.QueriesSQL.UnsyncedSQL;
import el.team_application.Models.QueriesSQL.UserSQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */

public class ModelSQL implements InterfaceModel{

    Helper sqlDb;
    final static int VERSION = 4; // versions start from 1 and up every time we update the database

    @Override
    public void init(Context context){
        if (sqlDb == null){
            sqlDb = new Helper(context);
        }
    }

    @Override
    // empty SQL tables
    public void emptyTables(){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.deleteAll(db);
        TeamMemberSQL.deleteAll(db);
        TeamSQL.deleteAll(db);
        db.close();
    }

    //region Team CRUD

    @Override
    public void createTeam(Team team, CreateTeamListener createTeamListener){}

    @Override
    public void createTeamSync(Team team){
        // add team, members and tasks (each to match table)
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamSQL.add(db, team);
        for(TeamMember member : team.getMemberList()){
            addMember(member, null);
        }
        for(Task task : team.getTaskList()){
            addTask(task, null);
        }
        sqlDb.close();
    }

    @Override
    public void removeTeam(Team team, RemoveTeamListener removeTeamListener){
        // remove team, teamMembers and tasks for the team
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamSQL.delete(db, team.getId());
        for(TeamMember member : team.getMemberList()){
            removeMember(member, null);
        }
        for(Task task : team.getTaskList()){
            removeTask(task, null);
        }
        sqlDb.close();
    }

    @Override
    public void editTeam(Team team, final EditTeamListener editTeamListener){}

    @Override
    public void editTeamSync(Team team){
        // edit team, tasks for team and team members
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        boolean is = db.isDbLockedByCurrentThread();
        TeamSQL.edit(db, team);
        for(TeamMember member : team.getMemberList()){
            editMember(member, null);
        }
        for(Task task : team.getTaskList()){
            editTask(task, null);
        }
        sqlDb.close();
    }

    @Override
    public void getMyTeams(String userId, final GetMyTeamsListener listener){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        List<Team> teams = new LinkedList<>();
        List<Team> teamIdsAndNames = JoinSQL.getTeamIdsAndNamesForUser(db, userId);  // we got List of teams that have TeamId and TeamName only!
        if(teamIdsAndNames == null){
            listener.onResult(null,null);
            sqlDb.close();
            return;
        }
        // now we assign team-tasks and team-members to each team
        for(Team team : teamIdsAndNames){
            List<TeamMember> currentMembers = TeamMemberSQL.getTeamMembersForTeam(db, team.getId()); // members for team
            List<Task> currentTasks = TaskSQL.getTasksForTeam(db, team.getId());    // tasks for team - tasks without Creator object and membersList
            for (final Task task : currentTasks){
                getMemberById(task.getCreator().getId(), new GetMemberByIdListener() {
                    @Override
                    public void onResult(TeamMember member, Exception e) {
                        task.setCreator(member);
                    }
                });
                task.setMemberList(getTeamMembersByTaskId(task.getId()));
            }

            team.setMemberList(currentMembers);
            team.setTaskList(currentTasks);
            teams.add(team);
        }
//        return teams;
        listener.onResult(teams, null);
        sqlDb.close();
    }

    @Override
    public void getTeamById(String teamId, GetTeamByIdListener getTeamByIdListener){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        Team team = TeamSQL.getById(db, teamId);
        if(team == null){
            getTeamByIdListener.onResult(null,null);
            sqlDb.close();
            return;
        }
        List<Task> currentTasks = TaskSQL.getTasksForTeam(db, team.getId());
        for (Task task : currentTasks){
            task.setCreator(TeamMemberSQL.getTeamMemberById(db, task.getCreator().getId()));
            task.setMemberList(getTeamMembersByTaskId(task.getId()));
        }
        team.setTaskList(TaskSQL.getTasksForTeam(db, teamId));
        team.setMemberList(TeamMemberSQL.getTeamMembersForTeam(db, teamId));
        getTeamByIdListener.onResult(team, null);
        sqlDb.close();
    }


    @Override
    public Team getTeamByIdSync(String teamId){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        Team team = TeamSQL.getById(db, teamId);
        if(team == null){
            sqlDb.close();
            return null;
        }
        List<Task> currentTasks = TaskSQL.getTasksForTeam(db, team.getId());
        for (Task task : currentTasks){
            task.setCreator(TeamMemberSQL.getTeamMemberById(db, task.getCreator().getId()));
            task.setMemberList(getTeamMembersByTaskId(task.getId()));
        }
        team.setTaskList(TaskSQL.getTasksForTeam(db, teamId));
        team.setMemberList(TeamMemberSQL.getTeamMembersForTeam(db, teamId));
        sqlDb.close();
        return team;
    }
    //endregion


    //region TeamMember & Task Relation

    public List<String> getTeamMembersByTaskId(String taskId){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return MembersTaskSQL.getMembersByTaskId(db,taskId);
    }

    //endregion


    //region TeamMember CRUD

    // TODO : delete if not used
    public List<TeamMember> getAllTeamMembers(){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return TeamMemberSQL.getAllTeamMembers(db);
//        sqlDb.close();
    }

    @Override
    public void getMemberById(String memberId, GetMemberByIdListener getMemberByIdListener){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        getMemberByIdListener.onResult(TeamMemberSQL.getTeamMemberById(db, memberId), null);
        sqlDb.close();
    }

    @Override
    public void getMembersForTeam(String teamId, GetAllTeamMembersListener getAllTeamMembersListener) {
        // TODO : delete if not used
    }

    @Override
    public void addMember(TeamMember tm, AddMemberListener addMemberListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamMemberSQL.add(db, tm);
        sqlDb.close();
    }

    @Override
    public void removeMember(TeamMember member, RemoveMemberListener removeMemberListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamMemberSQL.delete(db, member.getId());
        sqlDb.close();
    }

    @Override
    public void editMember(TeamMember member, EditMemberListener editMemberListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamMemberSQL.edit(db, member);
        sqlDb.close();
    }

    @Override
    public void getMembersForTask(String taskId, GetMembersForTaskCallback getMembersForTaskCallback){
        final List<TeamMember> members = new LinkedList<>();
        List<String> memberIds = getTeamMembersByTaskId(taskId);
        if(memberIds == null){
            getMembersForTaskCallback.onResult(null, null);
            sqlDb.close();
            return;
        }
        for(final String id : memberIds){
            getMemberById(id, new GetMemberByIdListener() {
                @Override
                public void onResult(TeamMember member, Exception e) {
                    members.add(member);
                }
            });
        }
        getMembersForTaskCallback.onResult(members, null);
        sqlDb.close();
    }

    //endregion


    //region Task CRUD

    @Override
    public void addTask(Task task, AddTaskListener addTaskListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.add(db, task);
        sqlDb.close();
    }

    @Override
    public void removeTask(Task task, RemoveTaskListener removeTaskListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.delete(db, task.getId());
        sqlDb.close();
    }

    @Override
    public void editTask(Task task, EditTaskListener editTaskListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.edit(db, task);
        sqlDb.close();
    }

    @Override
    public void getTaskById(String taskId, GetTaskListener getTaskListener){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        getTaskListener.onResult(TaskSQL.getById(db, taskId), null);
        sqlDb.close();
    }

    @Override
    public void getTeamTasks(String teamId){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        TaskSQL.getTasksForTeam(db, teamId);
        sqlDb.close();
    }

    @Override
    public void getUserTasks(String userId){
        // TODO : delete if not used
    }

    @Override
    public void getTeamMemberTasks(String memberId){
        // TODO : delete if not used
    }

    //endregion


    // TODO
    //region Invitation Operations - not allowed locally in our application

    @Override
    public void newInvite(Invitation invitation, NewInviteListener newInviteListener) {

    }

    @Override
    public void acceptInvite(Invitation invitation, AcceptInviteListener acceptInviteListener) {

    }

    @Override
    public void declineInvite(Invitation invitation, DeclineInviteListener declineInviteListener) {

    }

    @Override
    public void getInvitationsForUser(String userId, GetInvitationsForUserListener getInvitationsListener) {

    }

    //endregion


    // TODO
    //region User Authentication

    @Override
    public void getSessionUser(String userId, String parseToken, GetSessionUserCallback callback){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        User user = UserSQL.getById(db, userId);
        if(user == null){
            callback.onResult(null);
            return;
        }
        List<Team> userTeams = JoinSQL.getTeamIdsAndNamesForUser(db, userId);
        List<String> myTeams = new LinkedList<String>();
        for(Team team : userTeams){
            myTeams.add(team.getId());
        }
        user.setMyTeams(myTeams);
        callback.onResult(user);
        sqlDb.close();
    }

    @Override
    // get current logged in user
    public void getSession(GetSessionCallback callback){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        callback.onResult(SessionSQL.getSession(db));
        sqlDb.close();
    }

    @Override
    public void setSession(Session session){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        SessionSQL.add(db, session);
        sqlDb.close();
    }

    @Override
    public void setSessionUser(User user){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        UserSQL.add(db, user);
        sqlDb.close();
    }

    @Override
    public void login(String email, String password, AfterLoginCallback loginCallback) {
        // performed on parse only
    }

    @Override
    public void logout(){
        // remove the session
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        SessionSQL.delete(db);
        sqlDb.close();
    }

    @Override
    public void register(User user, String password, AfterRegisterCallback registerCallback) {
        // unavailable locally
    }

    @Override
    public void joinUserToTeam(String userid, String teamId, JoinUserToTeamCallback joinUserToTeamCallback) {
        // unavailable locally
    }

    @Override
    public void getUserByEmail(String email, GetUserByEmailCallback getUserByEmailCallback) {
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        getUserByEmailCallback.onResult(UserSQL.getByEmail(db, email),null);
        sqlDb.close();
    }

    @Override
    public void getUsersToAdd(String teamId, GetUsersToAddCallback getUsersToAddCallback) {
        // TODO: delete if not used
    }

    @Override
    public void getTeamUsers(String teamId, GetTeamUsersCallback getTeamUsersCallback) {

    }

    //endregion


    //region SYNC

    @Override
    public void addUnsynced(Unsynced unsynced){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        UnsyncedSQL.add(db, null);
        sqlDb.close();
    }

    @Override
    public void removeUnsynced(String unsyncedId){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        UnsyncedSQL.remove(db, null);
        sqlDb.close();
    }

    @Override
    public List<Unsynced> getUnsynced(){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        List<Unsynced> list = UnsyncedSQL.getUnsyncedData(db);
        sqlDb.close();
        return list;
    }

    //endregion

    //region Helper Class
    class Helper extends SQLiteOpenHelper{
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            UnsyncedSQL.create(db);
            UserSQL.create(db);
            TeamSQL.create(db);
            TeamMemberSQL.create(db);
            TaskSQL.create(db);
            SessionSQL.create(db);
            Log.w("ModelSQL", "OnCreate DB");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            SessionSQL.drop (db);
            TaskSQL.drop(db);
            TeamMemberSQL.drop(db);
            TeamSQL.drop(db);
            UserSQL.drop(db);
            UnsyncedSQL.drop(db);
            onCreate(db);
        }
    }
    //endregion








}



