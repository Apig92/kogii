package me.aflak.bluetoothterminal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by cindy on 02/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //create database and table
    public static final String DATABASE_NAME = "lateralData.db";
    public static final String TABLE_NAME = "lateral_table";

    //Columns for the table
    public static final String ID = "ID";
    public static final String LAT = "lAT";
    public static final String LON = "LON";
    public static final String DISTANCE = "DISTANCE";
    public static final String TIMESTAMP = "TIMESTAMP";

    //Call to create a database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    //creates table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, LAT REAL, LON REAL, DISTANCE REAL, TIMESTAMP TEXT);" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String lat, String lon, String distance, String timestamp){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LAT, Double.parseDouble(lat));
        contentValues.put(LON, Double.parseDouble(lon));
        contentValues.put(DISTANCE, Double.parseDouble(distance));
        contentValues.put(TIMESTAMP, timestamp);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }else{
            return true;
        }

    }


    // Get all the data from the local android sqite db. Store in a nested ArrayList
    public ArrayList getAll() {

        ArrayList<ArrayList<String>> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null); //Select all from db

        // Move cursor through table until no more
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> line = new ArrayList<>();
                line.add(cursor.getString(1));
                line.add(cursor.getString(2));
                line.add(cursor.getString(3));
                line.add(cursor.getString(4));
                //The info returned is adding to a list then added to the outer array list
                data.add(line);
            } while (cursor.moveToNext());
        }

        //Close the cursor and return the data
        cursor.close();
        return data;
    }

    // Deletes all from local sqlite db
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }


}
