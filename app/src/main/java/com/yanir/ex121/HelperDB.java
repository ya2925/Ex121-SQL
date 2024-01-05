package com.yanir.ex121;

import static com.yanir.ex121.Student.*;
import static com.yanir.ex121.Grade.*;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
        strCreate+=" "+QUARTER+" INTEGER,";
        strCreate+=" "+IS_GRADE_ACTIVE+" INTEGER";
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
        // get the number of rows in the table with is_active = 1
        int count = db.query(TABLE_STUDENTS, columns, ACTIVE+"=1", null, null, null, null).getCount();
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

    public ArrayList<String> getStudents()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {NAME};
        ArrayList<String> students = new ArrayList<String>();
        Cursor crs = db.query(TABLE_STUDENTS, columns, null, null, null, null, null);
        crs.moveToFirst();
        int colName = crs.getColumnIndex(NAME);
        while (!crs.isAfterLast()) {
            students.add(crs.getString(colName));
            crs.moveToNext();
        }
        crs.close();
        db.close();

    // print the arraylist
        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i));
        }

        return students;
    }

    public ArrayList<Integer> getStudentsId() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID};
        ArrayList<Integer> studentsId = new ArrayList<Integer>();
        Cursor crs = db.query(TABLE_STUDENTS, columns, null, null, null, null, null);
        crs.moveToFirst();
        int colId = crs.getColumnIndex(KEY_ID);
        while (!crs.isAfterLast()) {
            studentsId.add(crs.getInt(colId));
            crs.moveToNext();
        }
        crs.close();
        db.close();
        return studentsId;
    }

    public ArrayList<ArrayList<String>> getGrades(int studentId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {SUBJECT, TYPE_OF_GRADE, GRADE, QUARTER};
        ArrayList<ArrayList<String>> grades = new ArrayList<ArrayList<String>>();
        Cursor crs = db.query(TABLE_GRADES, columns, STUDENT_ID+"="+studentId, null, null, null, null);
        crs.moveToFirst();
        int colSubject = crs.getColumnIndex(SUBJECT);
        int colTypeOfGrade = crs.getColumnIndex(TYPE_OF_GRADE);
        int colGrade = crs.getColumnIndex(GRADE);
        int colQuarter = crs.getColumnIndex(QUARTER);
        // screate 3 arrays for the 3 columns
        ArrayList<String> subject = new ArrayList<String>();
        ArrayList<String> typeOfGrade = new ArrayList<String>();
        ArrayList<String> grade = new ArrayList<String>();
        // add the data to the arrays
        while (!crs.isAfterLast()) {
            subject.add(crs.getString(colTypeOfGrade));
            typeOfGrade.add(crs.getString(colSubject) + " - " + crs.getString(colQuarter));
            grade.add(crs.getString(colGrade));
            crs.moveToNext();
        }
        // add the arrays to the grades arraylist
        grades.add(subject);
        grades.add(typeOfGrade);
        grades.add(grade);

        crs.close();
        db.close();

        return grades;
    }
    public ArrayList<Integer> getGradesId(int studentId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID_GRADE};
        ArrayList<Integer> gradesId = new ArrayList<Integer>();
        Cursor crs = db.query(TABLE_GRADES, columns, STUDENT_ID+"="+studentId, null, null, null, null);
        crs.moveToFirst();
        int colId = crs.getColumnIndex(KEY_ID_GRADE);
        while (!crs.isAfterLast()) {
            gradesId.add(crs.getInt(colId));
            crs.moveToNext();
        }
        crs.close();
        db.close();
        return gradesId;
    }

    public void deleteOrActiveStudent(int studentId)
    {
        // delete by changing the active value to 0 with db.update
        ContentValues cv = new ContentValues();
        if (isStudentActive(studentId)) {
            cv.put(ACTIVE, 0);
        }
        else
        {
            cv.put(ACTIVE, 1);
        }
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_STUDENTS, cv, KEY_ID+"="+studentId, null);
        db.close();
    }

    public void deleteOrActiveGrade(int gradeId)
    {
        ContentValues cv = new ContentValues();
        if (isGradeActive(gradeId)) {
            cv.put(IS_GRADE_ACTIVE, 0);
        }
        else
        {
            cv.put(IS_GRADE_ACTIVE, 1);
        }
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_GRADES, cv, KEY_ID_GRADE+"="+gradeId, null);
        db.close();
    }

    public boolean isStudentActive(int studentId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ACTIVE};
        Cursor crs = db.query(TABLE_STUDENTS, columns, KEY_ID+"="+studentId, null, null, null, null);
        crs.moveToFirst();
        int colActive = crs.getColumnIndex(ACTIVE);
        boolean isActive = crs.getInt(colActive) == 1;
        crs.close();
        db.close();
        return isActive;
    }

    public boolean isGradeActive(int gradeId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {IS_GRADE_ACTIVE};
        Cursor crs = db.query(TABLE_GRADES, columns, KEY_ID_GRADE+"="+gradeId, null, null, null, null);
        crs.moveToFirst();
        int colActive = crs.getColumnIndex(IS_GRADE_ACTIVE);
        boolean isActive = crs.getInt(colActive) == 1;
        crs.close();
        db.close();
        return isActive;
    }



}

