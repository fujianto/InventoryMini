package com.septianfujianto.inventorymini.ui.status;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.OverviewItem;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.models.RecyclerviewItem;
import com.septianfujianto.inventorymini.ui.product.ProductAdapter;
import com.septianfujianto.inventorymini.utils.SharedPref;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class StatusActivity extends AppCompatActivity {
    private Context context;
    private MiniRealmHelper helper;
    private ProductAdapter adapter;
    private List<Product> products;
    private RecyclerView rcvProduct;

    private RecyclerView rcvOverview;
    private List<RecyclerviewItem> overview;
    private OverviewAdapter overviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        context = this;
        helper = new MiniRealmHelper(this);

        setupOverviewRecyclerview();
        setupProductRecyclerview();
    }

    private void setupOverviewRecyclerview() {
        overview = new ArrayList<>();
        overviewAdapter = new OverviewAdapter(this, overview);
        rcvOverview = (RecyclerView) findViewById(R.id.rcv_overview);
        rcvOverview.setHasFixedSize(true);
        rcvOverview.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false));
        rcvOverview.setAdapter(overviewAdapter);

        List<String> brandList = helper.getProductColumnList("product_brand");
        String mostBrand = Utils.getMostFrequentItem(brandList);

        List<String> categotyList = helper.getProductColumnList("category_id");
        String mostCategory = Utils.getMostFrequentItem(categotyList);

        List<String> locationList = helper.getProductColumnList("location_id");
        String mostLocation = Utils.getMostFrequentItem(locationList);

        String currency_symbol = SharedPref.getString("currency_symbol") != null ?
                SharedPref.getString("currency_symbol") : App.getContext().getString(R.string.translate_false_currency_symbol);
        String grouping_separator = SharedPref.getString("grouping_separator") != null ?
                SharedPref.getString("grouping_separator") : App.getContext().getString(R.string.translate_false_currency_grouping_sep);
        String decimal_separator = SharedPref.getString("decimal_separator") != null ?
                SharedPref.getString("decimal_separator") : App.getContext().getString(R.string.translate_false_currency_decimal_sep);

        List<Integer> qtyList = new ArrayList<>();
        List<Double> priceList = new ArrayList<>();

        for (Product item : helper.getProducts()) {
            qtyList.add(item.getProduct_qty());
            priceList.add(item.getPrice());
        }

        Double dbPrice = helper.getTotalAmount(qtyList, priceList);
        String totalPrice = dbPrice != null ? Utils.formatCurrency(
                dbPrice, currency_symbol+" ",
                grouping_separator.charAt(0),
                decimal_separator.charAt(0)) : "";

        Long totalQty = (Long) helper.getSumColumn(Product.class, "product_qty");
        int productSize = helper.getProducts().size();

        overview.add(new OverviewItem(getString(R.string.product_variation), String.valueOf(productSize), ""));
        overview.add(new OverviewItem(getString(R.string.total_quantity), String.valueOf(totalQty), ""));
        overview.add(new OverviewItem(getString(R.string.total_amount), totalPrice, ""));
        overview.add(new OverviewItem(getString(R.string.most_used_brand), mostBrand, ""));
        overview.add(new OverviewItem(getString(R.string.most_used_category), mostCategory, ""));
        overview.add(new OverviewItem(getString(R.string.most_used_location), mostLocation, ""));
    }

    private void setupProductRecyclerview() {
        products = new ArrayList<>();
        adapter = new ProductAdapter(this, products);
        rcvProduct = (RecyclerView) findViewById(R.id.rcv_lowstock_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new GridLayoutManager(context, 2));
        rcvProduct.setAdapter(adapter);

        final List<Product> results = helper.getLowStocksProducts();
        products.addAll(results);
    }
}
