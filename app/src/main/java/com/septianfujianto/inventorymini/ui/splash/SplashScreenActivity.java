package com.septianfujianto.inventorymini.ui.splash;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.ui.product.ListProductActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private MiniRealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_screen);
        helper = new MiniRealmHelper(this);

        final Intent intent = new Intent(this, ListProductActivity.class);
        if (helper.getCategories().size() > 0 && helper.getCategories().size() > 0) {
            finish();
            startActivity(intent);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(intent);
                    setDefaultRealmDB();
                }
            }, 1000);
        }

    }

    private void setDefaultRealmDB() {
        // Default category
        if (helper.getCategories().size() <= 0) {
            helper.insertCategory(0, getString(R.string.other_category));
        }

        // Default Location
        if (helper.getLocations().size() <= 0) {
            helper.insertLocation(0, getString(R.string.other_location));
        }
    }
}
