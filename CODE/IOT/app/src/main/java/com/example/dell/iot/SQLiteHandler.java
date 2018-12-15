/**
 * Author: Hergli Sedki
 * */
package com.example.dell.iot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_PHOTOS = "photos";
    private static final String TABLE_LOCATION = "locations";
    private static final String TABLE_SUPER = "supers";
    private static final String TABLE_SENSOR = "sensors";
    private static final String TABLE_BALANCE = "balances";
    private static final String TABLE_STATU = "status";
    private static final String TABLE_US = "us";
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LATI = "lat_i";
    private static final String KEY_LNGI = "lng_i";
    private static final String KEY_CREATED_AT = "created_at";
    // Photos Table Columns names
    private static final String KEY_NAME1 = "photo_name";
    private static final String KEY_URL = "photo_url";
    private static final String KEY_CAPTION= "caption";
    // location Table Columns names
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG= "lng";
    // location Table Columns names
    private static final String KEY_NAME3 = "name_u";
    private static final String KEY_EMAIL3 = "email_u";
    private static final String KEY_UID3 = "unique_id_u";
    private static final String KEY_X = "X";
    private static final String KEY_Y = "Y";
    private static final String KEY_Z = "Z";

    private static final String KEY_PHONE_S = "phone_u";
    private static final String KEY_UID_SE = "sensor_uid";
    private static final String KEY_EMAIL_SE = "email_u";
    private static final String KEY_H_SE = "humidity";
    private static final String KEY_T_SE = "temperature";
    private static final String KEY_C_SE = "current";
    private static final String KEY_V_SE = "voltage";
    private static final String KEY_B_SE = "battery_mah";
    private static final String KEY_MV_SE = "max_v";
    private static final String KEY_MMV_SE = "min_v";
    private static final String KEY_STAT = "stat";
    private static final String KEY_DIST = "dist";
    private static final String KEY_ST = "st";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_PHONE + " TEXT," + KEY_LNGI + " TEXT," + KEY_LATI + " TEXT," + KEY_NAME3 + " TEXT," + KEY_EMAIL3 + " TEXT," + KEY_PHONE_S + " TEXT," + KEY_UID3 + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME1 + " TEXT," + KEY_URL + " TEXT," + KEY_CAPTION + " TEXT" + ")";
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAT + " TEXT,"  + KEY_LNG + " TEXT," + KEY_EMAIL3 + " TEXT," + KEY_UID3 + " TEXT" + ")";
        String CREATE_SUPER_TABLE = "CREATE TABLE " + TABLE_SUPER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_PHONE + " TEXT," + KEY_LNGI + " TEXT," + KEY_LATI + " TEXT," + KEY_NAME3 + " TEXT," + KEY_EMAIL3 + " TEXT," + KEY_PHONE_S + " TEXT," + KEY_UID3 + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_SENSOR_TABLE = "CREATE TABLE " + TABLE_SENSOR + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_UID_SE + " TEXT," + KEY_EMAIL_SE + " TEXT," + KEY_H_SE + " TEXT," + KEY_T_SE + " TEXT," + KEY_C_SE + " TEXT," + KEY_V_SE + " TEXT," + KEY_B_SE + " TEXT," + KEY_MV_SE + " TEXT," + KEY_MMV_SE + " TEXT," + KEY_DIST + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_BALANCE_TABLE = "CREATE TABLE " + TABLE_BALANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMAIL3 + " TEXT,"  + KEY_UID3 + " TEXT," + KEY_X + " TEXT," + KEY_Y + " TEXT," + KEY_Z + " TEXT" + ")";
        String CREATE_STATU_TABLE = "CREATE TABLE " + TABLE_STATU + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STAT + " TEXT" + ")";
        String CREATE_US_TABLE = "CREATE TABLE " + TABLE_US + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ST + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_SUPER_TABLE);
        db.execSQL(CREATE_PHOTOS_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_SENSOR_TABLE);
        db.execSQL(CREATE_BALANCE_TABLE);
        db.execSQL(CREATE_STATU_TABLE);
        db.execSQL(CREATE_US_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_US);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String phone, String lat, String lng, String name_s, String email_s, String phone_s, String uid_s, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_PHONE, phone); // phone
        values.put(KEY_LATI, lat); // lat
        values.put(KEY_LNGI, lng); // lng
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_NAME3, name_s); // Name
        values.put(KEY_EMAIL3, email_s); // Email
        values.put(KEY_UID3, uid_s); // Email
        values.put(KEY_PHONE_S, phone_s); // phone

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    /**
     * Storing type user details in database
     * */
    public void addType(String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ST, type); // Name

        // Inserting Row
        long id = db.insert(TABLE_US, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    /**
     * Storing super details in database
     * */
    public void addSuper(String name_s, String email_s, String uid, String phone_s ,String name, String email, String uid_u, String phone, String lat, String lng, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name_s); // Name
        values.put(KEY_EMAIL, email_s); // Email
        values.put(KEY_UID, uid); // unique_id
        values.put(KEY_PHONE, phone_s); // phone
        values.put(KEY_NAME3, name); // Name
        values.put(KEY_EMAIL3, email); // Email
        values.put(KEY_LATI, lat); // lat
        values.put(KEY_LNGI, lng); // lng
        values.put(KEY_UID3, uid_u); // Email
        values.put(KEY_PHONE_S, phone); // phone
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_SUPER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    /**
     * Storing photo details in database
     * */
    public void addPhoto(String photo_name, String photo_url, String caption) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME1, photo_name); // Name
        values.put(KEY_URL, photo_url); // Url
        values.put(KEY_CAPTION, caption); // Caption

        // Inserting Row
        long id = db.insert(TABLE_PHOTOS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New photo inserted into sqlite: " + id);
    }

    /**
     * Storing photo details in database
     * */
    public void addStat(String stat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STAT, stat);
        // Inserting Row
        long id = db.insert(TABLE_STATU, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New photo inserted into sqlite: " + id);
    }


    /**
     * Storing location details in database
     * */
    public void addLocation(String lat, String lng, String email, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAT, lat); // lat
        values.put(KEY_LNG, lng); // lng
        values.put(KEY_EMAIL3, email); // email
        values.put(KEY_UID3, uid); // unique_id_u


        // Inserting Row
        db.delete(TABLE_LOCATION, null, null);
        long id = db.insert(TABLE_LOCATION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New location inserted into sqlite: " + id);
    }


    /**
     * Storing balance details in database
     * */
    public void addBalance(String email, String unique_id, String x, String y, String z) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL3, email);
        values.put(KEY_UID3, unique_id);
        values.put(KEY_X, x);
        values.put(KEY_Y, y);
        values.put(KEY_Z, z);
        // Inserting Row
        db.delete(TABLE_BALANCE, null, null);
        long id = db.insert(TABLE_BALANCE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New balance inserted into sqlite: " + id);
    }


    /**
     * Storing sensor details in database
     * */
    public void addSensor(String sensor_uid, String email_u, String humidity, String temperature, String current, String voltage, String battery_mah, String max_v, String min_v, String dist, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID_SE, sensor_uid); // Name
        values.put(KEY_EMAIL_SE, email_u); // Email
        values.put(KEY_H_SE, humidity); // Email
        values.put(KEY_T_SE, temperature); // phone
        values.put(KEY_C_SE, current); // lat
        values.put(KEY_V_SE, voltage); // lng
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_B_SE, battery_mah); // Name
        values.put(KEY_MV_SE, max_v); // Email
        values.put(KEY_MMV_SE, min_v); // Email
        values.put(KEY_DIST, dist); // Email

        // Inserting Row
        long id = db.insert(TABLE_SENSOR, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Sensor inserted into sqlite: " + id);
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("unique_id", cursor.getString(3));
            user.put("phone", cursor.getString(4));
            user.put("lng_i", cursor.getString(5));
            user.put("lat_i", cursor.getString(6));
            user.put("name_u", cursor.getString(7));
            user.put("email_u", cursor.getString(8));
            user.put("phone_u", cursor.getString(9));
            user.put("unique_id_u", cursor.getString(10));
            user.put("created_at", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getTypeDetails() {
        HashMap<String, String> type = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_US;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            type.put("st", cursor.getString(1));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + type.toString());

        return type;
    }


    /**
     * Getting super data from database
     * */
    public HashMap<String, String> getSuperDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_SUPER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("unique_id", cursor.getString(3));
            user.put("phone", cursor.getString(4));
            user.put("lng_i", cursor.getString(5));
            user.put("lat_i", cursor.getString(6));
            user.put("name_u", cursor.getString(7));
            user.put("email_u", cursor.getString(8));
            user.put("phone_u", cursor.getString(9));
            user.put("unique_id_u", cursor.getString(10));
            user.put("created_at", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching super from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting photo data from database
     * */
    public HashMap<String, String> getPhotoDetails() {
        HashMap<String, String> photo = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTOS ;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                photo.put("photo_name", cursor.getString(1));
                photo.put("photo_url", cursor.getString(2));
                photo.put("caption", cursor.getString(3));
            }
            cursor.close();
            db.close();
            // return photo
            Log.d(TAG, "Fetching photo from Sqlite: " + photo.toString());
        return photo;
    }


    /**
     * Getting stat data from database
     * */
    public HashMap<String, String> getStatDetails() {
        HashMap<String, String> stat = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            stat.put("stat", cursor.getString(1));
        }
        cursor.close();
        db.close();
        return stat;
    }


    /**
     * Getting location data from database
     * */
    public HashMap<String, String> getLocationDetails() {
        HashMap<String, String> location = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            location.put("lat", cursor.getString(1));
            location.put("lng", cursor.getString(2));
            location.put("email_u", cursor.getString(3));
            location.put("unique_id_u", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return photo
        Log.d(TAG, "Fetching location from Sqlite: " + location.toString());
        return location;
    }

    /**
     * Getting sensor data from database
     * */
    public HashMap<String, String> getSensorDetails() {
        HashMap<String, String> sensor = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_SENSOR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            sensor.put("sensor_uid", cursor.getString(1));
            sensor.put("email_u", cursor.getString(2));
            sensor.put("humidity", cursor.getString(3));
            sensor.put("temperature", cursor.getString(4));
            sensor.put("current", cursor.getString(5));
            sensor.put("voltage", cursor.getString(6));
            sensor.put("battery_mah", cursor.getString(7));
            sensor.put("max_v", cursor.getString(8));
            sensor.put("min_v", cursor.getString(9));
            sensor.put("dist", cursor.getString(10));
            sensor.put("created_at", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching sensor from Sqlite: " + sensor.toString());

        return sensor;
    }


    /**
     * Getting balance data from database
     * */
    public HashMap<String, String> getBalanceDetails() {
        HashMap<String, String> balance = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_BALANCE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            balance.put("email_u", cursor.getString(1));
            balance.put("unique_id_u", cursor.getString(2));
            balance.put("X", cursor.getString(3));
            balance.put("Y", cursor.getString(4));
            balance.put("Z", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching balance from Sqlite: " + balance.toString());

        return balance;
    }


    /**
     * balance existence in database
     * */
    public boolean isBalnceExist() {

        String selectQuery = "SELECT  * FROM " + TABLE_BALANCE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }




    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_PHOTOS, null, null);
        db.delete(TABLE_LOCATION, null, null);
        db.delete(TABLE_SUPER, null, null);
        db.delete(TABLE_SENSOR, null, null);
        db.delete(TABLE_BALANCE, null, null);
        db.delete(TABLE_STATU, null, null);
        db.delete(TABLE_US, null, null);
        db.close();
        Log.d(TAG, "Deleted all user, sensor, photo and location info from sqlite");
    }

}