package com.uppc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.uppc.activities.MapActivity;
import com.uppc.lib.uppc;
import com.uppc.models.Location;

import java.io.Serializable;

/**
 * Created by songlinwei on 16/4/12.
 */
public class LocationService extends Service {
    private int NOTIFICATION = R.string.location_service_started;
    private NotificationManager mNM;
    private uppc mUPPC = null;
    private final int UPPC_UPDATE_MSG = 1000;
    private Location mCurrentLocation;

    public native String stringFromJNI();
    public native String stringLibFromJNI();
    public native String stringLibUUIDJNI();

    static {
        System.loadLibrary("uppc-jni");
    }

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        /* Step 0: Create UPPC Application. */
        mUPPC = new uppc();
		/* Step 1: Initialize UPPC. */
        mUPPC.init();
		/* Step 2: Start UPPC thread. */
        mUPPC.start();
        new Thread(new UPPCThread()).start();

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        if(mUPPC!=null){
            mUPPC.stop();
        }
        // Tell the ic_user we stopped.
        Toast.makeText(this, R.string.location_service_stopped, Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocationBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.location_service_started);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MapActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)  // the status icon FIXME
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.location_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        mNM.notify(NOTIFICATION, notification);
    }

    class UPPCThread implements Runnable {
        public void run() {
            while (! Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = UPPC_UPDATE_MSG;
                int[] mXYZ = new int[4];
                String sPos;
                if (mUPPC.getLocation(mXYZ)) {
                    sPos = "X: " + String.format("%d", mXYZ[0])
                            + "cm  Y: " + String.format("%d", mXYZ[1])
                            + "cm  Z: " + String.format("%d", mXYZ[2]) + "cm";
                    message.obj = sPos;
                    System.out.println(sPos);
                    mCurrentLocation = new Location(mXYZ);

                    Intent i = new Intent("locationUpdate");
                    i.putExtra("com.uppc.models.location", (Serializable) mCurrentLocation);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }
}