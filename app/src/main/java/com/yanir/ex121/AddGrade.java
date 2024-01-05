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
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class AddGrade extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    int gradeID;
    ArrayList<String> students;
    ArrayList<Integer> studentsId;
    ArrayAdapter<String> adpStudents;
    ArrayAdapter<Integer> adpQuarter;
    Spinner spinnerStudent, spinnerQuarter;
    EditText editTextSubject, editTextTypeOfGrade, editTextGrade;
    Switch is_grade_active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        hlp = new HelperDB(this);

        students = hlp.getStudents();
        studentsId = hlp.getStudentsId();




        // connect the java to the xml file
        spinnerStudent = (Spinner) findViewById(R.id.spinnerStudent);
        spinnerQuarter = (Spinner) findViewById(R.id.spinnerQuarter);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextTypeOfGrade = (EditText) findViewById(R.id.editTextTypeOfGrade);
        editTextGrade = (EditText) findViewById(R.id.editTextGrade);
        is_grade_active = (Switch) findViewById(R.id.is_grade_active);
        is_grade_active.setVisibility(View.GONE);


        adpStudents = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, students);
        spinnerStudent.setAdapter(adpStudents);
        adpQuarter = new ArrayAdapter<Integer>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new Integer[]{1, 2, 3, 4});
        spinnerQuarter.setAdapter(adpQuarter);

        gradeID = getIntent().getIntExtra("grade_id", -1);
        if (gradeID != -1) {
            is_grade_active.setVisibility(View.VISIBLE);
            db = hlp.getReadableDatabase();
            String[] columns = {Grade.STUDENT_ID, Grade.SUBJECT, Grade.TYPE_OF_GRADE, Grade.GRADE, Grade.QUARTER, Grade.IS_GRADE_ACTIVE};
            String where = Grade.KEY_ID_GRADE + " = ?";
            String[] args = {"" + gradeID};
            Cursor crs = db.query(Grade.TABLE_GRADES, columns, where, args, null, null, null);
            crs.moveToFirst();
            editTextSubject.setText(crs.getString(1));
            editTextTypeOfGrade.setText(crs.getString(2));
            editTextGrade.setText("" + crs.getInt(3));
            spinnerQuarter.setSelection(crs.getInt(4) - 1);
            spinnerStudent.setSelection(studentsId.indexOf(crs.getInt(0)));
            is_grade_active.setChecked(crs.getInt(5) == 1);
            crs.close();
            db.close();
        }

    }

    public void addGrade(View v) {
        // check if the grade is valid
        if (!isValidGrade(editTextGrade.getText().toString())) {
            return;
        }
        db = hlp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Grade.STUDENT_ID, studentsId.get(spinnerStudent.getSelectedItemPosition()));
        cv.put(Grade.SUBJECT, editTextSubject.getText().toString());
        cv.put(Grade.TYPE_OF_GRADE, editTextTypeOfGrade.getText().toString());
        cv.put(Grade.GRADE, Integer.parseInt(editTextGrade.getText().toString()));
        cv.put(Grade.QUARTER, spinnerQuarter.getSelectedItemPosition() + 1);
        if (gradeID != -1) {
            cv.put(Grade.IS_GRADE_ACTIVE, is_grade_active.isChecked());
            db.update(Grade.TABLE_GRADES, cv, Grade.KEY_ID_GRADE+"="+gradeID, null);
            db.close();
            Toast.makeText(this, "Grade was updated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            cv.put(Grade.IS_GRADE_ACTIVE, 1);
            db.insert(Grade.TABLE_GRADES, null, cv);
            db.close();
            // create a toast that will tell the user that the grade was added
            Toast.makeText(this, "Grade was added", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidGrade(String grade) {
        // check if is a empty string by length or by ""
        if (grade.length() == 0 || grade.equals("")) {
            Toast.makeText(this, "you must enter a grade", Toast.LENGTH_SHORT).show();
            return false;
        }
        // check if the grade is between 0-100
        int gradeInt = Integer.parseInt(grade);
        if (gradeInt < 0 || gradeInt > 100) {
            Toast.makeText(this, "the grade must be between 0-100", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}