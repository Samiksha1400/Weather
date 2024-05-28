package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    EditText enterCity;
    TextView showCity, humidity, pressure, description, country, sunrise, sunset, wind, clouds, date_time;
    ImageView locationIcon/*,weatherIcon*/;


    /*final String APP_ID = "fb48d85f23d77786b5900d5b5453bb78";
    final String WEATHER_URL ="https://api.openweathermap.org/data/2.5/weather";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String location_Provider = LocationManager.GPS_PROVIDER;

    LocationManager locationManager;
    LocationListener locationListener;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
       // weatherIcon = findViewById(R.id.weatherIcon);

        //enterCity.setText(Locale.GERMAN);
    }

    public void get(View view)
    {
        String city = enterCity.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=fb48d85f23d77786b5900d5b5453bb78";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                locationIcon.setVisibility(View.VISIBLE);
                try {

                    String cityname = response.getString("name");
                    JSONObject sys = response.getJSONObject("sys");
                    String countryName = sys.getString("country");
                    country.setText(countryName + "," + cityname);

                    JSONObject object = response.getJSONObject("main");
                    String temperature = object.getString("temp");
                    Double temp = Double.parseDouble(temperature) - 273.15;
                    showCity.setText(temp.toString().substring(0, 2) + "°c");

                    JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                    String desc = weather.getString("description");
                    description.setText(desc);

                    Long showDateTime = response.getLong("dt");
                    String updateDateTime = "Last Updated at : " + new SimpleDateFormat("dd/MM/yyyy hh:mm: a", Locale.ENGLISH).format(new Date(showDateTime * 1000));
                    date_time.setText(updateDateTime);

                    String humi_dity = object.getString("humidity");
                    humidity.setText(humi_dity + "%");

                    String airPressure = object.getString("pressure");
                    pressure.setText(airPressure + "hPa");

                    JSONObject cloud = response.getJSONObject("clouds");
                    String cloudinfo = cloud.getString("all");
                    clouds.setText(cloudinfo + "%");

                    Long rise = sys.getLong("sunrise");
                    String sun_rise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                    sunrise.setText(sun_rise);

                    Long set = sys.getLong("sunset");
                    String sun_set = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));
                    sunset.setText(sun_set);

                    JSONObject windd = response.getJSONObject("wind");
                    String windspeed = windd.getString("speed");
                    wind.setText(windspeed + "m/s");

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    //Fetch Current Location
    /*@Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }*/

    /*private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                networkingMethod(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Toast.makeText(MainActivity.this, "Not able to get location", Toast.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(location_Provider, MIN_TIME, MIN_DISTANCE, locationListener);

    }*/

    //Whether the user allow location or not
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Location get Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied permission
            }
        }
    }*/

    //
    /*private  void networkingMethod(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                //super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this, "Data get sucessfully", Toast.LENGTH_SHORT).show();

                weatherData weatherD = weatherData.fromJson(response);
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    /*private  void updateUI(weatherData weather)
    {
        showCity.setText(weather.getmTemperature());
        country.setText(weather.getmCity());
        description.setText(weather.getWeatherType());
        int resourceID=getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(locationManager!=null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }*/







    public void networkService(String url)
    {
        String requestURL = url;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(0, requestURL, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}



