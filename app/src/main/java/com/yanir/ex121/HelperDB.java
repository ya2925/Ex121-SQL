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

    /**
     * this function counts the number of active students in the db
     * @return the number of active students
     */
    public int getStudentCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID};
        // get the number of rows in the table with is_active = 1
        int count = db.query(TABLE_STUDENTS, columns, ACTIVE+"=1", null, null, null, null).getCount();
        db.close();
        return count;
    }

    /**
     * this function counts the number of active grades in the db
     * @return the number of active grades
     */
    public int getGradeCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {STUDENT_ID};
        // get all the students if of an active grade
        Cursor crs = db.query(TABLE_GRADES, columns, IS_GRADE_ACTIVE+"=1", null, null, null, null);
        // check if the student is active if not then dont count it
        int count = 0;
        crs.moveToFirst();
        int colStudentId = crs.getColumnIndex(STUDENT_ID);
        while (!crs.isAfterLast()) {
            if (isStudentActive(crs.getInt(colStudentId))) {
                count++;
            }
            crs.moveToNext();
        }
        crs.close();
        db.close();
        return count;
    }

    /**
     * this function return an arraylist of all the students names in the db
     * @return an arraylist of all the students names in the db
     */
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

        return students;
    }

    /**
     * this function return an arraylist of all the students id in the db
     * @return an arraylist of all the students id in the db
     */
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

    /**
     * this function takes a student id and return the student name
     * @param studentId the student id
     * @return the student name
     */
    public String getStudentName(int studentId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {NAME};
        Cursor crs = db.query(TABLE_STUDENTS, columns, KEY_ID+"="+studentId, null, null, null, null);
        crs.moveToFirst();
        int colName = crs.getColumnIndex(NAME);
        String name = crs.getString(colName);
        crs.close();
        db.close();
        return name;
    }

    /**
     * this function takes a student id and return an arraylist of all the student in the array list there are 3 lists, list of grades, list of types and list of subjects and quarter
     * every index will coralate to the same index in the other lists
     * so when you have a grade at index 1 then the subject and quarter will be at index 1 in the other lists and also the type of grade
     * @param studentId the student id
     * @return an arraylist of all the student in the array list there are 3 lists, list of grades, list of types and list of subjects and quarter
     */
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
            typeOfGrade.add(crs.getString(colSubject) + " - quarter " + crs.getString(colQuarter));
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

    /**
     * this function takes a student id and return an arraylist of all the grades id of the student
     * @param studentId the student id
     * @return an arraylist of all the grades id of the student
     */
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

    /**
     * this function takes a student id and change the active value to 0 or 1 depending on the current value
     * @param studentId the student id
     */
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

    /**
     * this function takes a grade id and change the active value to 0 or 1 depending on the current value
     * @param gradeId the grade id
     */
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

    /**
     * this function takes a student id and return true if the student is active and false if not
     * @param studentId
     * @return
     */
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

    /**
     * this function takes a grade id and return true if the grade is active and false if not
     * @param gradeId
     * @return
     */
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

    /**
     * this functiom return an arraylist of all the subjects in the db
     * @return
     */
    public ArrayList<String> getSubjects()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {SUBJECT};
        ArrayList<String> subjects = new ArrayList<String>();
        Cursor crs = db.query(TABLE_GRADES, columns, null, null, null, null, null);
        crs.moveToFirst();
        int colSubject = crs.getColumnIndex(SUBJECT);
        while (!crs.isAfterLast()) {
            subjects.add(crs.getString(colSubject));
            crs.moveToNext();
        }
        crs.close();
        db.close();

        // remove duplicates
        for (int i = 0; i < subjects.size(); i++) {
            for (int j = i+1; j < subjects.size(); j++) {
                if(subjects.get(i).equals(subjects.get(j))){
                    subjects.remove(j);
                    j--;
                }
            }
        }
        return subjects;
    }

    /**
     * this function return an arraylist of all the grades of the subject the arraylist will contain 3 arraylists, one for the student name, one for the type of grade and one for the grade
     * every index will coralate to the same index in the other arraylists
     * so when you have a grade at index 1 then the student name and type of grade will be at index 1 in the other arraylists
     * @param subject the subject
     * @return an arraylist of all the grades of the subject the arraylist will contain 3 arraylists, one for the student name, one for the type of grade and one for the grade
     */
    public ArrayList<ArrayList<String>> getGradesInSubject(String subject)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {STUDENT_ID, TYPE_OF_GRADE, GRADE, QUARTER};
        ArrayList<ArrayList<String>> grades = new ArrayList<ArrayList<String>>();
        // get all the grades with the subject and that are active
        Cursor crs = db.query(TABLE_GRADES, columns , SUBJECT+"='"+subject+"' AND "+IS_GRADE_ACTIVE+"=1", null, null, null, null);
        crs.moveToFirst();
        int colStudentId = crs.getColumnIndex(STUDENT_ID);
        int colTypeOfGrade = crs.getColumnIndex(TYPE_OF_GRADE);
        int colGrade = crs.getColumnIndex(GRADE);
        int colQuarter = crs.getColumnIndex(QUARTER);
        // screate 3 arrays for the 3 columns
        ArrayList<String> studentId = new ArrayList<String>();
        ArrayList<String> typeOfGrade = new ArrayList<String>();
        ArrayList<String> grade = new ArrayList<String>();
        // add the data to the arrays
        while (!crs.isAfterLast()) {
            // check if the student is active
            if (isStudentActive(crs.getInt(colStudentId))) {
                studentId.add(getStudentName(crs.getInt(colStudentId)));
                typeOfGrade.add(crs.getString(colTypeOfGrade) + " - quarter " + crs.getString(colQuarter));
                grade.add(crs.getString(colGrade));
            }
            crs.moveToNext();
        }
        // add the arrays to the grades arraylist
        grades.add(studentId);
        grades.add(typeOfGrade);
        grades.add(grade);

        crs.close();
        db.close();

        return grades;
    }


    /**
     * this function takes a student id and return the average of the student
     * @param studentId the student id
     * @return the average of the student
     */
    public double getStudentAverage(int studentId){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {GRADE};
        // get all the active grades of the student
        Cursor crs = db.query(TABLE_GRADES, columns , STUDENT_ID+"="+studentId+" AND "+IS_GRADE_ACTIVE+"=1", null, null, null, null);
        crs.moveToFirst();
        int colGrade = crs.getColumnIndex(GRADE);
        double sum = 0;
        int count = 0;
        while (!crs.isAfterLast()) {
            sum += crs.getInt(colGrade);
            count++;
            crs.moveToNext();
        }
        crs.close();
        db.close();
        if (count == 0) {
            return 0;
        }
        return sum/count;
    }

    /**
     * this function return an arraylist of all the students average in the db
     * @return an arraylist of all the students average in the db
     */
    public ArrayList<String> getStudentsAverage(){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID};
        // get all the students id of active students
        Cursor crs = db.query(TABLE_STUDENTS, columns , ACTIVE+"=1", null, null, null, null);
        crs.moveToFirst();
        int colId = crs.getColumnIndex(KEY_ID);
        ArrayList<String> studentsAverage = new ArrayList<String>();
        while (!crs.isAfterLast()) {
            studentsAverage.add(getStudentName(crs.getInt(colId)) + " - " + getStudentAverage(crs.getInt(colId)));
            crs.moveToNext();
        }
        crs.close();
        db.close();

        // sort the arraylist
        for (int i = 0; i < studentsAverage.size(); i++) {
            for (int j = i+1; j < studentsAverage.size(); j++) {
                if(Double.parseDouble(studentsAverage.get(i).substring(studentsAverage.get(i).indexOf("-")+2)) < Double.parseDouble(studentsAverage.get(j).substring(studentsAverage.get(j).indexOf("-")+2))){
                    String temp = studentsAverage.get(i);
                    studentsAverage.set(i, studentsAverage.get(j));
                    studentsAverage.set(j, temp);
                }
            }
        }

        return studentsAverage;
    }

    /**
     * this function return an arraylist of all the grades of the quarter the arraylist will contain 4 arraylists, one for the student name, one for the subject, one for the type of grade and one for the grade
     * every index will coralate to the same index in the other arraylists
     * @param quarter
     * @return
     */
    public ArrayList<ArrayList<String>> getGradesInQuarter(int quarter)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {STUDENT_ID, SUBJECT, TYPE_OF_GRADE, GRADE};
        ArrayList<ArrayList<String>> grades = new ArrayList<ArrayList<String>>();
        // get all the grades with the subject and that are active
        Cursor crs = db.query(TABLE_GRADES, columns , QUARTER+"="+quarter+" AND "+IS_GRADE_ACTIVE+"=1", null, null, null, null);
        crs.moveToFirst();
        int colStudentId = crs.getColumnIndex(STUDENT_ID);
        int colSubject = crs.getColumnIndex(SUBJECT);
        int colTypeOfGrade = crs.getColumnIndex(TYPE_OF_GRADE);
        int colGrade = crs.getColumnIndex(GRADE);
        // screate 3 arrays for the 3 columns
        ArrayList<String> studentId = new ArrayList<String>();
        ArrayList<String> subject = new ArrayList<String>();
        ArrayList<String> typeOfGrade = new ArrayList<String>();
        ArrayList<String> grade = new ArrayList<String>();
        // add the data to the arrays
        while (!crs.isAfterLast()) {
            // check if the student is active
            if (isStudentActive(crs.getInt(colStudentId))) {
                studentId.add(getStudentName(crs.getInt(colStudentId)));
                subject.add(crs.getString(colSubject));
                typeOfGrade.add(crs.getString(colTypeOfGrade));
                grade.add(crs.getString(colGrade));
            }
            crs.moveToNext();
        }
        // add the arrays to the grades arraylist
        grades.add(studentId);
        grades.add(subject);
        grades.add(typeOfGrade);
        grades.add(grade);

        crs.close();
        db.close();

        return grades;
    }


}

