package com.yanir.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class credits extends AppCompatActivity {
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        in = getIntent();
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