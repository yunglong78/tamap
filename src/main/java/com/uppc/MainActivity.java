package com.uppc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.uppc.activities.MapActivity;
import com.uppc.lib.uppc;

public class MainActivity extends ActionBarActivity {
    private TextView iUPPC;
    private TextView iUUID;
    private TextView iPosition;
    private uppc mUPPC = null;
    private final int UPPC_UPDATE_MSG = 1000;

    public native String stringFromJNI();
    public native String stringLibFromJNI();
    public native String stringLibUUIDJNI();
    public native int add(int a, int b);

    static {
        System.loadLibrary("uppc-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iUPPC = (TextView) findViewById(R.id.textView1);
        iUUID = (TextView) findViewById(R.id.textView2);
        iPosition = (TextView) findViewById(R.id.textView3);

        iUPPC.setText(stringLibFromJNI());
        iUUID.setText(stringLibUUIDJNI());

		/* Step 0: Create UPPC Application. */
        mUPPC = new uppc();
		/* Step 1: Initialize UPPC. */
        mUPPC.init();
		/* Step 2: Start UPPC thread. */
        mUPPC.start();
        new Thread(new UIThread()).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(mUPPC!=null){
            mUPPC.stop();
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // FIXME
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, MapActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPPC_UPDATE_MSG:
                    System.out.println("UI update...");
                    iPosition.setText(msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    class UIThread implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
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
                    myHandler.sendMessage(message);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
