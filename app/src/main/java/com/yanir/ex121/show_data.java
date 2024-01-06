package com.yanir.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class show_data extends AppCompatActivity implements View.OnCreateContextMenuListener {
    Spinner studentsOrGrades,chooseStudent;
    ListView listView;
    HelperDB hlp;
    ArrayList<String> students;
    int colName;
    ArrayAdapter<String> adpStudentsOrGrades,adpChooseStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        // connect the java spinner and listView to the xml file
        studentsOrGrades = (Spinner) findViewById(R.id.typeOfSorting);
        chooseStudent = (Spinner) findViewById(R.id.additionalData);
        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Students", "Grades"});
        studentsOrGrades.setAdapter(adapter);

        // connect the java to the database
        hlp = new HelperDB(this);


        // add a listener to the spinner with a func that will show the students or grades depending on the user choice
        studentsOrGrades.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position == 0) {
                    showStudents();
                    // hide the chooseStudent spinner
                    chooseStudent.setEnabled(false);
                } else {
                    // show the chooseStudent spinner
                    chooseStudent.setEnabled(true);
                    // get all the students names in an arrayList
                    students = hlp.getStudents();
                    // print the students names
                    for (int i = 0; i < students.size(); i++) {
                        System.out.println(students.get(i));
                    }
                    adpChooseStudent = new ArrayAdapter<String>(show_data.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, students);
                    chooseStudent.setAdapter(adpChooseStudent);
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // add an event listener to the chooseStudent spinner
        chooseStudent.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                showGrades();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        registerForContextMenu(listView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // check if the user selected the students spinner
        if (studentsOrGrades.getSelectedItemPosition() == 0) {
            showStudents();
        }
        // check if the user selected the grades spinner
        else {
            showGrades();
        }
    }

    private void showGrades() {
        // get the selected student name index on the spinner
        int studentIndex = chooseStudent.getSelectedItemPosition();
        // check if the user selected a student
        if (studentIndex == -1) {
            return;
        }
        // get the selected student id from the database
        int studentId = hlp.getStudentsId().get(studentIndex);
        // get all the grades of the selected student
        ArrayList<ArrayList<String>> grades = hlp.getGrades(studentId);
        // if there is no grades add to the arrayList a message
        if (grades.size() == 0) {
            String[] grade = new String[3];
            grade[0] = "No grades";
            grade[1] = "";
            grade[2] = "";
        }
        // create an adapter for the listView with sub items
        GradeListAdapter adapter = new GradeListAdapter(this, grades.get(0), grades.get(1), grades.get(2));
        listView.setAdapter(adapter);
    }

    public void showStudents() {
        // get all the students names in an arrayList
        students = hlp.getStudents();
        adpStudentsOrGrades = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, students);
        listView.setAdapter(adpStudentsOrGrades);
    }


    public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        super.onCreateContextMenu(menu, v, menuInfo);
        // check if the user selected the students spinner
        if (studentsOrGrades.getSelectedItemPosition() == 0) {
            menu.add("Edit student");
            // add a delete option only if the student is active
            // get the selected student name index on the spinner
            if (hlp.isStudentActive(hlp.getStudentsId().get(position))) {
                menu.add("Delete student");
            }
            else
            {
                menu.add("Activate student");
            }
        }
        // check if the user selected the grades spinner
        else {

            menu.add("Edit grade");
            if (hlp.isGradeActive(hlp.getGradesId(hlp.getStudentsId().get(chooseStudent.getSelectedItemPosition())).get(position))) {
                menu.add("Delete grade");
            }
            else
            {
                menu.add("Activate grade");
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (item.getTitle().equals("Edit student")) {
            // get the selected student id from the database
            int studentId = hlp.getStudentsId().get(position);
            // create an intent to the AddStudent activity and send the student id
            Intent si = new android.content.Intent(this, AddStudent.class);
            si.putExtra("student_id", studentId);
            startActivity(si);
        }
        else if(item.getTitle().equals("Delete student") || item.getTitle().equals("Activate student"))
        {
            // get the selected student id from the database
            int studentId = hlp.getStudentsId().get(position);
            // delete the student from the database
            hlp.deleteOrActiveStudent(studentId);
        }
        else if(item.getTitle().equals("Edit grade")){
            // get the grade id from the database
            int gradeId = hlp.getGradesId(hlp.getStudentsId().get(chooseStudent.getSelectedItemPosition())).get(position);

            Intent si = new android.content.Intent(this, AddGrade.class);
            si.putExtra("grade_id", gradeId);
            startActivity(si);
            showGrades();
        }
        else if (item.getTitle().equals("Delete grade") || item.getTitle().equals("Activate grade"))
        {
            // get the grade id from the database
            int gradeId = hlp.getGradesId(hlp.getStudentsId().get(chooseStudent.getSelectedItemPosition())).get(position);
            // delete the grade from the database
            hlp.deleteOrActiveGrade(gradeId);
        }



        return false;
    }
}