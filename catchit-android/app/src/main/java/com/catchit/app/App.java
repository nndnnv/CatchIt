package com.catchit.app;

import android.app.Application;

import com.catchit.lib.CatchIt;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CatchIt.start(this, "MOCK_API_KEY");
    }
}
