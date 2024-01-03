package com.yanir.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddGrade extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    int colName, colId;
    Cursor crs;
    ArrayList<String> students;
    ArrayList<Integer> studentsId;
    ArrayAdapter<String> adpStudents;
    ArrayAdapter<Integer> adpQuarter;
    Spinner spinnerStudent, spinnerQuarter;
    EditText editTextSubject, editTextTypeOfGrade, editTextGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();

        // get a arrayList of all the students and an arrayList of all the student KEY_ID so that the same index will be the same student
        crs = db.query(Student.TABLE_STUDENTS, null, null, null, null, null, null);
        crs.moveToFirst();
        colName = crs.getColumnIndex(Student.NAME);
        colId = crs.getColumnIndex(Student.KEY_ID);
        students = new ArrayList<String>();
        studentsId = new ArrayList<Integer>();
        while (!crs.isAfterLast()) {
            students.add(crs.getString(colName));
            studentsId.add(crs.getInt(colId));
            crs.moveToNext();
        }
        crs.close();
        db.close();


        // connect the java to the xml file
        spinnerStudent = (Spinner) findViewById(R.id.spinnerStudent);
        spinnerQuarter = (Spinner) findViewById(R.id.spinnerQuarter);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextTypeOfGrade = (EditText) findViewById(R.id.editTextTypeOfGrade);
        editTextGrade = (EditText) findViewById(R.id.editTextGrade);


        adpStudents = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, students);
        spinnerStudent.setAdapter(adpStudents);
        adpQuarter = new ArrayAdapter<Integer>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new Integer[]{1, 2, 3, 4});
        spinnerQuarter.setAdapter(adpQuarter);

    }

    public void addGrade(View v) {
        db = hlp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Grade.STUDENT_ID, studentsId.get(spinnerStudent.getSelectedItemPosition()));
        cv.put(Grade.SUBJECT, editTextSubject.getText().toString());
        cv.put(Grade.TYPE_OF_GRADE, editTextTypeOfGrade.getText().toString());
        cv.put(Grade.GRADE, Integer.parseInt(editTextGrade.getText().toString()));
        cv.put(Grade.QUARTER, spinnerQuarter.getSelectedItemPosition() + 1);
        db.insert(Grade.TABLE_GRADES, null, cv);
        db.close();

        // create a toast that will tell the user that the grade was added
        Toast.makeText(this, "Grade was added", Toast.LENGTH_SHORT).show();
    }
}