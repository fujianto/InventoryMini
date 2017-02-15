package com.septianfujianto.inventorymini;


import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.septianfujianto.inventorymini.models.realm.MiniMigration;
import com.septianfujianto.inventorymini.process.StockReminderJobCreator;
import com.septianfujianto.inventorymini.ui.main.MainActivity;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.zxy.recovery.core.Recovery;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Septian A. Fujianto on 1/29/2017.
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(3) // Must be bumped when the schema changes
                .migration(new MiniMigration()) // MiniMigration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        Iconify.with(new FontAwesomeModule());

        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .recoverEnabled(true)
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(this);

        JobManager.create(this).addJobCreator(new StockReminderJobCreator());
    }

    public static synchronized Context getContext() {
        return context;
    }
}
