package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class TheThao extends AppCompatActivity {

    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<DocBao> arrayDocBao;
    ArrayList<String> arrayLink;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_thao);

        lv = (ListView) findViewById(R.id.listViewTheThao);

        arrayLink = new ArrayList<>();
        arrayDocBao = new ArrayList<DocBao>();

        new ReadRss().execute("https://vnexpress.net/rss/the-thao.rss");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TheThao.this,NewsActivity.class);
                intent.putExtra("linkTinTuc",arrayLink.get(i));
                startActivity(intent);
            }
        });
    }

    private class ReadRss extends AsyncTask<String, Void, String> {
        StringBuilder content = new StringBuilder();
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
                arrayLink.add(parser.getValue(element,"link"));
                arrayDocBao.add(new DocBao(tieude,link,hinh,ngay));
            }
            customAdapter = new CustomAdapter(TheThao.this, android.R.layout.simple_list_item_1, arrayDocBao);
            lv.setAdapter(customAdapter);
        }
    }
}