package el.team_application.Models.QueriesSQL;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/20/2015.
 */
public class JoinSQL {
    /*
     *
     */

    final static String TEAM_TABLE                  = "team";
    final static String TEAM_TABLE_ID               = "_teamId";
    final static String TEAM_TABLE_NAME             = "name";

    final static String TEAM_MEMBER_TABLE           = "teamMember"; //table name
    final static String TEAM_MEMBER_TABLE_ID        = "_teamMemberId";
    final static String TEAM_MEMBER_TABLE_USER_ID   = "userId";
    final static String TEAM_MEMBER_TABLE_TEAM_ID   = "teamId";
    final static String TEAM_MEMBER_TABLE_JOIN_DATE = "joinDate";
    final static String TEAM_MEMBER_TABLE_JOB_TITLE = "jobTitle";
    final static String TEAM_MEMBER_TABLE_ROLE      = "role";

    final static String TASK_TABLE                  = "task";
    final static String TASK_TABLE_ID               = "_taskId";
    final static String TASK_TABLE_TEAM_ID          = "teamId";
    final static String TASK_TABLE_CREATOR_ID       = "creatorId";
    final static String TASK_TABLE_NAME             = "name";
    final static String TASK_TABLE_START            = "startDate";
    final static String TASK_TABLE_END              = "endDate";
    final static String TASK_TABLE_STATUS           = "status";
    final static String TASK_TABLE_DESC             = "description";
    final static String TASK_TABLE_ASSOCI           = "association";


    /***
     * return Team list only with id and name in it
     * @param db
     * @param userId
     * @return
     */
    public static List<Team> getTeamIdsForUser(SQLiteDatabase db, String userId){
        String query = "SELECT * FROM " + TEAM_TABLE+ " INNER JOIN " + TEAM_MEMBER_TABLE + " ON " + TEAM_TABLE_ID + " = " + TEAM_MEMBER_TABLE_TEAM_ID;

        Cursor cursor = db.rawQuery(query, null);
        List<Team> teams = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TEAM_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TEAM_TABLE_NAME);
            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                teams.add(new Team(id,name,null));
            } while (cursor.moveToNext());
        }
        return teams; // teams with - id and name only
    }

    private static Task.Status stringToTaskStatus(String status){
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

    public static List<Task> getTasksForTeam(SQLiteDatabase db, String teamId){
        String where = TASK_TABLE_TEAM_ID + " = ?";
        String[] args = {teamId};
        Cursor cursor = db.query(TASK_TABLE, null, where, args, null, null, null);

        List<Task> tasks = new LinkedList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TASK_TABLE_ID);
            int creatorIndex = cursor.getColumnIndex(TASK_TABLE_CREATOR_ID);
            int nameIndex = cursor.getColumnIndex(TASK_TABLE_NAME);
            int startIndex = cursor.getColumnIndex(TASK_TABLE_START);
            int endIndex = cursor.getColumnIndex(TASK_TABLE_END);
            int statusIndex = cursor.getColumnIndex(TASK_TABLE_STATUS);
            int descriptionIndex = cursor.getColumnIndex(TASK_TABLE_DESC);
            int associationIndex = cursor.getColumnIndex(TASK_TABLE_ASSOCI);

            do {
                String id = cursor.getString(idIndex);
                String creatorId = cursor.getString(creatorIndex);
                String name = cursor.getString(nameIndex);
                String start = cursor.getString(startIndex);
                String end = cursor.getString(endIndex);
                String status = cursor.getString(statusIndex);
                String description = cursor.getString(descriptionIndex);
                String association = cursor.getString(associationIndex);

                Task newTask = new Task(id,start,new TeamMember(creatorId,null,null,null,null),name);
                newTask.setEndDate(end);
                newTask.setStatus(stringToTaskStatus(status));
                newTask.setDescription(description);
                newTask.setAssociation(association);
                newTask.setTeamId(teamId);
                tasks.add(newTask);
            } while (cursor.moveToNext());
        }
        return tasks;
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
}
