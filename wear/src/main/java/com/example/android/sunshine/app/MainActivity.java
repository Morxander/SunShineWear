package com.example.android.sunshine.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private TextView txt_clock,txt_date,txt_degree_h,txt_degree_s;
    private ImageView ic_weather;
    private static final String PREFERENCE_NAME = "WeatherPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                txt_clock = (TextView) stub.findViewById(R.id.txt_clock);
                txt_date = (TextView) stub.findViewById(R.id.txt_date);
                txt_degree_h = (TextView) stub.findViewById(R.id.txt_degree_h);
                txt_degree_s = (TextView) stub.findViewById(R.id.txt_degree_s);
                ic_weather = (ImageView)stub.findViewById(R.id.ic_weather);
                // New thread to keep the clock up to date
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            while (!isInterrupted()) {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        SimpleDateFormat sdf2 = new SimpleDateFormat("E, MMM d yyyy");
                                        String currentDateandTime = sdf.format(new Date());
                                        String currentDateandTime2 = sdf2.format(new Date());
                                        txt_clock.setText(currentDateandTime);
                                        txt_date.setText(currentDateandTime2);
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            Log.v("sunshine",e.getLocalizedMessage());
                        }
                    }
                };

                t.start();
                SharedPreferences pref = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
                String weather_h = pref.getString("weather_h", "");
                String weather_s = pref.getString("weather_s", "");
                int weatherId = pref.getInt("weather_id",0);
                ic_weather.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
                if(weather_h != null){
                    txt_degree_h.setText("" + weather_h + "°");
                }
                if(weather_s != null){
                    txt_degree_s.setText("" + weather_s + "°");
                }
            }
        });
    }
}
