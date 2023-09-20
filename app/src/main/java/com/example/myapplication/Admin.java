package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Admin extends AppCompatActivity {
    private long backPressed;

    Button AccountManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AccountManage = findViewById(R.id.AccountManage);

        AccountManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenUserInfo();
            }
        });
    }
    public void doOpenUserInfo() {
        Intent myIntent = new Intent(this, UserListAdapter.class);
        startActivity(myIntent);
    }
}