package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import el.team_application.Models.Entities.Session;
import el.team_application.Models.Entities.Team;

/**
 * Created by Eliel on 6/21/2015.
 */
public class SessionSQL {
    final static String SESSION_TABLE                       = "session";
    final static String SESSION_TABLE_ID                    = "_sessionId";
    final static String SESSION_TABLE_USER_ID               = "userId";
    final static String SESSION_TABLE_TOKEN                 = "token";
    final static String SESSION_TABLE_IS_LOGGED             = "isLogged";
    final static String SESSION_TABLE_IS_UNSYNCED_DATA      = "isUnsyncedData";
    final static String SESSION_TABLE_LAST_SYNC             = "lastSyncFromServer";


    static public void create(SQLiteDatabase db){
        db.execSQL("create table " + SESSION_TABLE + " (" +
                SESSION_TABLE_ID + " TEXT," +
                SESSION_TABLE_USER_ID + " TEXT," +
                SESSION_TABLE_TOKEN + " TEXT," +
                SESSION_TABLE_IS_LOGGED + " BOOLEAN," +
                SESSION_TABLE_IS_UNSYNCED_DATA + " BOOLEAN," +
                SESSION_TABLE_LAST_SYNC + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + SESSION_TABLE + ";");
    }

    public static Session getSession (SQLiteDatabase db) {
        Cursor cursor = db.query(SESSION_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int sessionIndex = cursor.getColumnIndex(SESSION_TABLE_ID);
            int userIdIndex = cursor.getColumnIndex(SESSION_TABLE_USER_ID);
            int tokenIndex = cursor.getColumnIndex(SESSION_TABLE_TOKEN);
            int isLoggedIndex = cursor.getColumnIndex(SESSION_TABLE_IS_LOGGED);
            int isUnsyncedIndex = cursor.getColumnIndex(SESSION_TABLE_IS_UNSYNCED_DATA);
            int lastSyncIndex = cursor.getColumnIndex(SESSION_TABLE_LAST_SYNC);

            String sessionId    = cursor.getString(sessionIndex);
            String userId       = cursor.getString(userIdIndex);
            String token        = cursor.getString(tokenIndex);
            boolean isLogged    = cursor.getInt(isLoggedIndex) == 1;
            boolean isUnsynced  = cursor.getInt(isUnsyncedIndex) == 1;
            String  lastSync    = cursor.getString(lastSyncIndex);

            return new Session(sessionId, userId, token, isLogged, isUnsynced, lastSync);
        }
        return null;
    }

    public static void add(SQLiteDatabase db, Session session) {
        ContentValues values = new ContentValues();
        values.put(SESSION_TABLE_ID, session.getSessionId());
        values.put(SESSION_TABLE_USER_ID, session.getUserId());
        values.put(SESSION_TABLE_TOKEN, session.getToken());
        values.put(SESSION_TABLE_LAST_SYNC, session.getLastSyncFromServer());
        if(session.isLogged()){
            values.put(SESSION_TABLE_IS_LOGGED, 1);
        }else{
            values.put(SESSION_TABLE_IS_LOGGED, 0);
        }
        if(session.isUnsynced()){
            values.put(SESSION_TABLE_IS_UNSYNCED_DATA, 1);
        }else{
            values.put(SESSION_TABLE_IS_UNSYNCED_DATA, 0);
        }
        db.insert(SESSION_TABLE, SESSION_TABLE_ID, values);
    }

    public static void edit(SQLiteDatabase db, Session session){
        String where = SESSION_TABLE_ID + " = " + session.getSessionId();
        ContentValues values = new ContentValues();
        values.put(SESSION_TABLE_ID, session.getSessionId());
        values.put(SESSION_TABLE_USER_ID, session.getUserId());
        values.put(SESSION_TABLE_TOKEN, session.getToken());
        values.put(SESSION_TABLE_IS_LOGGED, session.isLogged());
        values.put(SESSION_TABLE_IS_UNSYNCED_DATA, session.isUnsynced());
        values.put(SESSION_TABLE_LAST_SYNC, session.getLastSyncFromServer());

        db.update(SESSION_TABLE, values, where, null);
    }

    public static void delete(SQLiteDatabase db){
        db.delete(SESSION_TABLE, null, null);
    }
}
