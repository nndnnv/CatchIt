package com.catchit.app;

import android.app.Application;

import com.catchit.lib.CatchIt;
import com.catchit.lib.CatchItOptions;

import java.util.concurrent.TimeUnit;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CatchIt.start(this,
                new CatchItOptions.Builder()
                    .setServerAddress("http://192.168.1.100")
                    .setSyncInterval(5, TimeUnit.SECONDS).build());
    }
}
