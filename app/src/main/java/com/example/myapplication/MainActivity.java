package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    CardView kinhDoanh, giaiTri, sucKhoe, khoaHoc,theThao,xe, tien_ich,account;
    private long backPressed;
    CustomAdapter customAdapter;
    ArrayList<String> arrayTitle;
    ArrayList<String> arrayLink, arrayDate, arrayImage;
    ArrayList<DocBao> arrayDocBao;
    FirebaseDatabase db;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);
        kinhDoanh = findViewById(R.id.kinhDoanh);
        giaiTri = findViewById(R.id.giaiTri);
        sucKhoe = findViewById(R.id.sucKhoe);
        khoaHoc = findViewById(R.id.khoaHoc);
        theThao = findViewById(R.id.theThao);
        xe = findViewById(R.id.xe);
        tien_ich = findViewById(R.id.tienich);
        account = findViewById(R.id.account);

        arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
        arrayDate = new ArrayList<>();
        arrayImage = new ArrayList<>();
        arrayDocBao = new ArrayList<DocBao>();

        // Thêm history vào Firebase
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("history");

        new ReadRSS().execute("https://vtc.vn/rss/feed.rss");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                intent.putExtra("linkTinTuc",arrayLink.get(i));
                startActivity(intent);
                //
                // Kiểm tra xem title đã tồn tại trước đó trong Firebase chưa
                reference.orderByChild("title").equalTo(arrayTitle.get(i)).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Nếu title đã được lưu rồi thì thôi
                                if (dataSnapshot.exists()) {
                                    return;
                                } else {
                                    // Nếu title chưa được lưu, thêm title vào Firebase
                                    DocBao docbao = new DocBao(arrayTitle.get(i),arrayLink.get(i),arrayImage.get(i),arrayDate.get(i));
                                    reference.child(arrayTitle.get(i).replace(".", ",")).setValue(docbao).
                                            addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    }
                                            );
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

        tien_ich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityTien_ich();
            }
        });

        kinhDoanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityKinhDoanh();
            }
        });

        giaiTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityGiaiTri();
            }
        });

        sucKhoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivitySucKhoe();
            }
        });

        khoaHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityKhoaHoc();
            }
        });

        theThao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityTheThao();
            }
        });

        xe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityXe();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOpenActivityAccount();
            }
        });

    }

    public void doOpenActivityAccount(){
        Intent myIntent = new Intent(this, Account.class);
        startActivity(myIntent);
    }
    public void doOpenActivityTien_ich(){
        Intent myIntent = new Intent(this, TienIch.class);
        startActivity(myIntent);
    }
    public void doOpenActivityKinhDoanh(){
        Intent myIntent = new Intent(this, KinhDoanh.class);
        startActivity(myIntent);
    }
    public void doOpenActivityGiaiTri(){
        Intent myIntent = new Intent(this, GiaiTri.class);
        startActivity(myIntent);
    }
    public void doOpenActivitySucKhoe(){
        Intent myIntent = new Intent(this, SucKhoe.class);
        startActivity(myIntent);
    }
    public void doOpenActivityKhoaHoc(){
        Intent myIntent = new Intent(this, KhoaHoc.class);
        startActivity(myIntent);
    }
    public void doOpenActivityTheThao(){
        Intent myIntent = new Intent(this, TheThao.class);
        startActivity(myIntent);
    }
    public void doOpenActivityXe(){
        Intent myIntent = new Intent(this, Xe.class);
        startActivity(myIntent);
    }


    private class  ReadRSS extends AsyncTask<String, Void, String>{
        StringBuilder content = new StringBuilder();
        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDescription = document.getElementsByTagName("description");

            String tieude ="";
            String link ="";
            String hinh ="";
            String ngay ="";

            for (int i =0; i< nodeList.getLength(); i++){
                String cdata = nodeListDescription.item(i+1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher m = p.matcher(cdata);
                if (m.find()){
                    hinh = m.group(1);
                }

                Element element = (Element) nodeList.item(i);
                tieude = parser.getValue(element,"title") ;
                link = parser.getValue(element,"link") ;
                ngay = parser.getValue(element,"pubDate");
                arrayTitle.add(tieude);
                arrayDate.add(ngay);
                arrayImage.add(hinh);
                arrayLink.add(link);
                arrayDocBao.add(new DocBao(tieude,link,hinh,ngay));
            }
            customAdapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayDocBao);
            lv.setAdapter(customAdapter);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Đang tải...");
            dialog.setCancelable(false);
            dialog.show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timkiembao, menu);
        MenuItem menuItem = menu.findItem(R.id.menuseach);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}