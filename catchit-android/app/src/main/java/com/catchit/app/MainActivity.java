package com.catchit.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.catchit.lib.CatchIt;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.caught_exception_button).setOnClickListener(v -> {
            CatchIt.logException(new Exception("hello caught exception"));
        });
        findViewById(R.id.uncaught_exception_button).setOnClickListener(v -> {
            String nullString = null;
            nullString.toString();
        });
    }
}