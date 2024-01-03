package com.yanir.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    TextView textTotalStudents, textTotalGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connect the TextViews to the xml file
        textTotalStudents = (TextView) findViewById(R.id.textTotalStudents);
        textTotalGrades = (TextView) findViewById(R.id.textTotalGrades);


        hlp = new HelperDB(this);

        // update the TextViews with the total number of students and grades
        textTotalStudents.setText("" + hlp.getStudentCount());
        textTotalGrades.setText("" + hlp.getGradeCount());
    }

    @Override
    public void onResume(){
        super.onResume();

        // update the TextViews with the total number of students and grades
        textTotalStudents.setText("" + hlp.getStudentCount());
        textTotalGrades.setText("" + hlp.getGradeCount());
    }

    //a func for a button that will start an activity
    public void goToAddStudent(View v) {
        startActivity(new Intent(this, AddStudent.class));
    }
    public void goToAddGrade(View v) {
        startActivity(new Intent(this, AddGrade.class));
    }

}