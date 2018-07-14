package com.example.android.mausam;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.mausam.data.WeatherPreference;
import com.example.android.mausam.utils.NetworkUtils;
import com.example.android.mausam.utils.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = findViewById(R.id.tv_weather_data);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mProgressBar = findViewById(R.id.pb_loading_indicator);

        loadWeatherData();
    }

    private void loadWeatherData(){
        showWeatherDataView();

        String prefLocation = WeatherPreference.getPreferedWatherLocation(this);
        new WeatherTaskClass().execute(prefLocation);
    }


    private void showWeatherDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mWeatherTextView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

        mWeatherTextView.setVisibility(View.INVISIBLE);
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
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

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

            mProgressBar.setVisibility(View.INVISIBLE);

           if (strings != null){

               showWeatherDataView();


               for (String data : strings){

                   mWeatherTextView.append(data + "\n\n\n");
               }
           }else {
               showErrorMessage();
           }
        }
    }
}
