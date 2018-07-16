package com.example.dell.contactsmodifier;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;

    public static final String DATABASE_NAME="CONTACTS";

    public static final String TABLE="CON";
    public static final String Name="NAME";
    public static final String Number="NUMBER";
    public static final String Status="STATUS";


    public SQLiteHelper(Context context){

         super(context, DATABASE_NAME,null,DATABASE_VERSION);
     }

    public void onCreate(SQLiteDatabase db)
    {

        String p="CREATE TABLE "+TABLE+" ("+Name+" TEXT,"+Number+" TEXT,"+Status+" TEXT, PRIMARY KEY ("+Name+","
        +Number+"))";
        db.execSQL(p);

    }

    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);


        onCreate(db);


    }


}
