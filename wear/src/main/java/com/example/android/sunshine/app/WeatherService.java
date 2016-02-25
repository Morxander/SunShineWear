package com.example.android.sunshine.app;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class WeatherService extends WearableListenerService {
    private static final String PREFERENCE_NAME = "WeatherPreference";
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        for (DataEvent dataEvent : dataEvents){
            if(dataEvent.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                if(path.equals("/weather")){
                    double weather_h = dataMap.getDouble("weather_h");
                    double weather_s = dataMap.getDouble("weather_s");
                    int weather_id = dataMap.getInt("weather_id");
                    long timestamp = dataMap.getLong("timestamp");
                    SharedPreferences pref = getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("weather_h", String.valueOf(weather_h));
                    editor.putString("weather_s", String.valueOf(weather_s));
                    editor.putInt("weather_id", weather_id);
                    editor.putLong("timestamp",timestamp);
                    editor.commit();
                }
            }
        }
    }
}

