package com.yanir.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    Intent in;

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


        // create an adapter for the spinners
        adpStudents = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, students);
        spinnerStudent.setAdapter(adpStudents);
        adpQuarter = new ArrayAdapter<Integer>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new Integer[]{1, 2, 3, 4});
        spinnerQuarter.setAdapter(adpQuarter);


        // check if the user want to edit a grade instead of adding a new one
        gradeID = getIntent().getIntExtra("grade_id", -1);
        if (gradeID != -1) {
            // if they dose, show the is_grade_active switch and set the values of the grade to the editTexts
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

        in = getIntent();

    }

    /**
     * add/update a grade to the database
     * @param v the view - button
     */
    public void addGrade(View v) {
        // check if the grade is valid
        if (!isValidGrade(editTextGrade.getText().toString())) {
            return;
        }

        // make sure the user want to add/update the grade
        // create an alert dialog to make sure the user want to add/update the student
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Are you sure?");
        boolean isUpdate = gradeID != -1;
        if (isUpdate) {
            alertDialog.setMessage("Are you sure you want to update this grade?");
        } else {
            alertDialog.setMessage("Are you sure you want to add this grade?");
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            // if the user press yes, add/update the grade
            addToDB();
            dialog.dismiss();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            // if the user press no, close the dialog
            if (isUpdate){
                finish();
            }
            dialog.dismiss();

        });
    }

    public void addToDB(){
        db = hlp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Grade.STUDENT_ID, studentsId.get(spinnerStudent.getSelectedItemPosition()));
        cv.put(Grade.SUBJECT, editTextSubject.getText().toString());
        cv.put(Grade.TYPE_OF_GRADE, editTextTypeOfGrade.getText().toString());
        cv.put(Grade.GRADE, Integer.parseInt(editTextGrade.getText().toString()));
        cv.put(Grade.QUARTER, spinnerQuarter.getSelectedItemPosition() + 1);
        // check if the user want to edit a grade instead of adding a new one
        if (gradeID != -1) {
            // if they dose, update the grade in the database
            cv.put(Grade.IS_GRADE_ACTIVE, is_grade_active.isChecked());
            db.update(Grade.TABLE_GRADES, cv, Grade.KEY_ID_GRADE+"="+gradeID, null);
            db.close();
            Toast.makeText(this, "Grade was updated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            // if not add the grade to the database
            cv.put(Grade.IS_GRADE_ACTIVE, 1);
            db.insert(Grade.TABLE_GRADES, null, cv);
            db.close();
            // create a toast that will tell the user that the grade was added
            Toast.makeText(this, "Grade was added", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * check if the grade is valid
     * @param grade the grade to check
     * @return true if the grade is valid, false otherwise
     */
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

    /**
     * This function presents the options menu for moving between activities.
     * @param menu The options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.manu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getTitle().toString().equals("Home")){
            in.setClass(this, MainActivity.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("add student")){
            in.setClass(this, AddStudent.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("add grade")){
            in.setClass(this, AddGrade.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("show data")){
            in.setClass(this, show_data.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("filter data")){
            in.setClass(this, sorting.class);
            startActivity(in);
        }
        else if (item.getTitle().toString().equals("credits")){
            in.setClass(this, credits.class);
            startActivity(in);
        }
        in.setClass(this, MainActivity.class);
        return super.onOptionsItemSelected(item);
    }
}