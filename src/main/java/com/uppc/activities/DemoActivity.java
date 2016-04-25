package com.uppc.activities;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.uppc.R;
import com.uppc.models.Location;

public class DemoActivity extends ActionBarActivity {
    private Location mLoc;

    FrameLayout mFrame;
    ImageView mMarker;
    ImageView mMap;
    ImageView mSat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMap = (ImageView) findViewById(R.id.map);
        mMarker = (ImageView) findViewById(R.id.marker);
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mSat = (ImageView) findViewById(R.id.sat);
        mFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glow();
            }
        });
        mMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glow();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("locationUpdate"));
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
            mLoc = (Location) intent.getSerializableExtra("com.uppc.models.location");
            glow();
        }
    };

    private void addShadowEffect(ImageView image) {

    }

    static int i =0;
    private void glow() {
        Drawable drawable = mMarker.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        //FIXME
        updateLocation();
    }

    int oldleftMargin=0;
    int oldTopMargin=0;


    private void updateLocation()
    {
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mMarker.getLayoutParams();
        // TODO new position
//        params.leftMargin = 100;
//        params.topMargin = 100;
//        mMarker.setLayoutParams(params);

        int mapWidth = mMap.getWidth();
        int mapHeight = mMap.getHeight();

        int frameWidth = mFrame.getWidth();
        int frameHeight = mFrame.getHeight();

        double canvasViewportWidth = 595.22;
        double canvasViewportHeight = 842;

        double[] Os = {297.61, 421};


        double scaleReal = 23.91; // =6000/248.64
        double[] sat= {258.54 + 3000/scaleReal , 555.6 + 1950/scaleReal};
        double[][] target = {sat, {258.54, 555.6},  {507.18, 555.6}, {517.08,753.66}};


        //ImageView sat = mMarker;

        //mSat.requestLayout();


        // 因为图片是水平居中的,跟scaleType有关,要推敲
//        double scaleX =  mapWidth / (canvasViewportWidth / 0.8) ;
//        double scaleY =  mapHeight / (canvasViewportHeight / 0.8);
//        double scale = Math.max (scaleX, scaleY);

        double scale = 1.75 * 1.3; // half local density?

        /*width="199.93294"
        height="128.03278"
        x="286.33929"
        y="581.51953"*/

        //DisplayMetrics metrics = getResources().getDisplayMetrics();
        double[][] traces = {{286.33929,581.51953}, {286.33929+199.93294,581.51953}, {286.33929+199.93294, 581.51953+128.03278}, {286.33929,581.51953+128.03278}};

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        double[] markerOffset = {20, 20};

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mSat.getLayoutParams();

        params.leftMargin = (int)(sat[0] * scale - 10 * logicalDensity);//+ 100;
        params.topMargin = (int)(sat[1] * scale - 10 * logicalDensity);//+ 100;
        mSat.setLayoutParams(params);
        mSat.setVisibility(View.VISIBLE);





//        final int newLeftMargin = (int)(286.33929 * scale  - markerOffset[0]*logicalDensity) ;
//        final int newTopMargin = (int)(581.51953  * scale - markerOffset[1]*logicalDensity);
        final int newLeftMargin = (int) ((target[i%4][0]) * scale  - markerOffset[0]*logicalDensity) ; //507.18;
        final int newTopMargin = (int) ((target[i%4][1])  * scale - markerOffset[1]*logicalDensity);//555.6;


//581,1036-581,1252,1252-1252
        TranslateAnimation move = new TranslateAnimation(
                /*Animation.ABSOLUTE, */oldleftMargin,
                /*Animation.ABSOLUTE, */newLeftMargin/*-oldleftMargin*/,
                /*Animation.ABSOLUTE, */oldTopMargin,
                /*Animation.ABSOLUTE, */newTopMargin/*-oldTopMargin*/
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
                //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();

                //params.leftMargin = newLeftMargin ;//+ 100;
                //params.topMargin = newTopMargin ;//+ 100;
                //mMarker.setLayoutParams(params);
                //mMarker.requestLayout();

                glow();
//                updateLocation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        oldleftMargin = newLeftMargin;
        oldTopMargin = newTopMargin;


        mMarker.startAnimation(move);
        i++;


       /* mMarker = new ImageView(this);
        //mMarker.setOnClickListener();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(40, 40);
        params.topMargin = startTopMargin;
        params.leftMargin = startLeftMargin;
        mFrame.addView(mMarker);*/

        /*FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();

        params.leftMargin = newLeftMargin;
        params.topMargin = newTopMargin;
        mMarker.setLayoutParams(params);

        mMarker.setVisibility(View.VISIBLE);   */



        /*Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();
                params.leftMargin = (int)(newLeftMargin * interpolatedTime);
                params.topMargin = (int)(newTopMargin * interpolatedTime);

                mMarker.setLayoutParams(params);
            }
        };


        a.setDuration(3000);
//        a.setFillEnabled(true);
//        a.setFillAfter(true);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();

                params.leftMargin = newLeftMargin ;//+ 100;
                params.topMargin = newTopMargin ;//+ 100;
                mMarker.setLayoutParams(params);
                mMarker.requestLayout();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mMarker.startAnimation(a);*/
//
//        double[] newM = {newTopMargin, newLeftMargin};
//        mMarker.startAnimation(new MarkerMoveAnim(newM, null, 3000));

//        final int oldLeftMargin = i ==1 ? 0 : (int) ((traces[(i-2)%4][0]) * scale  - markerOffset[0]*logicalDensity) ; //507.18;
//        final int oldTopMargin = i == 1 ? 0: (int) ((traces[(i-2)%4][1])  * scale - markerOffset[1]*logicalDensity);//555.6;
//
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mMarker, "marginLeft", oldLeftMargin, newLeftMargin );
//        objectAnimator.setDuration(3000);
//        objectAnimator.start();
    }

    /*private void updateLocation()
    {
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mMarker.getLayoutParams();
        // TODO new position
//        params.leftMargin = 100;
//        params.topMargin = 100;
//        mMarker.setLayoutParams(params);

        int Wm = mMap.getWidth();
        int Hm = mMap.getHeight();

        double Ws = 595.22;
        double Hs = 842;

        double[][] targets = {{297.61, 421}, {79.56, 102.24},  {507.18, 555.6}, {517.08,753.66}};
        i++;
        //double[] target = {507.18, 555.6};
        //double[] target = {517.08,753.66};





        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        double[] markerOffset = {20, 20};

        // SVG的中心点
        double[] Os = {297.61, 421};
        // MapView的中心点
        double Omx = Os[0] * Wm / Ws;
        double Omy = Os[1] * Hm / Hs;
        double[] Om = {Omx, Omy};
        // 缩放比例
        double scale = Wm / Ws; // or = Hm / Hs
        // svg目标坐标
        double[] target = targets[i%4];

        final int newLeftMargin = (int) (Os[0] *scale - markerOffset[0]*logicalDensity);
        final int newTopMargin = (int)(Os[1] * scale - markerOffset[1]*logicalDensity);

//        final int newLeftMargin = (int) ((Os[0]) * Wm / Ws  - markerOffset[0]*logicalDensity) ; //507.18;
//        final int newTopMargin = (int) ((Os[1]) * Hm / Hs  - markerOffset[1]*logicalDensity);//555.6;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();
                params.leftMargin = (int)(newLeftMargin * interpolatedTime);
                params.topMargin = (int)(newTopMargin * interpolatedTime);

                mMarker.setLayoutParams(params);
            }
        };

        a.setDuration(3000);
        mMarker.startAnimation(a);


    }*/

    class MarkerMoveAnim extends Animation {
        private double[] newMargin;
        private double[] oldMargin;
        private int duration;

        MarkerMoveAnim(final double[] newMargin, double[] oldMargin, int duration) {
            this.newMargin = newMargin;
            this.oldMargin = oldMargin;
            this.duration = duration;
            setDuration(duration);

            setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // start a new anim
                    double[] newNewLog = {newMargin[0]+1, newMargin[1]+1};
                    MarkerMoveAnim newa = new MarkerMoveAnim(newNewLog, null, 3000);
                    mMarker.startAnimation(newa);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mMarker.getLayoutParams();
            params.topMargin = (int)(this.newMargin[0] * interpolatedTime);
            params.leftMargin = (int)(this.newMargin[1] * interpolatedTime);

            mMarker.setLayoutParams(params);
        }
    }
}
