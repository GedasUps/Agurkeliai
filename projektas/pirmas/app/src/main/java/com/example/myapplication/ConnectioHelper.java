package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

public class ConnectioHelper extends SQLiteOpenHelper {
    private Context context;
    private   static final String DATABASE_NAME="Markers.db";
    private  static final int DATABASE_VERSION=1;
    private  static final String TABLE_NAME="Entries";
    private   static final String COLUMN_ID="_id ";
    private  static final String COLUMN_NAME="_userName ";
    private  static final String COLUMN_TYPE="_type ";
    private  static final String COLUMN_DATE="_date ";
    private  static final String COLUMN_DESCRIPION="_description ";
    private  static final String COLUMN_LATITUDE="_latitude ";
    private  static final String COLUMN_LONGTITUDE="_longtitude ";




    public ConnectioHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    // Assuming your MySQL server is running locally


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME+" ("+COLUMN_ID+"INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME+"CHAR, "
                +COLUMN_TYPE+"CHAR, "+COLUMN_DATE+"DATE, "+COLUMN_DESCRIPION+"CAHR, "+COLUMN_LATITUDE+"DOUBLE, "
                +COLUMN_LONGTITUDE+"DOUBLE);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    void addMarker(String name, String type, Date date, String description, double latitude, double longtitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_TYPE,type);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);
        cv.put(COLUMN_DATE,dateString);
        cv.put(COLUMN_DESCRIPION,description);
        cv.put(COLUMN_LATITUDE,latitude);
        cv.put(COLUMN_LONGTITUDE,longtitude);

      long result=  db.insert(TABLE_NAME,null,cv);
      if(result==-1)
      {
          // failed to insert to db
      //    Toast.makeText(context,"failed to insert",Toast.LENGTH_SHORT).show();// show message as android notification

      }
      else
      {
        //  Toast.makeText(context,"inserted",Toast.LENGTH_SHORT).show();
      }
    }
    //reads all data from ours db
    Cursor ReadAllData()
    {
        String query  = "Select * from Entries";
        //gets all data from db
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        // if db is not empty
        if(db!=null )
        {
          cursor=  db.rawQuery(query,null);

        }
        return cursor;
    }

}
