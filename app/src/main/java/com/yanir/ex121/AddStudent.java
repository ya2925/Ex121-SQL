package com.yanir.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudent extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    EditText etName, etAddress, etPhone, etHomePhone, etFatherName, etFatherPhone, etMotherName, etMotherPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // connect the EditTexts to the xml file
        etName = (EditText) findViewById(R.id.student_name);
        etAddress = (EditText) findViewById(R.id.student_address);
        etPhone = (EditText) findViewById(R.id.student_phone);
        etHomePhone = (EditText) findViewById(R.id.student_home_phone);
        etFatherName = (EditText) findViewById(R.id.student_father_name);
        etFatherPhone = (EditText) findViewById(R.id.student_father_phone);
        etMotherName = (EditText) findViewById(R.id.student_mother_name);
        etMotherPhone = (EditText) findViewById(R.id.student_mother_phone);



        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
    }

    // a func for a button that adds a student to the db
    public void addStudent(View v)
    {
        //check if the user entered at least a name
        if(etName.getText().toString().equals("") || etName.getText().toString().equals(null))
        {
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        db = hlp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Student.NAME, etName.getText().toString());
        cv.put(Student.ADDRESS, etAddress.getText().toString());
        cv.put(Student.PHONE, etPhone.getText().toString());
        cv.put(Student.HOME_PHONE, etHomePhone.getText().toString());
        cv.put(Student.FATHER_NAME, etFatherName.getText().toString());
        cv.put(Student.FATHER_PHONE, etFatherPhone.getText().toString());
        cv.put(Student.MOTHER_NAME, etMotherName.getText().toString());
        cv.put(Student.MOTHER_PHONE, etMotherPhone.getText().toString());
        cv.put(Student.ACTIVE, 1);
        db.insert(Student.TABLE_STUDENTS, null, cv);
        db.close();

        // create a tost that says the student was added
        Toast.makeText(this, "Student was added", Toast.LENGTH_SHORT).show();
    }

}