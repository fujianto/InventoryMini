package com.septianfujianto.inventorymini.process;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.ui.main.MainActivity;
import com.septianfujianto.inventorymini.ui.status.StatusActivity;
import com.septianfujianto.inventorymini.utils.SharedPref;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Septian A. Fujianto on 2/15/2017.
 */

public class StockReminderJob extends Job {
    public static final String TAG = "stock_reminder";
    private int mLastJobId;

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        boolean success = new PushNotif(getContext()).notif();

        if (params.isPeriodic()) {
            Context context = getContext();
            PushNotif pushNotif = new PushNotif(context);
            pushNotif.showPushNotification(
                    context.getString(R.string.pref_title_low_stock_notifications),
                    context.getString(R.string.low_stock_desc),
                    R.mipmap.ic_launcher,
                    new Intent(context, StatusActivity.class));
        }

        return success ? Result.SUCCESS : Result.FAILURE;
    }


    public void schedulePeriodicJob() {
        String sync_frequency = SharedPref.getString("sync_frequency") != null ?
                SharedPref.getString("sync_frequency") : "360";

        mLastJobId = new JobRequest.Builder(StockReminderJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(Long.valueOf(sync_frequency)), JobRequest.MIN_FLEX)
                .setPersisted(true)
                .build()
                .schedule();
    }

    public void cancelJob(int jobId) {
        JobManager.instance().cancel(jobId);
    }
}
