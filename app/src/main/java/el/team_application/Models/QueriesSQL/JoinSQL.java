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
    public static List<Team> getTeamIdsAndNamesForUser(SQLiteDatabase db, String userId){
        //String query = "SELECT * FROM " + TEAM_TABLE+ " INNER JOIN " + TEAM_MEMBER_TABLE + " WHERE " + TEAM_TABLE_ID + " = " + TEAM_MEMBER_TABLE_TEAM_ID;
        String tables = TEAM_TABLE+ " INNER JOIN " + TEAM_MEMBER_TABLE;
        String where = TEAM_TABLE_ID + " = " + TEAM_MEMBER_TABLE_TEAM_ID;
        Cursor cursor = db.query(tables,null,where,null,null,null,null);
        List<Team> teams = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TEAM_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TEAM_TABLE_NAME);
            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                if(!teams.contains(new Team(id,name,null))){
                    teams.add(new Team(id,name,null));
                }
            } while (cursor.moveToNext());
        }
        return teams; // teams with - id and name only
    }
}
