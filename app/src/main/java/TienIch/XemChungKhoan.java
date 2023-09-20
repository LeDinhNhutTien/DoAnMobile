package TienIch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.myapplication.R;

public class XemChungKhoan extends AppCompatActivity {

    WebView webView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chung_khoan);

        webView = (WebView) findViewById(R.id.webViewChungKhoan);

        Intent intent = getIntent();
        String link1 = "https://banggia.dnse.com.vn/";

        if (link1 != null) {
            dialog = new ProgressDialog(XemChungKhoan.this);
            dialog.setMessage("Đang tải...");
            dialog.setCancelable(false);
            dialog.show();
            // dùng để khi nhấn vào những cái link báo khác thì nó vẫn ở trong app chứ nhảy ra khỏi app
            webView.setWebViewClient(onWebViewLoaded);
            webView.loadUrl(link1);
        }
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
            Toast.makeText(XemChungKhoan.this, "Co loi!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(XemChungKhoan.this);
            builder.setMessage("Bạn cần kết nối internet để sử dụng ứng dụng này")
                    .setTitle("Không có kết nối internet");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
}