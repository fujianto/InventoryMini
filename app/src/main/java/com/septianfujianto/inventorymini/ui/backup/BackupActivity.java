package com.septianfujianto.inventorymini.ui.backup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Category;
import com.septianfujianto.inventorymini.models.realm.Location;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.models.realm.RealmBackupRestoreJson;
import com.septianfujianto.inventorymini.ui.product.ListProductActivity;
import com.septianfujianto.inventorymini.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static android.R.attr.path;

public class BackupActivity extends AppCompatActivity {
    @BindView(R.id.viewpagerBackupRestore) ViewPager viewPager;
    @BindView(R.id.tabBackupRestore) TabLayout tabLayout;

    private MiniRealmHelper helper;
    private Context mContext;
    private  RealmBackupRestoreJson realmBackupRestoreJson;
    private Activity activity;
    private String backupfilePath, jsonBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_backup);
        ButterKnife.bind(this);
        helper = new MiniRealmHelper(this);
        mContext = this;
        activity = this;
        realmBackupRestoreJson = new RealmBackupRestoreJson(this);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setElevation(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        BackupFragmentPagerAdapter  backupFragmentPagerAdapter = new BackupFragmentPagerAdapter(getSupportFragmentManager());
        backupFragmentPagerAdapter.addFragment(new BackupFragment(), "Backup");
        backupFragmentPagerAdapter.addFragment(new RestoreFragment(), "Restore");

        viewPager.setAdapter(backupFragmentPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, ListProductActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            backupfilePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String filename = backupfilePath.substring(backupfilePath.lastIndexOf("/")+1);
            String tableType = TextUtils.split(filename, "-")[0];

            if (TextUtils.equals(tableType, getString(R.string.app_simple_name))) {
                restoreBackup(backupfilePath);
            } else {
                oldRestoreBackup(backupfilePath);
            }

        }
    }

    private void oldRestoreBackup(String backupfilePath) {
        try {
            if (backupfilePath != null) {
                String filename = backupfilePath.substring(backupfilePath.lastIndexOf("/")+1);
                String filenameArray[] = filename.split("\\.");
                String fileExtension = filenameArray[filenameArray.length-1];
                String tableType = TextUtils.split(filename, "-")[0];

                if (TextUtils.equals(fileExtension, "json") == false) {
                    Toast.makeText(mContext, getString(R.string.msg_only_json_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, getString(R.string.restore_data)+" "+backupfilePath, Toast.LENGTH_SHORT).show();
                    jsonBackup = FileUtils.getStringFromFile(backupfilePath);

                    if (TextUtils.equals(tableType, getString(R.string.translate_false_table_product))) {
                        helper.addItemstoRealmFromJson(jsonBackup, Product.class);
                    }

                    if (TextUtils.equals(tableType, getString(R.string.translate_false_table_category))) {
                        helper.addItemstoRealmFromJson(jsonBackup, Category.class);
                    }

                    if (TextUtils.equals(tableType, getString(R.string.translate_false_table_location))) {
                        helper.addItemstoRealmFromJson(jsonBackup, Location.class);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreBackup(String backupfilePath) {
        try {
            if (backupfilePath != null) {
                String filename = backupfilePath.substring(backupfilePath.lastIndexOf("/")+1);
                String filenameArray[] = filename.split("\\.");
                String fileExtension = filenameArray[filenameArray.length-1];

                if (TextUtils.equals(fileExtension, "json") == false) {
                    Toast.makeText(mContext, getString(R.string.msg_only_json_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, getString(R.string.restore_data)+" "+backupfilePath, Toast.LENGTH_SHORT).show();
                    jsonBackup = FileUtils.getStringFromFile(backupfilePath);

                    JSONObject obj = new JSONObject(jsonBackup);
                    JSONArray productsArr = obj.getJSONArray("products");
                    JSONArray categoriesArr = obj.getJSONArray("categories");
                    JSONArray locationsArr = obj.getJSONArray("locations");

                    helper.addItemstoRealmFromJson(productsArr.toString(), Product.class);
                    helper.addItemstoRealmFromJson(categoriesArr.toString(), Category.class);
                    helper.addItemstoRealmFromJson(locationsArr.toString(), Location.class);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
