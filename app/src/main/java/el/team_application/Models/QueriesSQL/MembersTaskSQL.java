package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import el.team_application.Models.Entities.Team;

/**
 * Created by Eliel on 6/20/2015.
 */
public class MembersTaskSQL {
     /*
        provide access to Relationship TeamMembers & Tasks - Table in SQLite - CRUD
     */

    final static String MEMBERS_TASK_TABLE              = "membersTask";
    final static String MEMBERS_TASK_TABLE_MEMBER_ID    = "_memberId";
    final static String MEMBERS_TASK_TABLE_TASK_ID      = "_taskId";

    static public void create(SQLiteDatabase db){
        db.execSQL("create table " + MEMBERS_TASK_TABLE + " (" +
                MEMBERS_TASK_TABLE_MEMBER_ID + " TEXT PRIMARY KEY," +
                MEMBERS_TASK_TABLE_TASK_ID + " TEXT PRIMARY KEY);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + MEMBERS_TASK_TABLE + ";");
    }

//    public static MembersTaskRelation getByMemberId (SQLiteDatabase db, String id1, String id2) {
//        String where = MEMBERS_TASK_TABLE_ID1 + " = ? AND " + MEMBERS_TASK_TABLE_ID2 + " = ?";
//        String[] args = {id1,id2};
//        Cursor cursor = db.query(MEMBERS_TASK_TABLE, null, where, args, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            int id1Index = cursor.getColumnIndex(MEMBERS_TASK_TABLE_ID1);
//            int id2Index = cursor.getColumnIndex(MEMBERS_TASK_TABLE_ID2);
//
//            String _id1 = cursor.getString(id1Index);
//            String _id2 = cursor.getString(id2Index);
//
//            ;
//            return team;
//        }
//        return null;
//    }
//
//    public static void add(SQLiteDatabase db, Team team) {
//        ContentValues values = new ContentValues();
//        values.put(TEAM_TABLE_ID, team.getId());
//        values.put(TEAM_TABLE_NAME, team.getName());
//        db.insert(TEAM_TABLE, TEAM_TABLE_ID, values);
//    }
//
//    public static void edit(SQLiteDatabase db, Team team){
//        String where = TEAM_TABLE_ID + " = " + team.getId();
//        ContentValues values = new ContentValues();
//        values.put(TEAM_TABLE_ID, team.getId());
//        values.put(TEAM_TABLE_NAME, team.getName());
//        db.update(TEAM_TABLE, values, where, null);
//    }
//
//    public static void delete(SQLiteDatabase db, String teamId){
//        String where = TEAM_TABLE_ID + " = " + teamId;
//        db.delete(TEAM_TABLE, where, null);
//    }



    class MembersTaskRelation{
        String taskId;
        String teamMemberId;

        public MembersTaskRelation(String taskId, String teamMemberId) {
            this.taskId = taskId;
            this.teamMemberId = teamMemberId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTeamMemberId() {
            return teamMemberId;
        }

        public void setTeamMemberId(String teamMemberId) {
            this.teamMemberId = teamMemberId;
        }
    }
}
