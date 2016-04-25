package com.uppc.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.uppc.R;
import com.uppc.activities.DemoActivity;
import com.uppc.models.Location;

public class MapActivity extends AppCompatActivity {
    private Location mLoc;
    private float logicalDensity;
    private double[] sat;
    private double scale = 1.75 * 1.3;
    private double scaleReal = 23.91; // =6000/248.64

    private int oldleftMargin=0;
    private int oldTopMargin=0;

    private boolean isSatelliteShown ;

    FrameLayout mFrame;
    ImageView mMarker;
    ImageView mMap;
    ImageView mSat;
    TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // custom actionbar
        Toolbar actionbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_app);

        mMap = (ImageView) findViewById(R.id.map);
        mMarker = (ImageView) findViewById(R.id.marker);
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mSat = (ImageView) findViewById(R.id.sat);
        mLog = (TextView) findViewById(R.id.log);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.logicalDensity = metrics.density;

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("locationUpdate"));


        sat= new double[]{258.54 + 3000 / scaleReal, 555.6 + 1950 / scaleReal};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_user) {
            Intent intent = new Intent();
            intent.setClass(this, UserActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location loc = (Location) intent.getSerializableExtra("com.uppc.models.location");
            //glow();
            if (! isSatelliteShown) {
                //showSatellite();
            }
            mLog.setText("LOC:" + loc.toString());
            updateLocation(loc);
        }
    };

    private void showSatellite()
    {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mSat.getLayoutParams();
        params.leftMargin = (int)(sat[0] * scale - 10 * logicalDensity);
        params.topMargin = (int)(sat[1] * scale - 10 * logicalDensity);
        mSat.setLayoutParams(params);
        mSat.setVisibility(View.VISIBLE);

        isSatelliteShown = true;
    }

    private void setShadowEffect(ImageView image) {

    }

    static int i =0;
    private void glow() {
        Drawable drawable = mMarker.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void updateLocation(Location loc)
    {

        double[] markerOffset = {20, 20};

        final int newLeftMargin = (int) ((loc.getX()*10 / scaleReal + sat[0]) * scale  - markerOffset[0] * logicalDensity);
        final int newTopMargin = (int) ((loc.getY()*10 / scaleReal + sat[1])  * scale - markerOffset[1] * logicalDensity);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();
        params.leftMargin = newLeftMargin;
        params.topMargin = newTopMargin;
        mMarker.setLayoutParams(params);
        mMarker.setVisibility(View.VISIBLE);

        glow();
        //move(newLeftMargin, newTopMargin);
    }

    private void move(int newLeftMargin, int newTopMargin) {
        TranslateAnimation move = new TranslateAnimation(
                oldleftMargin, newLeftMargin, oldTopMargin, newTopMargin
        );
        move.setDuration(2000);
        move.setFillAfter(true);
        move.setFillEnabled(true);

        move.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mMarker.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                glow();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        oldleftMargin = newLeftMargin;
        oldTopMargin = newTopMargin;

        mMarker.startAnimation(move);
    }
}
