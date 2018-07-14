package com.example.android.mausam;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.mausam.data.WeatherPreference;
import com.example.android.mausam.utils.NetworkUtils;
import com.example.android.mausam.utils.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = findViewById(R.id.tv_weather_data);

        loadWeatherData();
    }

    private void loadWeatherData(){
        String prefLocation = WeatherPreference.getPreferedWatherLocation(this);
        new WeatherTaskClass().execute(prefLocation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            mWeatherTextView.setText("");
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public class WeatherTaskClass extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... urlLoc) {

            if (urlLoc.length == 0)return null;

            URL url = NetworkUtils.buildUrl(urlLoc[0]);

            String[] result;
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(url);
               result =  OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(getApplicationContext(), response);

               return result;
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            } catch (JSONException e) {
                e.printStackTrace();

                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] strings) {
           if (strings != null){

               for (String data : strings){

                   mWeatherTextView.append(data + "\n\n\n");
               }
           }
        }
    }
}
