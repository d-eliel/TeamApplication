package el.team_application.Models;

import el.team_application.Listeners.User.AfterLoginCallback;
import el.team_application.Listeners.User.AfterRegisterCallback;
import el.team_application.Listeners.User.GetSessionCallback;
import el.team_application.Models.Entities.Member;
import el.team_application.Models.Entities.TeamMember;
import el.team_application.Models.Entities.User;
import el.team_application.Models.QueriesSQL.TeamMemberSQL;

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

    //region Team CRUD

    public void init(Context context){
        if (sqlDb == null){
            sqlDb = new Helper(context);
        }
    }

    public List<TeamMember> getAllTeamMembers(){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return TeamMemberSQL.getAllTeamMembers(db);
    }

    public TeamMember getTeamMemberById(String id){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return TeamMemberSQL.getTeamMemberById(db, id);
    }

    public void add(TeamMember tm){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        TeamMemberSQL.add(db,tm);
    }


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


    class Helper extends SQLiteOpenHelper{
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            TeamMemberSQL.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            TeamMemberSQL.drop(db);
            onCreate(db);
        }
    }
}


}
