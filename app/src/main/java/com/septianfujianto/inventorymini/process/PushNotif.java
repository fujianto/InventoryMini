package com.septianfujianto.inventorymini.process;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AlertDialog;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.ui.status.StatusActivity;
import com.septianfujianto.inventorymini.utils.SharedPref;

import java.util.concurrent.atomic.AtomicInteger;

import static android.R.id.message;

/**
 * Created by Septian A. Fujianto on 2/13/2017.
 */

public class PushNotif {
    private MiniRealmHelper helper;
    private Context context;

    public PushNotif(Context context) {
        this.context = context;
        helper = new MiniRealmHelper(context);
    }

    @WorkerThread
    public boolean notif() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new NetworkOnMainThreadException();
        }

        SystemClock.sleep(1_000);
        boolean success = Math.random() > 0.1; // successful 90% of the time

        return success;
    }

    public void showPushNotification(String title, String description, int notifIcon, Intent targetIntent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        AtomicInteger c = new AtomicInteger(0);
        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(notifIcon)
                .setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT < 16) {
            notification.setAutoCancel(true);
            notification.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(c.incrementAndGet(), notification.getNotification());

        } else {
            notification.setAutoCancel(true);
            notificationManager.notify(c.incrementAndGet(),
                    notification.setStyle(new Notification.BigTextStyle().bigText(description)).build());
        }
    }

    public void showStockAlertNotif() {
        Boolean isStockNotifOn = SharedPref.getBol("notifications_low_stock");

        if (isStockNotifOn == true && helper.getLowStocksProducts().size() > 0) {
            if (SharedPref.getLong("last_stock_alert_time") == 0) {
                SharedPref.saveLong("last_stock_alert_time", System.currentTimeMillis());
            }

            stockAlertDialog();
        }
    }

    private void stockAlertDialog() {
        long current_time = System.currentTimeMillis();
        String sync_frequency = SharedPref.getString("sync_frequency") != null ?
                SharedPref.getString("sync_frequency") : "360";
        long last_sync = SharedPref.getLong("last_stock_alert_time") > 0 ?
                SharedPref.getLong("last_stock_alert_time") : current_time;
        Long nextSync = last_sync + (Long.valueOf(sync_frequency) * 60 * 1000);

        AlertDialog.Builder lowstockAlert = new AlertDialog.Builder(context);
        lowstockAlert.setTitle(context.getString(R.string.pref_title_low_stock_notifications));
        lowstockAlert.setMessage(context.getString(R.string.low_stock_desc));
        lowstockAlert.setPositiveButton(context.getString(R.string.check_low_stock), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(context, StatusActivity.class));
            }
        });

        lowstockAlert.setNeutralButton(context.getString(R.string.cancels), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        if (current_time > nextSync) {
            lowstockAlert.show();
            SharedPref.saveLong("last_stock_alert_time", current_time);
        }
    }
}
