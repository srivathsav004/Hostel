package com.example.hostel;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class CallService extends Service {

    private final IBinder binder = new CallServiceBinder();
    private CallReceiver callReceiver;

    public class CallServiceBinder extends Binder {
        CallService getService() {
            return CallService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void makeCall(String phoneNumber) {
        // Send a broadcast to inform the activity to start the call
        Intent callIntent = new Intent("com.example.hostel.MAKE_CALL");
        callIntent.putExtra("PHONE_NUMBER", phoneNumber);
        sendBroadcast(callIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Register a receiver for the broadcast
        callReceiver = new CallReceiver();
        IntentFilter filter = new IntentFilter("com.example.hostel.MAKE_CALL");
        registerReceiver(callReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the receiver when the service is destroyed
        unregisterReceiver(callReceiver);
    }
    private class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = intent.getStringExtra("PHONE_NUMBER");
            if (phoneNumber != null) {
                // Use the context to start the activity with FLAG_ACTIVITY_NEW_TASK flag
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
            }
        }
    }

}
