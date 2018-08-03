package com.example.android.mausam.sync;

import android.app.IntentService;
import android.content.Intent;

public class SunshineSyncIntentService extends IntentService {


    public SunshineSyncIntentService() {
        super("SunshineSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }

}
