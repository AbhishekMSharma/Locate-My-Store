package com.abhishekmsharma.locatemystore2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Sharma on 23/04/2015.
 */
public class LocationDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationHistory",
            TABLE_locationHistory = "locationHistory",KEY_ID="lID",KEY_NAME="lName",
    KEY_LAT="lLat",KEY_LONG="lLong";

    public LocationDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_locationHistory + "(" + KEY_ID + " TEXT, " + KEY_NAME + " TEXT  PRIMARY KEY, "
        + KEY_LAT + " TEXT, " + KEY_LONG + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_locationHistory);
        onCreate(db);
    }
    public void createLocation(LocationHistory lh)
    {
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,lh.getlId());
        values.put(KEY_NAME, lh.getlName());
        values.put(KEY_LAT, lh.getlLat());
        values.put(KEY_LONG, lh.getlLong());

        db.insert(TABLE_locationHistory, null, values);
        db.close();


    }
    public LocationHistory getLocation(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_locationHistory, new String[] {KEY_ID, KEY_NAME, KEY_LAT, KEY_LONG}, KEY_NAME + "=?",new String[] {String.valueOf(id)},null,null,null,null);

        if(cursor!=null)
            cursor.moveToFirst();

        LocationHistory lh = new LocationHistory(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        db.close();
        cursor.close();
        return lh;
    }



    public void deleteLocation(LocationHistory lh)
    {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_locationHistory, KEY_ID + "=?", new String[] {String.valueOf(lh.getlId())});
        db.close();
    }
    public int updateLocation(LocationHistory lh)
    {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,lh.getlId());
        values.put(KEY_NAME, lh.getlName());
        values.put(KEY_LAT, lh.getlLat());
        values.put(KEY_LONG, lh.getlLong());

        return db.update(TABLE_locationHistory, values, KEY_ID + "=?", new String[] {String.valueOf(lh.getlId())});
    }
    public List<LocationHistory> getAllLocations()
    {
        List<LocationHistory> locations = new ArrayList<LocationHistory>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_locationHistory, null);
        if(cursor.moveToFirst())
        {
            do {
                LocationHistory lh = new LocationHistory(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                locations.add(lh);

            }while(cursor.moveToNext());
        }
        return locations;
    }
    public int getLocationsCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_locationHistory, null);
        int a = cursor.getCount();
        cursor.close();
        db.close();
        return a;

    }
}
