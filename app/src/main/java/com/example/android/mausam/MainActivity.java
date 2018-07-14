package com.example.android.mausam;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mausam.adapter.ForecastAdapter;
import com.example.android.mausam.data.WeatherPreference;
import com.example.android.mausam.utils.NetworkUtils;
import com.example.android.mausam.utils.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private ForecastAdapter mForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        mForecastAdapter = new ForecastAdapter(this);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mProgressBar = findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mForecastAdapter);

        loadWeatherData();
    }

    private void loadWeatherData(){
        showWeatherDataView();

        String prefLocation = WeatherPreference.getPreferedWatherLocation(this);
        new WeatherTaskClass().execute(prefLocation);
    }


    private void showWeatherDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage(){
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

        mRecyclerView.setVisibility(View.INVISIBLE);
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
            mForecastAdapter.setmWeatherData(null);

            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String str) {
        Toast.makeText(this, "str", Toast.LENGTH_SHORT).show();
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

               mForecastAdapter.setmWeatherData(strings);

           }else {
               showErrorMessage();
           }
        }
    }
}
