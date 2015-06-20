package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
                UNSYNCED_TABLE_ID + " TEXT PRIMARY KEY," +
                UNSYNCED_TABLE_ACTION + " TEXT," +
                UNSYNCED_TABLE_TABLE + " TEXT," +
                UNSYNCED_TABLE_CHANGED_OBJECT_ID + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + UNSYNCED_TABLE + ";");
    }

    public static Unsynced getById (SQLiteDatabase db, String id) {
        String where = UNSYNCED_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(UNSYNCED_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UNSYNCED_TABLE_ID);
            int actionIndex = cursor.getColumnIndex(UNSYNCED_TABLE_ACTION);
            int tableIndex = cursor.getColumnIndex(UNSYNCED_TABLE_TABLE);
            int changedObjectIdIndex = cursor.getColumnIndex(UNSYNCED_TABLE_CHANGED_OBJECT_ID);

            String _id = cursor.getString(idIndex);
            String action = cursor.getString(actionIndex);
            String table = cursor.getString(tableIndex);
            String changedObjectId = cursor.getString(changedObjectIdIndex);

            Unsynced unsynced = new Unsynced (_id,action,table,changedObjectId);
            return unsynced;
        }
        return null;
    }

    public static void add(SQLiteDatabase db, Unsynced unsynced) {
        ContentValues values = new ContentValues();
        values.put(UNSYNCED_TABLE_ID, unsynced.getId());
        values.put(UNSYNCED_TABLE_ACTION, unsynced.getAction());
        values.put(UNSYNCED_TABLE_TABLE, unsynced.getTable());
        values.put(UNSYNCED_TABLE_CHANGED_OBJECT_ID, unsynced.getChangedObjectId());
        db.insert(UNSYNCED_TABLE, UNSYNCED_TABLE_ID, values);
    }

    public static void edit(SQLiteDatabase db, Unsynced unsynced){
        String where = UNSYNCED_TABLE_ID + " = " + unsynced.getId();
        ContentValues values = new ContentValues();
        values.put(UNSYNCED_TABLE_ID, unsynced.getId());
        values.put(UNSYNCED_TABLE_ACTION, unsynced.getAction());
        values.put(UNSYNCED_TABLE_TABLE, unsynced.getTable());
        values.put(UNSYNCED_TABLE_CHANGED_OBJECT_ID, unsynced.getChangedObjectId());
        db.update(UNSYNCED_TABLE, values, where, null);
    }

    public static void delete(SQLiteDatabase db, String unsyncedId){
        String where = UNSYNCED_TABLE_ID + " = " + unsyncedId;
        db.delete(UNSYNCED_TABLE,where,null);
    }
}
