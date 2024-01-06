package com.yanir.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class sorting extends AppCompatActivity {
    Spinner typeOfSorting, additionalData;
    HelperDB hlp;
    ListView listView;
    ArrayList<String> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        hlp = new HelperDB(this);

        // connect the java spinner and listView to the xml file
        typeOfSorting = (Spinner) findViewById(R.id.typeOfSorting);
        additionalData = (Spinner) findViewById(R.id.additionalData);
        listView = (ListView) findViewById(R.id.listView);
        additionalData.setEnabled(false);

        String[] sortingOptions = new String[]{"All student grades in a single subject", "students grades average in descending order", "All student grades in a single quarter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortingOptions);
        typeOfSorting.setAdapter(adapter);

        // add a listener to the spinner with a func that will show the students or grades depending on the user choice
        typeOfSorting.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position == 0) {
                    additionalData.setEnabled(true);
                    subjects = hlp.getSubjects();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(sorting.this, android.R.layout.simple_spinner_item, subjects);
                    additionalData.setAdapter(adapter);
                }
                else if (position == 1) {
                    additionalData.setEnabled(false);
                    ArrayList<String> studentsAvrage = hlp.getStudentsAverage();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(sorting.this, android.R.layout.simple_spinner_item, studentsAvrage);
                    listView.setAdapter(adapter);
                }
                else if (position == 2) {
                    additionalData.setEnabled(true);
                    ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(sorting.this, android.R.layout.simple_spinner_item, new Integer[]{1, 2, 3, 4});
                    additionalData.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });

        additionalData.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if(typeOfSorting.getSelectedItemPosition() == 0){
                    showAllStudentGradesInASingleSubject(position);
                }
                else if(typeOfSorting.getSelectedItemPosition() == 2){
                    ArrayList<ArrayList<String>> info = hlp.getGradesInQuarter(position+1);
                    GradeListAdapter adp = new GradeListAdapter(sorting.this, info.get(0), info.get(1), info.get(2));
                    listView.setAdapter(adp);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
    }


    /**
     * this function will show all the students in the listView
     */
    public void showAllStudentGradesInASingleSubject(int position) {
        ArrayList<ArrayList<String>> info = hlp.getGradesInSubject(subjects.get(position));
        GradeListAdapter adp = new GradeListAdapter(this, info.get(0), info.get(1), info.get(2));
        listView.setAdapter(adp);
    }
}