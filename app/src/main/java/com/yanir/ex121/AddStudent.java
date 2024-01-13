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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class AddStudent extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    EditText etName, etAddress, etPhone, etHomePhone, etFatherName, etFatherPhone, etMotherName, etMotherPhone;
    int studentID;
    Button addStudent;
    Switch switchActive;
    Intent in;

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
        addStudent = (Button) findViewById(R.id.save_button);
        switchActive = (Switch) findViewById(R.id.is_active);

        //hide the switch
        switchActive.setVisibility(View.GONE);


        // connect the java to the database
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        // get the intent that started this activity and check if it there is a student id value
        studentID =  getIntent().getIntExtra("student_id", -1);
        if(studentID != -1)
        {
            // if there is a student id value then get the student info from the db and put it in the EditTexts
            db = hlp.getReadableDatabase();
            String[] columns = {Student.NAME, Student.ADDRESS, Student.PHONE, Student.HOME_PHONE, Student.FATHER_NAME, Student.FATHER_PHONE, Student.MOTHER_NAME, Student.MOTHER_PHONE, Student.ACTIVE};
            String where = Student.KEY_ID + " = ?";
            String[] args = {"" + studentID};
            Cursor crs = db.query(Student.TABLE_STUDENTS, columns, where, args, null, null, null);
            crs.moveToFirst();
            etName.setText(crs.getString(0));
            etAddress.setText(crs.getString(1));
            etPhone.setText(crs.getString(2));
            etHomePhone.setText(crs.getString(3));
            etFatherName.setText(crs.getString(4));
            etFatherPhone.setText(crs.getString(5));
            etMotherName.setText(crs.getString(6));
            etMotherPhone.setText(crs.getString(7));
            switchActive.setChecked(crs.getInt(8) == 1);
            switchActive.setVisibility(View.VISIBLE);
            crs.close();
            db.close();

            //change the text of the button to "update student" and the on click func to updateStudent
            addStudent.setText("Update Student");
        }

        in = getIntent();
    }

    /**
     * add/update a student to the database
     * @param v the view - button
     */
    public void addStudent(View v)
    {
        //check if the user entered at least a name
        if(etName.getText().toString().equals("") || etName.getText().toString().equals(null))
        {
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        // create an alert dialog to make sure the user want to add/update the student
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        boolean isUpdate = studentID != -1;
        if(isUpdate)
        {
            alertDialog.setMessage("Are you sure you want to update the student?");
        }
        else
        {
            alertDialog.setMessage("Are you sure you want to add the student?");
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                (dialog, which) -> {
                    addToDB();
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                (dialog, which) -> {
                    dialog.dismiss();
                    if (isUpdate)
                        finish();
                    return;
                });
        alertDialog.show();
    }

    public void addToDB(){
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
        if (studentID != -1)
        {
            cv.put(Student.ACTIVE, switchActive.isChecked());
            // if there is a student id value then update the student info in the db
            db.update(Student.TABLE_STUDENTS, cv, Student.KEY_ID+"="+studentID, null);
            db.close();
            Toast.makeText(this, "Student was updated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        else {
            cv.put(Student.ACTIVE, 1);
            db.insert(Student.TABLE_STUDENTS, null, cv);
            db.close();

            // create a tost that says the student was added
            Toast.makeText(this, "Student was added", Toast.LENGTH_SHORT).show();
        }
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