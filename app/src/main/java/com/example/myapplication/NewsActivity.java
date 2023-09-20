package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class NewsActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog dialog;
    ImageView tvTrolai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        webView = (WebView) findViewById(R.id.webViewTinTuc);
        tvTrolai = findViewById(R.id.tvTrolai);

        Intent intent = getIntent();
        String link = intent.getStringExtra("linkTinTuc");

        if (link != null) {
            dialog = new ProgressDialog(NewsActivity.this);
            dialog.setMessage("Đang tải...");
            dialog.setCancelable(false);
            dialog.show();
            // dùng để khi nhấn vào những cái link báo khác thì nó vẫn ở trong app chứ nhảy ra khỏi app
            webView.setWebViewClient(onWebViewLoaded);
            webView.loadUrl(link);
        }

        tvTrolai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityHome();
            }
        });
    }
    private WebViewClient onWebViewLoaded = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            dialog.dismiss();
            Toast.makeText(NewsActivity.this, "Co loi!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(NewsActivity.this);
            builder.setMessage("Bạn cần kết nối internet để sử dụng ứng dụng này")
                    .setTitle("Không có kết nối internet");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
    public void doOpenActivityHome() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}