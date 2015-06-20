package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import el.team_application.Models.Entities.Team;

/**
 * Created by Eliel on 6/4/2015.
 */
public class TeamSQL {
    /*
        provide access to Team Table in SQLite - CRUD
     */

    final static String TEAM_TABLE          = "team";
    final static String TEAM_TABLE_ID       = "_teamId";
    final static String TEAM_TABLE_NAME     = "name";

    static public void create(SQLiteDatabase db){
        db.execSQL("create table " + TEAM_TABLE + " (" +
                TEAM_TABLE_ID + " TEXT PRIMARY KEY," +
                TEAM_TABLE_NAME + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TEAM_TABLE + ";");
    }

    public static Team getById (SQLiteDatabase db, String id) {
        String where = TEAM_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TEAM_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TEAM_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TEAM_TABLE_NAME);

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);

            Team team = new Team (_id,name,null);
            return team;
        }
        return null;
    }

    public static void add(SQLiteDatabase db, Team team) {
        ContentValues values = new ContentValues();
        values.put(TEAM_TABLE_ID, team.getId());
        values.put(TEAM_TABLE_NAME, team.getName());
        db.insert(TEAM_TABLE, TEAM_TABLE_ID, values);
    }

    public static void edit(SQLiteDatabase db, Team team){
        String where = TEAM_TABLE_ID + " = " + team.getId();
        ContentValues values = new ContentValues();
        values.put(TEAM_TABLE_ID, team.getId());
        values.put(TEAM_TABLE_NAME, team.getName());
        db.update(TEAM_TABLE, values, where, null);
    }

    public static void delete(SQLiteDatabase db, String teamId){
        String where = TEAM_TABLE_ID + " = " + teamId;
        db.delete(TEAM_TABLE, where, null);
    }

}
