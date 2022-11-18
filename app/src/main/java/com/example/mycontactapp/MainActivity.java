package com.example.mycontactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initilization
        fab = findViewById(R.id.fab);
        // Add Listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to new activity to add contact
                Intent intent = new Intent(MainActivity.this, AddEditContact.class);
                startActivity(intent);
            }
        });
    }

    /*
    1. Add Dependency
    2. Add Custom Colors
    3. Design Main Activity
    4. Create New Activity Named AddEditContact
    5. Design AddEditContact Activity
    */
}