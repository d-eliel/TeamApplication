package el.team_application.Models;

import el.team_application.Listeners.User.AfterLoginCallback;
import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Models.Entities.Member;
import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.QueriesSQL.TaskSQL;
import el.team_application.Models.QueriesSQL.TeamMemberSQL;
import el.team_application.Models.QueriesSQL.TeamSQL;
import el.team_application.Models.QueriesSQL.UserSQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by ariel-mac on 24/05/2015.
 */

public class ModelSQL {

    Helper sqlDb;
    final static int VERSION = 1; //versions start from 1 and up

    public void init(Context context){
        if (sqlDb == null){
            sqlDb = new Helper(context);
        }
    }

    //region Team CRUD

    public void createTeam(Team team){

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

    public List<TeamMember> getAllTeamMembers(){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return TeamMemberSQL.getAllTeamMembers(db);
    }

    public TeamMember getTeamMemberById(String id){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return TeamMemberSQL.getTeamMemberById(db, id);
    }

    public void addTeamMember(TeamMember tm){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamMemberSQL.add(db,tm);
    }
//rmv
    public void removeMember(String id){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamMemberSQL.delete(db,id);

    }

    public void editMember(){

    }

    public void getMember(){

    }

    public void getAllTaskMembers(){

    }

    //endregion

    //region Task CRUD

    public void addTask(Task task){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.add(db, task);
    }

    public void deleteTask(Task task){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.delete(db, task.getId());
    }

    public void editTask(Task task){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TaskSQL.edit(db, task);
    }

    public Task getTask(String taskId){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        return TaskSQL.getById(db, taskId);
    }

    public void getTeamTasks(String teamId){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        TaskSQL.getTeamTasks(db, teamId);
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


    class Helper extends SQLiteOpenHelper{
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            UserSQL.create(db);
            TeamSQL.create(db);
            TeamMemberSQL.create(db);
            TaskSQL.create(db);
            //Log.w("ModelSQL", "OnCreate DB");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            TaskSQL.drop(db);
            TeamMemberSQL.drop(db);
            TeamSQL.drop(db);
            UserSQL.drop(db);
            onCreate(db);
        }
    }
}



