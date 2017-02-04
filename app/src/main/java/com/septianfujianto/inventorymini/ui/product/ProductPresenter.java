package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;

import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Septian A. Fujianto on 2/2/2017.
 */

public class ProductPresenter {
    private Context context;
    private ProductPresenterListener listener;
    private MiniRealmHelper helper;

    public interface ProductPresenterListener {
        void productLoaded();
        void productUpdated();
        void productDeleted();
        Product createdProduct();
        void productSearched();
    }

    public ProductPresenter(Context context, ProductPresenterListener listener) {
        this.context = context;
        this.listener = listener;
        helper = new MiniRealmHelper(context);
    }

    public RealmResults<Product> getProducts() {

        return helper.getProducts();
    }

    public void deleteAllProduct() {
        helper.deleteAllItem(Product.class);
        listener.productDeleted();

    }

    public RealmResults<Product> searchProduct(String name) {

        return helper.searchProducts(name);
    }

    public void recyclerviewUpdated() {
        listener.productUpdated();
    }

    public void addNewProduct() {
        helper = new MiniRealmHelper(context);

        if (listener.createdProduct() != null) {
            helper.insertItem(listener.createdProduct());
        }
    }

}
