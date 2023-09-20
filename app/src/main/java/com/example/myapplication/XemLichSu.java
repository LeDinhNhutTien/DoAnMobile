package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityUserInfoBinding;
import com.example.myapplication.databinding.ActivityXemLichSuBinding;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class XemLichSu extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference reference;
    private ListView mListView;
    ImageView tvTrolai;

    ActivityXemLichSuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXemLichSuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  history vào Firebase
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("history");

        mListView = binding.lv;
        tvTrolai = binding.tvTrolai;

        FirebaseListOptions<DocBao> options = new FirebaseListOptions.Builder<DocBao>()
                .setLayout(R.layout.dong_layout_listview)
                .setQuery(reference, DocBao.class)
                .build();

        FirebaseListAdapter<DocBao> adapter = new FirebaseListAdapter<DocBao>(options) {
            @Override
            protected void populateView(@androidx.annotation.NonNull View v, @androidx.annotation.NonNull DocBao model, int position) {
                // Gán dữ liệu của mỗi người dùng vào các thành phần trong layout
                ImageView tvImage = v.findViewById(R.id.imageView);
                TextView tvTitle = v.findViewById(R.id.textViewTitle);
                TextView tvDate= v.findViewById(R.id.textViewDate);

                Picasso.get().load(model.image).into(tvImage);
                tvTitle.setText(model.getTitle());
                tvDate.setText(model.getDate());
            }
        };
        // Kết nối adapter với ListView và bắt đầu lắng nghe
        adapter.startListening();
        // Đặt adapter cho ListView
        mListView.setAdapter(adapter);

        tvTrolai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityAccount();
            }
        });
    }
    public void doOpenActivityAccount() {
        Intent myIntent = new Intent(this, Account.class);
        startActivity(myIntent);
    }
}