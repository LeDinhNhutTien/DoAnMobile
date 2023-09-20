package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import TienIch.LichActivity;

public class Account extends AppCompatActivity{

    Button btnLichSu, btnLogin;
    FirebaseDatabase db;
    DatabaseReference reference;
    TextView textViewUsername;
    ImageView tvTrolaiHome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("users");

        btnLogin = findViewById(R.id.btn_login);
        btnLichSu = findViewById(R.id.btn_lichSu);
        textViewUsername = findViewById(R.id.textview_username);
        tvTrolaiHome = findViewById(R.id.tvTrolai);

        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null && username != "") {
            // Hiển thị thông tin đăng nhập
            textViewUsername.setText("Xin chào " + username);
        } else {
            // Chưa đăng nhập,
            textViewUsername.setText("Xin chào bạn chưa đăng nhập");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenActivityLogin();
            }
        });
        btnLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenActivityLichSu();
            }
        });
        tvTrolaiHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenActivityHome();
            }
        });
    }

    public void doOpenActivityLogin() {
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }
    public void doOpenActivityLichSu() {
        Intent myIntent = new Intent(this, XemLichSu.class);
        startActivity(myIntent);
    }
    public void doOpenActivityHome() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("username");
        editor.apply();
    }
}