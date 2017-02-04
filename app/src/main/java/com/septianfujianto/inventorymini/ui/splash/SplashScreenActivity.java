package com.septianfujianto.inventorymini.ui.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.os.Handler;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.ui.product.ListProductActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private MiniRealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        helper = new MiniRealmHelper(this);
        System.out.println("``` "+helper.getCategories()+" `"+helper.getCategories().size());
        // Default category
        if (helper.getCategories() != null || helper.getCategories().size() > 0) {
            helper.insertCategory(0, "Lainnya");
        }

        final Intent intent = new Intent(this, ListProductActivity.class);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
                startActivity(intent);
            }
        }, 1000);
    }
}
