package com.yanir.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    HelperDB hlp;
    TextView textTotalStudents, textTotalGrades;
    Intent in;

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

        in = getIntent();
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

    public void goToShowData(View v) {
        startActivity(new Intent(this, show_data.class));
    }

    public void goToSorting(View v) {
        startActivity(new Intent(this, sorting.class));
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