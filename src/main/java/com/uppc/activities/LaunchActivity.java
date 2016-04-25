package com.uppc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.uppc.LocationService;
import com.uppc.R;

public class LaunchActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // start location service
        Context context = getApplicationContext();
        Intent i = new Intent(context, LocationService.class);
        context.startService(i);

        // custom actionbar
        Toolbar actionbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_app);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                gotoMap();
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void gotoMap()
    {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }
}
