package com.septianfujianto.inventorymini.models.realm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.Config;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.CategoryPrimitive;
import com.septianfujianto.inventorymini.models.LocationPrimitive;
import com.septianfujianto.inventorymini.models.ProductPrimitive;
import com.septianfujianto.inventorymini.ui.backup.BackupActivity;
import com.septianfujianto.inventorymini.utils.FileUtils;
import com.septianfujianto.inventorymini.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.data;

/**
 * Created by Septian A. Fujianto on 2/5/2017.
 */

public class RealmBackupRestoreJson {
    private File STORAGE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_JSON_PATH = STORAGE_PATH.toString()+"/"+Config.getDefaultDirectory();
    private final static String TAG = RealmBackupRestoreJson.class.getSimpleName();
    private Activity activity;
    private MiniRealmHelper helper;

    public RealmBackupRestoreJson(Activity activity) {
        this.activity = activity;
        helper = new MiniRealmHelper(activity);
    }


    public void backupProduct() {
        RealmResults<Product> productResults = helper.getProducts();
        List<ProductPrimitive> productList = new ArrayList<>();
        checkStoragePermissions(activity, new FileUtils());

        try {
            for (Product product : productResults) {
                ProductPrimitive proPrim = new ProductPrimitive();
                proPrim.setProduct_id(product.getProduct_id());
                proPrim.setProduct_name(product.getProduct_name());
                proPrim.setProduct_desc(product.getProduct_desc());
                proPrim.setProduct_brand(product.getProduct_brand());
                proPrim.setProduct_image(product.getProduct_image());
                proPrim.setProduct_qty(product.getProduct_qty());
                proPrim.setCategory_id(product.getCategory_id());
                proPrim.setLocation_id(product.getLocation_id());
                proPrim.setProduct_qty_label(product.getProduct_qty_label() == null ? "pcs" : product.getProduct_qty_label());
                proPrim.setProduct_weight(product.getProduct_weight());
                proPrim.setProduct_weight_label(product.getProduct_weight_label() == null ? "gr" : product.getProduct_weight_label());
                proPrim.setProduct_qty_watch(product.getProduct_qty_watch());
                proPrim.setSale_price(product.getSale_price());
                proPrim.setPrice(product.getPrice());
                proPrim.setBulk_price(product.getBulk_price());
                proPrim.setDate_created(product.getDate_created());
                proPrim.setDate_modified(product.getDate_modified());

                productList.add(proPrim);
            }

            String jsonBackup = new Gson().toJson(productList);
            writeFile("Product", jsonBackup, EXPORT_JSON_PATH);
        } catch (Exception e) {
            Toast.makeText(App.getContext(), e.getMessage() != null ?
                    e.getMessage() : "Something wrong with Backup process", Toast.LENGTH_SHORT).show();
        }
    }

    public void backupCategory() {
        RealmResults<Category> categoryResults = helper.getCategories();
        List<CategoryPrimitive> categoryList = new ArrayList<>();
        checkStoragePermissions(activity, new FileUtils());

        try {
            for (Category category : categoryResults) {
                CategoryPrimitive catPrim = new CategoryPrimitive();
                catPrim.setCategory_id(category.getCategory_id());
                catPrim.setCategory_name(category.getCategory_name());

                categoryList.add(catPrim);
            }

            String jsonBackup = new Gson().toJson(categoryList);
            writeFile("Category", jsonBackup, EXPORT_JSON_PATH);
        } catch (Exception e) {
            Toast.makeText(App.getContext(), e.getMessage() != null ?
                    e.getMessage() : "Something wrong on Backup", Toast.LENGTH_SHORT).show();
        }
    }

    public void backupLocation() {
        RealmResults<Location> locationResults = helper.getLocations();
        List<LocationPrimitive> locationList = new ArrayList<>();
        checkStoragePermissions(activity, new FileUtils());

        try {
            for (Location location : locationResults) {
                LocationPrimitive catPrim = new LocationPrimitive();
                catPrim.setLocation_id(location.getLocation_id());
                catPrim.setLocation_name(location.getLocation_name());

                locationList.add(catPrim);
            }

            String jsonBackup = new Gson().toJson(locationList);
            writeFile("Location", jsonBackup, EXPORT_JSON_PATH);
        } catch (Exception e) {
            Toast.makeText(App.getContext(), e.getMessage() != null ?
                    e.getMessage() : "Something wrong on Backup", Toast.LENGTH_SHORT).show();
        }
    }

    public void backupAll() {
        RealmResults<Product> productResults = helper.getProducts();
        List<ProductPrimitive> productList = new ArrayList<>();
        RealmResults<Category> categoryResults = helper.getCategories();
        List<CategoryPrimitive> categoryList = new ArrayList<>();
        RealmResults<Location> locationResults = helper.getLocations();
        List<LocationPrimitive> locationList = new ArrayList<>();

        checkStoragePermissions(activity, new FileUtils());


        try {
            for (Product product : productResults) {
                ProductPrimitive proPrim = new ProductPrimitive();
                proPrim.setProduct_id(product.getProduct_id());
                proPrim.setProduct_name(product.getProduct_name());
                proPrim.setProduct_desc(product.getProduct_desc());
                proPrim.setProduct_brand(product.getProduct_brand());
                proPrim.setProduct_image(product.getProduct_image());
                proPrim.setProduct_qty(product.getProduct_qty());
                proPrim.setCategory_id(product.getCategory_id());
                proPrim.setLocation_id(product.getLocation_id());
                proPrim.setProduct_qty_label(product.getProduct_qty_label() == null ? "pcs" : product.getProduct_qty_label());
                proPrim.setProduct_weight(product.getProduct_weight());
                proPrim.setProduct_weight_label(product.getProduct_weight_label() == null ? "gr" : product.getProduct_weight_label());
                proPrim.setProduct_qty_watch(product.getProduct_qty_watch());
                proPrim.setSale_price(product.getSale_price());
                proPrim.setPrice(product.getPrice());
                proPrim.setBulk_price(product.getBulk_price());
                proPrim.setDate_created(product.getDate_created());
                proPrim.setDate_modified(product.getDate_modified());

                productList.add(proPrim);
            }

            for (Location location : locationResults) {
                LocationPrimitive catPrim = new LocationPrimitive();
                catPrim.setLocation_id(location.getLocation_id());
                catPrim.setLocation_name(location.getLocation_name());

                locationList.add(catPrim);
            }

            for (Category category : categoryResults) {
                CategoryPrimitive catPrim = new CategoryPrimitive();
                catPrim.setCategory_id(category.getCategory_id());
                catPrim.setCategory_name(category.getCategory_name());

                categoryList.add(catPrim);
            }

            String jsonCategoryBackup = new Gson().toJson(categoryList);
            String jsonLocationBackup = new Gson().toJson(locationList);
            String jsonProductBackup = new Gson().toJson(productList);
            String jsonBackup = "{'': "+jsonProductBackup+", 'categories': "+jsonCategoryBackup+", 'locations': "+jsonLocationBackup+" }";

            writeFile(this.activity.getString(R.string.app_simple_name), jsonBackup, EXPORT_JSON_PATH);
        } catch (Exception e) {
            Toast.makeText(App.getContext(), e.getMessage() != null ?
                    e.getMessage() : "Something wrong with Backup process", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFile(String tableName, String data, String path) {
        try {
            Boolean isDirExists = FileUtils.isFileExist(path);
            String fileName = path+"/"+tableName+"-backup-"+Utils.getTodayDate("")+".json";

            if (isDirExists == false)
                createDirectory(path);

            // Writing to a file
            File file = new File(fileName);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDirectory(String path) {
        File backupDir = new File(path);
        backupDir.mkdir();
    }

    private void checkStoragePermissions(Activity activity, FileUtils fileUtils) {
        fileUtils.checkStoragePermissions(activity);
    }
}
