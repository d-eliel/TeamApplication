package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import el.team_application.Models.Entities.Task;
import el.team_application.Models.Entities.Team;
import el.team_application.Models.Entities.TeamMember;

/**
 * Created by Eliel on 6/4/2015.
 */
//provide access to Task Table in SQLite - CRUD

public class TaskSQL {

    // static string for consistancy
    final static String TASK_TABLE = "task";
    final static String TASK_TABLE_ID = "_taskId";
    final static String TASK_TABLE_NAME = "taskName";
    final static String TASK_TABLE_TEAM_ID = "teamId";
    final static String TASK_TABLE_CREATOR_ID = "creatorId";
    final static String TASK_TABLE_START = "startDate";
    final static String TASK_TABLE_END = "endDate";
    final static String TASK_TABLE_STATUS = "status";
    final static String TASK_TABLE_DESC = "description";
    final static String TASK_TABLE_ASSOCI = "association";


    // create the table
    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TASK_TABLE + " (" +
                TASK_TABLE_ID + " TEXT PRIMARY KEY," +
                TASK_TABLE_NAME + " TEXT," +
                TASK_TABLE_TEAM_ID + " TEXT," +
                TASK_TABLE_CREATOR_ID + " TEXT," +
                TASK_TABLE_START + " TEXT," +
                TASK_TABLE_END + " TEXT," +
                TASK_TABLE_STATUS + " TEXT," +
                TASK_TABLE_DESC + " TEXT," +
                TASK_TABLE_ASSOCI + " TEXT);");
    }

    // drop table
    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TASK_TABLE + ";");
    }

    private static Task.Status stringToTaskStatus(String status) {
        switch (status) {
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

    // return according to the id
    public static Task getById(SQLiteDatabase db, String id) {
        String where = TASK_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TASK_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int Id = cursor.getColumnIndex(id);
            int creatorId = cursor.getColumnIndex(TASK_TABLE_CREATOR_ID);
            int nameId = cursor.getColumnIndex(TASK_TABLE_NAME);
            int startDateId = cursor.getColumnIndex(TASK_TABLE_START);
            int endDateId = cursor.getColumnIndex(TASK_TABLE_END);
            int statusId = cursor.getColumnIndex(TASK_TABLE_STATUS);
            int descId = cursor.getColumnIndex(TASK_TABLE_DESC);
            int associationId = cursor.getColumnIndex(TASK_TABLE_ASSOCI);
            int teamInd = cursor.getColumnIndex(TASK_TABLE_TEAM_ID);

            String taskId = cursor.getString(Id);
            String creator = cursor.getString(creatorId);
            String taskName = cursor.getString(nameId);
            String startDate = cursor.getString(startDateId);
            String endDate = cursor.getString(endDateId);
            String status = cursor.getString(statusId);
            String association = cursor.getString(associationId);
            String description = cursor.getString(descId);
            String teamId = cursor.getString(teamInd);

            Task newTask = new Task(id, startDate, new TeamMember(creator, null, null, null, null), taskName);
            newTask.setEndDate(endDate);
            newTask.setStatus(stringToTaskStatus(status));
            newTask.setDescription(description);
            newTask.setAssociation(association);
            newTask.setTeamId(teamId);
            return newTask;
        }
        return null;
    }

    //add method
    public static void add(SQLiteDatabase db, Task task) {
        ContentValues values = new ContentValues();
        values.put(TASK_TABLE_ID, task.getId());
        values.put(TASK_TABLE_TEAM_ID, task.getTeamId());
        values.put(TASK_TABLE_ASSOCI, task.getAssociation());
        values.put(TASK_TABLE_CREATOR_ID, task.getCreator().getId());
        values.put(TASK_TABLE_DESC, task.getDescription());
        values.put(TASK_TABLE_START, task.getStartDate());
        values.put(TASK_TABLE_END, task.getEndDate());
        values.put(TASK_TABLE_STATUS, task.getStatus().toString());

        db.insert(TASK_TABLE, TASK_TABLE_ID, values);
    }

    // edit method
    public static void edit(SQLiteDatabase db, Task task) {
        String where = TASK_TABLE_ID + " = " + task.getId();
        ContentValues values = new ContentValues();
        values.put(TASK_TABLE_ID, task.getId());
        values.put(TASK_TABLE_TEAM_ID, task.getTeamId());
        values.put(TASK_TABLE_ASSOCI, task.getAssociation());
        values.put(TASK_TABLE_CREATOR_ID, task.getCreator().getId());
        values.put(TASK_TABLE_DESC, task.getDescription());
        values.put(TASK_TABLE_START, task.getStartDate());
        values.put(TASK_TABLE_END, task.getEndDate());
        values.put(TASK_TABLE_STATUS, task.getStatus().toString());
        db.update(TASK_TABLE, values, where, null);
    }

    //delete method
    public static void delete(SQLiteDatabase db, String taskId) {
        String where = TASK_TABLE_ID + " = " + taskId;
        db.delete(TASK_TABLE_ID, where, null);
    }

    public static List<Task> getTeamTasks(SQLiteDatabase db, String teamId){
        String where = TASK_TABLE_TEAM_ID + " = ?";
        String[] args = {teamId};
        Cursor cursor = db.query(TASK_TABLE, null, where, args, null, null, null);

        List<Task> tasks = new LinkedList<>();
        if (cursor.moveToFirst()) {
            int taskIdIndex = cursor.getColumnIndex(TASK_TABLE_ID);
            int creatorId = cursor.getColumnIndex(TASK_TABLE_CREATOR_ID);
            int nameId = cursor.getColumnIndex(TASK_TABLE_NAME);
            int startDateId = cursor.getColumnIndex(TASK_TABLE_START);
            int endDateId = cursor.getColumnIndex(TASK_TABLE_END);
            int statusId = cursor.getColumnIndex(TASK_TABLE_STATUS);
            int descId = cursor.getColumnIndex(TASK_TABLE_DESC);
            int associationId = cursor.getColumnIndex(TASK_TABLE_ASSOCI);

            do{
                String taskId = cursor.getString(taskIdIndex);
                String creator = cursor.getString(creatorId);
                String taskName = cursor.getString(nameId);
                String startDate = cursor.getString(startDateId);
                String endDate = cursor.getString(endDateId);
                String status = cursor.getString(statusId);
                String association = cursor.getString(associationId);
                String description = cursor.getString(descId);

                Task newTask = new Task(taskId, startDate, new TeamMember(creator, null, null, null, null), taskName);
                newTask.setEndDate(endDate);
                newTask.setStatus(stringToTaskStatus(status));
                newTask.setDescription(description);
                newTask.setAssociation(association);
                newTask.setTeamId(teamId);
            } while (cursor.moveToNext());
        }

        return tasks;
    }

}

