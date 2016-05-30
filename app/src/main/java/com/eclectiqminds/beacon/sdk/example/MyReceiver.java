package com.eclectiqminds.beacon.sdk.example;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.eclectiqminds.beacon.sdk.remote.beacon.Beacon;

/**
 * Created by Zsolt Szabo Negyedi on 5/25/2016.
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String INTENT_BEACON_EXTRA = "intent_beacon_extra";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(INTENT_BEACON_EXTRA)) {
            Beacon beacon = intent.getParcelableExtra(INTENT_BEACON_EXTRA);
            showBeaconNotification(context, beacon);
        }
        else {
            Toast.makeText(context,"Error happened while scanning fro BLE devices. Maybe BT is disabled?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBeaconNotification(Context context, Beacon beacon) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(context);
        final Notification notification = builder.setContentTitle("Detection")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Beacon:" + beacon.getMajor() + ":" + beacon.getMinor())
                .setContentIntent(getIntentForNotification(context)).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    private PendingIntent getIntentForNotification(Context context) {
        return PendingIntent.getActivity(context, 1,
                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),0);
    }
}
