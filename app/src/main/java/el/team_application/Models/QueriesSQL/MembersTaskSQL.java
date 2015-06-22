package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import el.team_application.Models.Entities.Team;

/**
 * Created by Eliel on 6/20/2015.
 */
public class MembersTaskSQL {
     /*
        provide access to Relationship TeamMembers & Tasks - Table in SQLite - CRUD
     */

    final static String MEMBERS_TASK_TABLE              = "membersTask";
    final static String MEMBERS_TASK_TABLE_TASK_ID      = "_taskId";
    final static String MEMBERS_TASK_TABLE_MEMBER_ID    = "_memberId";

    static public void create(SQLiteDatabase db){
        db.execSQL("create table " + MEMBERS_TASK_TABLE + " (" +
                MEMBERS_TASK_TABLE_MEMBER_ID + " TEXT," +
                MEMBERS_TASK_TABLE_TASK_ID + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + MEMBERS_TASK_TABLE + ";");
    }

    /***
     * return List<String>
     * @param db
     * @param taskId
     * @return
     */
    public static List<String> getMembersByTaskId(SQLiteDatabase db, String taskId) {
        String where = MEMBERS_TASK_TABLE_TASK_ID + " = ?";
        String[] args = {taskId};
        Cursor cursor = db.query(MEMBERS_TASK_TABLE, null, where, args, null, null, null);

        List<String> membersIds = new LinkedList<String>();
        if (cursor.moveToFirst()) {
            int memberIdIndex = cursor.getColumnIndex(MEMBERS_TASK_TABLE_MEMBER_ID);
            do{
                String memberId = cursor.getString(memberIdIndex);
                membersIds.add(memberId);
            } while (cursor.moveToNext());
        }
        return membersIds;
    }

    public static void add(SQLiteDatabase db, String taskId, String memberId) {
        ContentValues values = new ContentValues();
        values.put(MEMBERS_TASK_TABLE_TASK_ID, taskId);
        values.put(MEMBERS_TASK_TABLE_MEMBER_ID, memberId);
        db.insert(MEMBERS_TASK_TABLE, MEMBERS_TASK_TABLE_TASK_ID, values);
    }

    public static void deleteTaskRows(SQLiteDatabase db, String taskId){
        String where = MEMBERS_TASK_TABLE_TASK_ID + " = " + taskId;
        db.delete(MEMBERS_TASK_TABLE, where, null);
    }

    public static void deleteMemberRows(SQLiteDatabase db, String memberId){
        String where = MEMBERS_TASK_TABLE_MEMBER_ID + " = " + memberId;
        db.delete(MEMBERS_TASK_TABLE, where, null);
    }

    static class MembersTaskRelation{
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
