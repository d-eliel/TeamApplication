package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import el.team_application.Models.Entities.Unsynced;

/**
 * Created by Eliel on 6/20/2015.
 */
public class UnsyncedSQL {
    /*
        provide access to Unsynced Table in SQLite - CRUD
     */

    final static String UNSYNCED_TABLE                      = "unsynced";
    final static String UNSYNCED_TABLE_ID                   = "_unsyncedId";
    final static String UNSYNCED_TABLE_ACTION               = "action";
    final static String UNSYNCED_TABLE_TABLE                = "table";
    final static String UNSYNCED_TABLE_CHANGED_OBJECT_ID    = "changedObjectId";

    static public void create(SQLiteDatabase db){
        db.execSQL("create table " + UNSYNCED_TABLE + " (" +
                UNSYNCED_TABLE_ID + " TEXT," +
                UNSYNCED_TABLE_ACTION + " TEXT," +
                UNSYNCED_TABLE_TABLE + "TEXT," +
                UNSYNCED_TABLE_CHANGED_OBJECT_ID + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + UNSYNCED_TABLE + ";");
    }

    public static List<Unsynced> getUnsyncedData (SQLiteDatabase db) {
        Cursor cursor = db.query(UNSYNCED_TABLE, null, null, null, null, null, null);
        List<Unsynced> list = new LinkedList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UNSYNCED_TABLE_ID);
            int actionIndex = cursor.getColumnIndex(UNSYNCED_TABLE_ACTION);
            int tableIndex = cursor.getColumnIndex(UNSYNCED_TABLE_TABLE);
            int changedObjectIdIndex = cursor.getColumnIndex(UNSYNCED_TABLE_CHANGED_OBJECT_ID);
            do{
                String _id = cursor.getString(idIndex);
                String actionStr = cursor.getString(actionIndex);
                String tableStr = cursor.getString(tableIndex);
                String changedObjectId = cursor.getString(changedObjectIdIndex);
                Unsynced unsynced = new Unsynced (_id, Unsynced.Action.valueOf(actionStr), Unsynced.Table.valueOf(tableStr), changedObjectId);
                list.add(unsynced);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public static void add(SQLiteDatabase db, Unsynced unsynced) {
        ContentValues values = new ContentValues();
        values.put(UNSYNCED_TABLE_ID, unsynced.getId());
        values.put(UNSYNCED_TABLE_ACTION, unsynced.getAction().toString());
        values.put(UNSYNCED_TABLE_TABLE, unsynced.getTable().toString());
        values.put(UNSYNCED_TABLE_CHANGED_OBJECT_ID, unsynced.getChangedObjectId());
        db.insert(UNSYNCED_TABLE, UNSYNCED_TABLE_ID, values);
    }


    public static void remove(SQLiteDatabase db, String unsyncedId){
        String where = UNSYNCED_TABLE_ID + " = " + unsyncedId;
        db.delete(UNSYNCED_TABLE,where,null);
    }
}
