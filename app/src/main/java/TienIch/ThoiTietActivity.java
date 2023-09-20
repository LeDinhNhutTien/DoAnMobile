package TienIch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThoiTietActivity extends AppCompatActivity {
    EditText editSearch;
    Button btnSearch;
    TextView txtName, txtCountry, txtTemp, txtStatus, txthumidity, txtcloud, txtWind, txtDay;
    String City ="";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_thoi_tiet);
        Anhxa();
        String city = "Saigon";
                    GetCurrentWeatherData(city);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editSearch.getText().toString();
                if (city == "Saigon"){
                    GetCurrentWeatherData(city);
                }else{
                    GetCurrentWeatherData(city);
                }
            }
        });

    }
    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(ThoiTietActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=6092b2bebf1851bb022329c76ddce1f1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText("Tên thành phố: "+name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                            String Day = simpleDateFormat.format(date);

                            txtDay.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());

                            txtTemp.setText(Nhietdo+"°C");
                            txthumidity.setText(doam+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+"m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");
                            txtcloud.setText(may+"%");

//                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
//                            String country = jsonObjectSys.getString("country");
//                            txtCountry.setText("Tên quốc gia: "+country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(stringRequest);

    }

    private void Anhxa(){
        editSearch = (EditText) findViewById(R.id.edittextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        txtName = (TextView) findViewById(R.id.textviewName);
        txtcloud = (TextView) findViewById(R.id.textviewCloud);
//        txtCountry = (TextView) findViewById(R.id.textviewCountry);
        txthumidity = (TextView) findViewById(R.id.textviewHumidity);
        txtDay = (TextView) findViewById(R.id.textviewDay);
        txtStatus = (TextView) findViewById(R.id.textviewStatus);
        txtTemp = (TextView) findViewById(R.id.textviewTemp);
        txtWind = (TextView) findViewById(R.id.textviewWind);

    }


}

