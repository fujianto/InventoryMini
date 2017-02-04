package com.septianfujianto.inventorymini.models.realm;

import android.content.Context;
import android.widget.Toast;

import com.septianfujianto.inventorymini.models.ProductFilter;

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

    public RealmResults<Product> getProducts() {
        RealmResults<Product> results = realm.where(Product.class).findAllSorted("date_created", Sort.DESCENDING);

        return results;
    }

    public Product getProductById(int id) {
        Product results = realm.where(Product.class).equalTo("product_id", id).findFirst();

        return results;
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
        //System.out.println("`` CATID "+filter.getCategory_id()+ " "+ filter.getMinPrice() + "``" +filter.getMaxPrice()+ "`` "+filter.getMinQty()+" "+filter.getMaxQty());

        if (filter.getCategory_id() != null) {
            query = query.equalTo("category_id", filter.getCategory_id());
        }

        if (filter.getMinPrice() != null && filter.getMaxPrice() != null) {
            query = query.between("price", filter.getMinPrice(), filter.getMaxPrice());
        }

        if (filter.getMinQty() > 0 && filter.getMaxQty() > 0) {
            query = query.between("product_qty", filter.getMinQty(), filter.getMaxQty());
        }

        return query.findAll();
    }
}