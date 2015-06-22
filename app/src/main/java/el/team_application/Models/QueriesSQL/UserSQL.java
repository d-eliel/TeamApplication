package el.team_application.Models.QueriesSQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import el.team_application.Models.Entities.User;

/**
 * Created by Eliel on 6/20/2015.
 */
public class UserSQL {
    /*
        provide access to User Table in SQLite - CRUD
     */

    final static String USER_TABLE          = "user";
    final static String USER_TABLE_ID       = "_userId";
    final static String USER_TABLE_NAME     = "name";
    final static String USER_TABLE_EMAIL    = "email";
    final static String USER_TABLE_PHONE    = "phone";

    static public void create(SQLiteDatabase db){
        db.execSQL("create table " + USER_TABLE + " (" +
                USER_TABLE_ID + " TEXT," +
                USER_TABLE_NAME + " TEXT," +
                USER_TABLE_EMAIL + " TEXT," +
                USER_TABLE_PHONE + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + USER_TABLE + ";");
    }

    public static User getById (SQLiteDatabase db, String id) {
        String where = USER_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(USER_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(USER_TABLE_NAME);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);
            int phoneIndex = cursor.getColumnIndex(USER_TABLE_PHONE);

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            String phone = cursor.getString(phoneIndex);

            User user = new User (_id,email,name);
            user.setPhone(phone);
            return user;
        }
        return null;
    }

    public static User getByEmail (SQLiteDatabase db, String mEmail) {
        String where = USER_TABLE_EMAIL + " = ?";
        String[] args = {mEmail};
        Cursor cursor = db.query(USER_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(USER_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(USER_TABLE_NAME);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);
            int phoneIndex = cursor.getColumnIndex(USER_TABLE_PHONE);

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            String phone = cursor.getString(phoneIndex);

            User user = new User (_id,email,name);
            user.setPhone(phone);
            return user;
        }
        return null;
    }

    public static void add(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_ID, user.getId());
        values.put(USER_TABLE_NAME, user.getName());
        values.put(USER_TABLE_EMAIL, user.getEmail());
        values.put(USER_TABLE_PHONE, user.getPhone());
        db.insert(USER_TABLE, USER_TABLE_ID, values);
    }

    public static void edit(SQLiteDatabase db, User user){
        String where = USER_TABLE_ID + " = " + user.getId();
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_ID, user.getId());
        values.put(USER_TABLE_NAME, user.getName());
        values.put(USER_TABLE_EMAIL, user.getEmail());
        values.put(USER_TABLE_PHONE, user.getPhone());
        db.update(USER_TABLE, values, where, null);
    }

    public static void delete(SQLiteDatabase db, String userId){
        String where = USER_TABLE_ID + " = " + userId;
        db.delete(USER_TABLE,where,null);
    }

}
