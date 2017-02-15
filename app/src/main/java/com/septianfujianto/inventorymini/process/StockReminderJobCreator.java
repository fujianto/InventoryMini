package com.septianfujianto.inventorymini.process;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

/**
 * Created by Septian A. Fujianto on 2/15/2017.
 */

public class StockReminderJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case StockReminderJob.TAG:
                return new StockReminderJob();
            default:
                return null;
        }
    }

    public static final class AddReceiver extends AddJobCreatorReceiver {
        @Override
        protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
            // manager.addJobCreator(new DemoJobCreator());
        }
    }
}
