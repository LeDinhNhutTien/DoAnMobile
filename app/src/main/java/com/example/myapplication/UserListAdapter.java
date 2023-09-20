package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityUserInfoBinding;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.firebase.ui.database.FirebaseListAdapter.OnItemLongClickListener;

public class UserListAdapter extends AppCompatActivity {

    ActivityUserInfoBinding binding;

    private ListView mListView;
    private Button add_user, edit, delete;
    Users user;
    EditText etUsername, etPassword;
    RadioGroup etRole;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        etUsername = binding.editName;
        etPassword = binding.editPassword;
        etRole = binding.radiogroupRole;

        add_user = binding.btnInsert;
        edit = binding.btnEdit;
        delete = binding.btnDelete;


        mListView = binding.lv;

        database  = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        FirebaseListOptions<Users> options = new FirebaseListOptions.Builder<Users>()
                .setLayout(R.layout.list_item_user)
                .setQuery(myRef, Users.class)
                .build();

        FirebaseListAdapter<Users> adapter = new FirebaseListAdapter<Users>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Users model, int position) {
                // Gán dữ liệu của mỗi người dùng vào các thành phần trong layout
                TextView tvId = v.findViewById(R.id.textview_id);
                TextView tvUsername = v.findViewById(R.id.textview_username);
                TextView tvPassword = v.findViewById(R.id.password_edit_text);

                tvUsername.setText(model.getUsername());
                tvPassword.setText(model.getPassword());
                tvId.setText(model.getIsAdmin());
            }
        };
        // Kết nối adapter với ListView và bắt đầu lắng nghe
        adapter.startListening();
        // Đặt adapter cho ListView
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Users selectedUser = (Users) adapterView.getItemAtPosition(position);
                etUsername.setText(selectedUser.getUsername());
                etPassword.setText(selectedUser.getPassword());
//                etRole.setText(selectedUser.getIsAdmin());
            }
        });

        // thêm
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                int selectedId = etRole.getCheckedRadioButtonId();
                if (selectedId == R.id.radiobutton_admin) {
                    role = "admin";
                } else if (selectedId == R.id.radiobutton_user) {
                    role = "user";
                }

                if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Please enter username");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Please enter password");
                    return;
                }
                myRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Nếu username đã được sử dụng trước đó, hiển thị lỗi và không thực hiện đăng ký
                                if (dataSnapshot.exists()) {
                                    etUsername.setError("Username này đã được đăng ký trước đó!");
                                    return;
                                } else {
                                    // Nếu username chưa được sử dụng, thêm user vào Firebase
                                    user = new Users(username, password, "",role);
                                    myRef.child(username.replace(".", ",")).setValue(user).
                                            addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                                            etUsername.setText("");
                                                            etPassword.setText("");
                                                            Toast.makeText(UserListAdapter.this, "Thêm tài khoản thành công", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Xử lý lỗi khi thao tác với Firebase
                                Toast.makeText(UserListAdapter.this, "Đã xảy ra lỗi khi đăng ký tài khoản", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

// Thực hiện xóa người dùng khi nhấn nút Delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(UserListAdapter.this, "Vui lòng nhập tên người dùng cần xóa", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Xóa người dùng từ cơ sở dữ liệu Firebase
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(username);
                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etUsername.setText("");
                        etPassword.setText("");
                        Toast.makeText(UserListAdapter.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserListAdapter.this, "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        // chỉnh sửa
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                String role = "";
                int selectedId = etRole.getCheckedRadioButtonId();
                if (selectedId == R.id.radiobutton_admin) {
                    role = "admin";
                } else if (selectedId == R.id.radiobutton_user) {
                    role = "user";
                }

                if (!TextUtils.isEmpty(username)) {
                    DatabaseReference myRef = database.getReference("users").child(username);
                    if (!TextUtils.isEmpty(username)) {
                        myRef.child("username").setValue(username);
                    }
                    if (!TextUtils.isEmpty(password)) {
                        myRef.child("password").setValue(password);
                    }
                    if (!TextUtils.isEmpty(role)) {
                        myRef.child("isAdmin").setValue(role);
                    }
                    etUsername.setText("");
                    etPassword.setText("");
                    // Lưu thông tin người dùng vào Firebase Database
                    myRef.push().setValue(new Users(username, password, "",role));

                    Toast.makeText(UserListAdapter.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserListAdapter.this, "Please enter user id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}