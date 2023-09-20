package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView txt_register;
    String username, password, fullname, isAdmin;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        etUsername = binding.etLoginUsername;
        etPassword = binding.etLoginPassword;
        btnLogin = binding.btnLogin;
        txt_register = binding.tvRegister;

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etUsername.getText().toString())) {
                    etUsername.setError("Please enter username");
                    return;
                }

                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    etPassword.setError("Please enter password");
                    return;
                }

                // lấy dữ liệu lên
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    Intent intent;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // lấy thẻ trên database
                        username = snapshot.child(etUsername.getText().toString().replace(".", ",")).child("username").getValue(String.class);
                        password = snapshot.child(etUsername.getText().toString().replace(".", ",")).child("password").getValue(String.class);
                        fullname = snapshot.child(etUsername.getText().toString().replace(".", ",")).child("fullname").getValue(String.class);
                        isAdmin = snapshot.child(etUsername.getText().toString().replace(".", ",")).child("isAdmin").getValue(String.class);

                        user = new Users(username, password, fullname,"user");

                        if (username != null && etPassword.getText().toString().equals(password) && isAdmin.equals("user")) {
                            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", user.getFullname());
                            editor.apply();
                            // chuyển đến MainActivity
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công với tên " + user.getFullname(), Toast.LENGTH_LONG).show();
                        }
                        else  if (username != null && etPassword.getText().toString().equals(password) && isAdmin.equals("admin")) {
                            SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", user.getFullname());
                            editor.apply();
//
                            // chuyển đến Admin
                            intent = new Intent(LoginActivity.this, Admin.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công với tên " + user.getFullname(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Sai tên hoặc mật khẩu", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Sai tên hoặc mật khẩu", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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
