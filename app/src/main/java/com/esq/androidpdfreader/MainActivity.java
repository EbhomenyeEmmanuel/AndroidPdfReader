package com.esq.androidpdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handlerMethod(){
        int SPLASH_TIME = 4000;
        new Handler().postDelayed(() -> {
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            Log.i("MainActivity", "Intent delayed for 4 secs");
        }, SPLASH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handlerMethod();
    }
}
