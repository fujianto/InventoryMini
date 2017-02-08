package com.septianfujianto.inventorymini.models.realm;

import android.content.Context;
import android.widget.Toast;

import com.septianfujianto.inventorymini.models.ProductFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.R.attr.category;

/**
 * Created by Septian A. Fujianto on 1/30/2017.
 */

public class MiniRealmHelper {
    private Realm realm;
    public Context mContext;

    public MiniRealmHelper(Context mContext) {
        realm = Realm.getDefaultInstance();
        this.mContext = mContext;
    }

    public RealmResults<RealmModel> getAllRealmItems(Class obj) {
        RealmResults results = realm.where(obj).findAll();

        return results;
    }

    public void insertItem(RealmModel model) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
    }

    public void deleteAllItem(Class obj) {
        RealmResults<RealmObject> dataResults = realm.where(obj).findAll();
        realm.beginTransaction();
        dataResults.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public int getNextKey(Class obj, String dbId) {
        try {
            if (realm.where(obj).findAll().size() > 0) {
                return realm.where(obj).max(dbId).intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public RealmResults<Category> getCategories() {
        RealmResults<Category> results = realm.where(Category.class).findAll();
        results.sort("category_id", Sort.ASCENDING);
        return results;
    }

    public void insertCategory(int id, String name) {
        Category category = new Category(id, name);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(category);
        realm.commitTransaction();
    }

    public void deleteCategoryById(int id) {
        RealmResults<Category> dataResults = realm.where(Category.class).equalTo("category_id", id).findAll();
        realm.beginTransaction();
        dataResults.deleteFirstFromRealm();
        realm.commitTransaction();
    }

    public Category getCategoryById(int id) {
        Category results = realm.where(Category.class).equalTo("category_id", id).findFirst();

        return results;
    }

    public RealmResults<Product> getProducts() {
        RealmResults<Product> results = realm.where(Product.class).findAllSorted("date_created", Sort.DESCENDING);

        return results;
    }

    public Product getProductById(int id) {
        Product results = realm.where(Product.class).equalTo("product_id", id).findFirst();

        return results;
    }

    public void deleteProductById(int id) {
        RealmResults<Product> dataResults = realm.where(Product.class).equalTo("product_id", id).findAll();
        realm.beginTransaction();
        dataResults.deleteFirstFromRealm();
        realm.commitTransaction();
    }

    public RealmResults<Product> searchProducts(String productName) {
        RealmResults<Product> results = realm.where(Product.class).contains("product_name", productName, Case.INSENSITIVE).findAll();
        results.sort("product_id", Sort.ASCENDING);
        return results;
    }

    public RealmResults<Product> sortProduct(String fieldName, Sort sort) {
        if (fieldName != null && sort != null) {
            RealmResults<Product> results = realm.where(Product.class).findAllSorted(fieldName, sort);

            return results;
        }

        return this.getProducts();
    }

    public RealmResults<Product> filterProducts(ProductFilter filter) {
        RealmQuery<Product> query = realm.where(Product.class);

        if (filter.getCategory_id() != null) {
            query = query.equalTo("category_id", filter.getCategory_id());
        }
        System.out.println("` filter.getLocation_id() "+filter.getLocation_id() );
        if (filter.getLocation_id() > 0) {
            query = query.equalTo("location_id", filter.getLocation_id());
        }

        if (filter.getMinPrice() != null && filter.getMaxPrice() != null) {
            query = query.between("price", filter.getMinPrice(), filter.getMaxPrice());
        }

        if (filter.getMinQty() > 0 && filter.getMaxQty() > 0) {
            query = query.between("product_qty", filter.getMinQty(), filter.getMaxQty());
        }

        return query.findAll();
    }

    public RealmResults<Location> getLocations() {
        RealmResults<Location> results = realm.where(Location.class).findAll();
        results.sort("location_id", Sort.ASCENDING);
        return results;
    }

    public void insertLocation(int id, String name) {
        Location location = new Location(id, name);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(location);
        realm.commitTransaction();
    }

    public void deleteLocationById(int id) {
        RealmResults<Location> dataResults = realm.where(Location.class).equalTo("location_id", id).findAll();
        realm.beginTransaction();
        dataResults.deleteFirstFromRealm();
        realm.commitTransaction();
    }

    public Location getLocationById(int id) {
        Location results = realm.where(Location.class).equalTo("location_id", id).findFirst();

        return results;
    }

    public void addItemstoRealmFromJson(String jsonResult, Class modelClass) {
        try {
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(modelClass, jsonResult);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            e.printStackTrace();
        }
    }
}
