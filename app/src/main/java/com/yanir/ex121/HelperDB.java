package com.yanir.ex121;

import static com.yanir.ex121.Student.*;
import static com.yanir.ex121.Grade.*;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbexam.db";
    private static final int DATABASE_VERSION = 1;
    private String strCreate, strDelete;


    /**
     * Instantiates a new HelperDB
     *
     * @param context the context
     */
    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        strCreate="CREATE TABLE "+TABLE_STUDENTS;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+NAME+" TEXT,";
        strCreate+=" "+ACTIVE+" INTEGER,";
        strCreate+=" "+ADDRESS+" TEXT,";
        strCreate+=" "+PHONE+" TEXT,";
        strCreate+=" "+HOME_PHONE+" TEXT,";
        strCreate+=" "+FATHER_NAME+" TEXT,";
        strCreate+=" "+FATHER_PHONE+" TEXT,";
        strCreate+=" "+MOTHER_NAME+" TEXT,";
        strCreate+=" "+MOTHER_PHONE+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_GRADES;
        strCreate+=" ("+KEY_ID_GRADE+" INTEGER PRIMARY KEY,";
        strCreate+=" "+STUDENT_ID+" INTEGER,";
        strCreate+=" "+SUBJECT+" TEXT,";
        strCreate+=" "+TYPE_OF_GRADE+" TEXT,";
        strCreate+=" "+GRADE+" INTEGER,";
        strCreate+=" "+QUARTER+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        strDelete = "DROP TABLE IF EXISTS " + TABLE_STUDENTS;
        db.execSQL(strDelete);
        strDelete = "DROP TABLE IF EXISTS " + TABLE_GRADES;
        db.execSQL(strDelete);

        onCreate(db);
    }

    public int getStudentCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID};
        int count = db.query(TABLE_STUDENTS, columns, null, null, null, null, null).getCount();
        db.close();
        return count;
    }

    public int getGradeCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID_GRADE};
        int count = db.query(TABLE_GRADES, columns, null, null, null, null, null).getCount();
        db.close();
        return count;
    }

}

