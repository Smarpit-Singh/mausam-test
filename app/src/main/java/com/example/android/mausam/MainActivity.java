package com.example.android.mausam;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.ForecastAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String[]> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FORECAST_LOADER_ID = 0;
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

        getLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
    }


    private void showWeatherDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
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

        if (id == R.id.action_refresh) {

            invalidateData();
            getLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            return true;

        } else if (id == R.id.action_map) {
            openLocationInMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLocationInMap() {
        Uri geoLoc = Uri.parse("geo:0,0?q=1600 Ampitheatre Parkway, CA");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLoc);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLoc.toString()
                    + ", no receiving apps installed!");
        }

    }

    @Override
    public void onClick(String str) {
        startActivity(new Intent(MainActivity.this, DetailActivity.class));
    }

    private void invalidateData() {
        mForecastAdapter.setmWeatherData(null);
    }


    
    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {

        return new WeatherAsyncTaskLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (data == null) {
            showErrorMessage();
        } else {
            showWeatherDataView();
            mForecastAdapter.setmWeatherData(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }



    private class WeatherAsyncTaskLoader extends AsyncTaskLoader<String[]> {

        public WeatherAsyncTaskLoader(Context context) {
            super(context);
        }

        String[] mWeatherData = null;

        @Override
        protected void onStartLoading() {
            if (mWeatherData != null) {
                deliverResult(mWeatherData);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

        }

        @Override
        public String[] loadInBackground() {

            String urlQ = WeatherPreference.getPreferedWatherLocation(getApplicationContext());

            URL url = NetworkUtils.buildUrl(urlQ);

            String[] result;
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(url);
                result = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(getApplicationContext(), response);

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
        public void deliverResult(String[] data) {
            mWeatherData = data;
            super.deliverResult(data);
        }
    }


}
