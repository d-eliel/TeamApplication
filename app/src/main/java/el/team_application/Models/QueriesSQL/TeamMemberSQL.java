package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.LinkedList;
import java.util.List;

import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/4/2015.
 */

// public TeamMember(String id, String userId, String joinDate, String jobTitle, Role role)

public class TeamMemberSQL {
    //provide access to TeamMember Table in SQLite - CRUD
    final static String TEAM_MEMBER_TABLE           = "teamMember"; //table name
    final static String TEAM_MEMBER_TABLE_ID        = "_teamMemberId";
    final static String TEAM_MEMBER_TABLE_USER_ID   = "userId";
    final static String TEAM_MEMBER_TABLE_TEAM_ID   = "teamId";
    final static String TEAM_MEMBER_TABLE_JOIN_DATE = "joinDate";
    final static String TEAM_MEMBER_TABLE_JOB_TITLE = "jobTitle";
    final static String TEAM_MEMBER_TABLE_ROLE      = "role";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TEAM_MEMBER_TABLE + " (" +
                        TEAM_MEMBER_TABLE_ID + " TEXT," +
                        TEAM_MEMBER_TABLE_USER_ID + " TEXT," +
                        TEAM_MEMBER_TABLE_JOIN_DATE + " TEXT," +
                        TEAM_MEMBER_TABLE_JOB_TITLE + " TEXT," +
                        TEAM_MEMBER_TABLE_TEAM_ID + " TEXT," +
                        TEAM_MEMBER_TABLE_ROLE + " TEXT);"
        );
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TEAM_MEMBER_TABLE + ";");
    }

    public static List<TeamMember> getTeamMembersForTeam(SQLiteDatabase db, String teamId){
        String where = TEAM_MEMBER_TABLE_TEAM_ID + " = ?";
        String[] args = {teamId};
        Cursor cursor = db.query(TEAM_MEMBER_TABLE, null, where, args, null, null, null);

        List<TeamMember> members = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_ID);
            int userIdIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_USER_ID);
            int titleIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_JOB_TITLE);
            int joinDateIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_JOIN_DATE);
            int roleIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_ROLE);

            do {
                String id = cursor.getString(idIndex);
                String userId = cursor.getString(userIdIndex);
                String title = cursor.getString(titleIndex);
                String joinDate = cursor.getString(joinDateIndex);
                TeamMember.Role role;
                if(cursor.getString(roleIndex) == "MANAGER")
                    role = TeamMember.Role.MANAGER;
                else
                    role = TeamMember.Role.EMPLOYEE;
                TeamMember newMember = new TeamMember(id,userId,joinDate,title,role);
                newMember.setTeamId(teamId);
                members.add(newMember);
            } while (cursor.moveToNext());
        }
        return members;
    }

    /***
     * getting all the table
     * @param db
     * @return
     */
    public static List<TeamMember> getAllTeamMembers(SQLiteDatabase db) {
        Cursor cursor = db.query(TEAM_MEMBER_TABLE, null, null, null, null, null, null);
        List<TeamMember> teamMembers = new LinkedList<TeamMember>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_ID);
            int teamIndex =  cursor.getColumnIndex(TEAM_MEMBER_TABLE_TEAM_ID);
            int userIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_USER_ID);
            int joinDate = cursor.getColumnIndex(TEAM_MEMBER_TABLE_JOIN_DATE);
            int jobTitle = cursor.getColumnIndex(TEAM_MEMBER_TABLE_JOB_TITLE);
            int role = cursor.getColumnIndex(TEAM_MEMBER_TABLE_ROLE);

            do {
                String memberId = cursor.getString(idIndex);
                String teamId = cursor.getString(teamIndex);
                String userId = cursor.getString(userIndex);
                String jDate = cursor.getString(joinDate);
                String jTitle = cursor.getString(jobTitle);
                String uRole = cursor.getString(role);
                TeamMember.Role newRole;
                if (uRole == "MANAGER")
                    newRole = TeamMember.Role.MANAGER;
                else
                    newRole = TeamMember.Role.EMPLOYEE;

                TeamMember tm = new TeamMember(memberId, userId, jDate, jTitle, newRole);
                tm.setTeamId(teamId);
                teamMembers.add(tm);
            } while (cursor.moveToNext());
        }
        return teamMembers;
    }

    public static TeamMember getTeamMemberById(SQLiteDatabase db, String id) {
        String where = TEAM_MEMBER_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TEAM_MEMBER_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_ID);
            int teamIndex =  cursor.getColumnIndex(TEAM_MEMBER_TABLE_TEAM_ID);
            int userIndex = cursor.getColumnIndex(TEAM_MEMBER_TABLE_USER_ID);
            int joinDate = cursor.getColumnIndex(TEAM_MEMBER_TABLE_JOIN_DATE);
            int jobTitle = cursor.getColumnIndex(TEAM_MEMBER_TABLE_JOB_TITLE);
            int role = cursor.getColumnIndex(TEAM_MEMBER_TABLE_ROLE);

            String memberId = cursor.getString(idIndex);
            String teamId = cursor.getString(teamIndex);
            String userId = cursor.getString(userIndex);
            String jDate = cursor.getString(joinDate);
            String jTitle = cursor.getString(jobTitle);
            String uRole = cursor.getString(role);
            TeamMember.Role newRole = TeamMember.Role.valueOf(uRole);
            TeamMember tm = new TeamMember(memberId, userId, jDate, jTitle, newRole);
            tm.setTeamId(teamId);
            return tm;
        }
        return null;
    }

    //add
    public static void add(SQLiteDatabase db, TeamMember tm) {
        ContentValues values = new ContentValues();
        values.put(TEAM_MEMBER_TABLE_ID, tm.getId());
        values.put(TEAM_MEMBER_TABLE_TEAM_ID, tm.getTeamId());
        values.put(TEAM_MEMBER_TABLE_USER_ID, tm.getUserId());
        values.put(TEAM_MEMBER_TABLE_JOIN_DATE, tm.getJoinDate());
        values.put(TEAM_MEMBER_TABLE_JOB_TITLE, tm.getJobTitle());
        values.put(TEAM_MEMBER_TABLE_ROLE, tm.getRole().toString());

        db.insert(TEAM_MEMBER_TABLE, TEAM_MEMBER_TABLE_ID, values); // "TEAM_MEMBER_TABLE_ID" can't be null
    }

    //edit
    public static void edit(SQLiteDatabase db, TeamMember tm) {
        String where = TEAM_MEMBER_TABLE_ID + " = " + tm.getId();
        ContentValues values = new ContentValues();
        values.put(TEAM_MEMBER_TABLE_ID, tm.getId());
        values.put(TEAM_MEMBER_TABLE_TEAM_ID, tm.getTeamId());
        values.put(TEAM_MEMBER_TABLE_USER_ID, tm.getUserId());
        values.put(TEAM_MEMBER_TABLE_JOIN_DATE, tm.getJoinDate());
        values.put(TEAM_MEMBER_TABLE_JOB_TITLE, tm.getJobTitle());
        values.put(TEAM_MEMBER_TABLE_ROLE, tm.getRole().toString());
        //query
        db.update(TEAM_MEMBER_TABLE, values, where, null);
    }

    //delete
    public static void delete(SQLiteDatabase db, String tmId) {
        String where = TEAM_MEMBER_TABLE_ID + " = " + tmId;
        db.delete(TEAM_MEMBER_TABLE, where, null);
    }

    //delete all
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TEAM_MEMBER_TABLE, null, null);
    }
}
