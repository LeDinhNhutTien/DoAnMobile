package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import TienIch.LichActivity;
import TienIch.ThoiTietActivity;
import TienIch.Widget;
import TienIch.WidgetAdapter;
import TienIch.XoSoActivity;
import TienIch.XemChungKhoan;

public class TienIch extends AppCompatActivity {
    private RecyclerView widgetsRecyclerView;
    private WidgetAdapter widgetAdapter;
    private List<Widget> widgetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Ánh xạ RecyclerView
        widgetsRecyclerView = findViewById(R.id.widgetsRecyclerView);

        // Thiết lập layout manager cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        widgetsRecyclerView.setLayoutManager(layoutManager);

        // Khởi tạo danh sách các phần tiện ích
        widgetList = new ArrayList<>();
        widgetList.add(new Widget("Thời tiết", R.drawable.thoi_tiet, ThoiTietActivity.class));
        widgetList.add(new Widget("Lịch", R.drawable.lich_hinhnen, LichActivity.class));
        widgetList.add(new Widget("Kết quả sổ số", R.drawable.xoso, XoSoActivity.class));
        widgetList.add(new Widget("Xem giá chứng khoán", R.drawable.chungkhoan, XemChungKhoan.class));
        // Thêm các phần tiện ích khác vào danh sách

        // Khởi tạo Adapter
        widgetAdapter = new WidgetAdapter(widgetList);

        // Đặt Adapter cho RecyclerView
        widgetsRecyclerView.setAdapter(widgetAdapter);
    }
}
