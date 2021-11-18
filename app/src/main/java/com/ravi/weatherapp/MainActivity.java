package com.ravi.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editCityName;
    Button searchCityBtn;
    ImageView imageView;
    TextView countryTv, cityTv, tempTv;
    TextView latTv, logTv, humidityTv,sunSetTv, sunriseTv, windSpeedTv,pressureTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCityName=findViewById(R.id.cityNameEt);
        searchCityBtn=findViewById(R.id.searchCityBtn);
        imageView=findViewById(R.id.imageView);
        countryTv=findViewById(R.id.countryTv);
        cityTv=findViewById(R.id.cityTv);
        tempTv=findViewById(R.id.temperatureTv);

        latTv=findViewById(R.id.latitudeTv);
        logTv=findViewById(R.id.longitudeTv);
        humidityTv=findViewById(R.id.humidityTv);
        sunSetTv=findViewById(R.id.sunsetTv);
        sunriseTv=findViewById(R.id.sunriseTv);
        windSpeedTv=findViewById(R.id.windSpeedTv);
        pressureTv=findViewById(R.id.pressureTv);



        searchCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findWeather();
            }
        });
    }

    public void findWeather(){
        String city=editCityName.getText().toString();
        String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=62b3d923f2a91210059423442856ea45&units=metric";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //calling api
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            //To find country name
                            JSONObject object=jsonObject.getJSONObject("sys");
                            String countryName=object.getString("country");

//                            //To find Sunset and Sunrise
                            long sunrise=object.getLong("sunrise");
                            long sunset=object.getLong("sunset");

                            //To find the city name
                            String cityName=jsonObject.getString("name");

                            //To find temperature
                            JSONObject object1=jsonObject.getJSONObject("main");
                            String temperature=object1.getString("temp");

//                            //To find humidity
                            double humidity=object1.getDouble("humidity");
//
//                            //To find the pressure
                            int pressure=object1.getInt("pressure");

                            //To get icon from server
                            JSONArray jsonArray=jsonObject.getJSONArray("weather");
                            JSONObject iconJsonObject=jsonArray.getJSONObject(0);
                            String icon=iconJsonObject.getString("icon");
                            Picasso.get().load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

                            //To find latitude and longitude
                            JSONObject coordObj=jsonObject.getJSONObject("coord");
                            double lat=coordObj.getDouble("lat");
                            double lon=coordObj.getDouble("lon");

                            //To find the wind speed
                            JSONObject windObject=jsonObject.getJSONObject("wind");
                            double windspeed=windObject.getDouble("speed");

                            countryTv.setText(countryName);
                            cityTv.setText(cityName);
                            tempTv.setText(temperature+" °C");
                            latTv.setText(lat+"° N");
                            logTv.setText(lon+"° E");
                            humidityTv.setText(humidity+" %");
                            sunriseTv.setText(sunrise+"");
                            sunSetTv.setText(sunset+"");
                            windSpeedTv.setText(windspeed+" Km/h");
                            pressureTv.setText(pressure+" hPa");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: "+error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
}