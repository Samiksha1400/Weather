package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    EditText enterCity;
    TextView showCity,humidity,pressure,description,country,sunrise,sunset,wind,clouds,date_time;
    ImageView locationIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterCity = findViewById(R.id.enterCity);
        showCity = findViewById(R.id.showCity);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        description = findViewById(R.id.description);
        country = findViewById(R.id.country);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        wind = findViewById(R.id.wind);
        clouds = findViewById(R.id.clouds);
        date_time = findViewById(R.id.date_time);
        locationIcon = findViewById(R.id.locationIcon);
    }

    public void get(View view)
    {
        String apikey = "fb48d85f23d77786b5900d5b5453bb78";
        String city = enterCity.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=fb48d85f23d77786b5900d5b5453bb78";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                locationIcon.setVisibility(View.VISIBLE);
                try {

                    String cityname = response.getString("name");
                    JSONObject sys = response.getJSONObject("sys");
                    String countryName =sys.getString("country");
                    country.setText(countryName+","+cityname);

                    JSONObject object = response.getJSONObject("main");
                    String temperature = object.getString("temp");
                    Double temp = Double.parseDouble(temperature) -273.15;
                    showCity.setText(temp.toString().substring(0,2)+"°c");

                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                    String desc = weather.getString("description");
                    description.setText(desc);

                    Long showDateTime = response.getLong("dt");
                    String updateDateTime = "Last Updated at : "+new SimpleDateFormat("dd/MM/yyyy hh:mm: a",Locale.ENGLISH).format(new Date(showDateTime * 1000));
                    date_time.setText(updateDateTime);

                    String humi_dity = object.getString("humidity");
                    humidity.setText(humi_dity+"%");

                    String airPressure = object.getString("pressure");
                    pressure.setText(airPressure+"hPa");

                    JSONObject cloud = response.getJSONObject("clouds");
                    String cloudinfo =cloud.getString("all");
                    clouds.setText(cloudinfo+"%");

                    Long rise = sys.getLong("sunrise");
                    String sun_rise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                    sunrise.setText(sun_rise);

                    Long set = sys.getLong("sunset");
                    String sun_set = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));
                    sunset.setText(sun_set);

                    JSONObject windd = response.getJSONObject("wind");
                    String windspeed = windd.getString("speed");
                    wind.setText(windspeed+"m/s");

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}