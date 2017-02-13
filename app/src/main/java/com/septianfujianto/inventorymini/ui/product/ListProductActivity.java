package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.ProductFilter;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.ui.backup.BackupActivity;
import com.septianfujianto.inventorymini.ui.category.CreateCategoryActivity;
import com.septianfujianto.inventorymini.ui.location.CreateLocationActivity;
import com.septianfujianto.inventorymini.ui.settings.SettingsActivity;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProductPresenter.ProductPresenterListener {
    private Context context;
    private MiniRealmHelper helper;
    private ProductAdapter adapter;
    private List<Product> products;
    private RecyclerView rcvProduct;
    private ProductPresenter productPresenter;
    private Toolbar toolbar;
    private BottomSheetDialogFragment sortDialogFragment;
    private BottomSheetDialogFragment filterDialogFragment;
    private View dialogView;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.searcbox) EditText searchbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.bar_title_latest_product));

        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        context = this;
        helper = new MiniRealmHelper(this);
        productPresenter = new ProductPresenter(this, this);
        sortDialogFragment = new SortDialogFragment();
        filterDialogFragment = new filterDialogFragment();

        setupDrawerNavigation();
        setupSearchbox();
        setupProductRecyclerview(savedInstanceState);
        setupBottombar();
    }

    private void setupDrawerNavigation() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        menu.findItem(R.id.nav_category).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_tag)
            .colorRes(R.color.icons).actionBarSize());

        menu.findItem(R.id.nav_location).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_location_arrow)
                .colorRes(R.color.icons).actionBarSize());

        menu.findItem(R.id.nav_import).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_download)
                .colorRes(R.color.icons).actionBarSize());

        menu.findItem(R.id.nav_about).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_info)
                .colorRes(R.color.icons).actionBarSize());

        menu.findItem(R.id.nav_settings).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_cog)
                .colorRes(R.color.icons).actionBarSize());
    }

    private void setupBottombar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_new_product:
                        startActivity(new Intent(context, CreateProductActivity.class));
                        return true;

                    case R.id.action_sort:
                        sortDialogFragment.show(getSupportFragmentManager(), sortDialogFragment.getTag());
                        return true;

                    case R.id.action_filter:
                        filterDialogFragment.show(getSupportFragmentManager(), filterDialogFragment.getTag());
                        return true;
                }

                return false;
            }
        });

        bottomNavigationView.getMenu().findItem(R.id.action_new_product).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_plus_circle)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        bottomNavigationView.getMenu().findItem(R.id.action_filter).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_filter)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        bottomNavigationView.getMenu().findItem(R.id.action_sort).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_sort_amount_desc)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        bottomNavigationView.setMinimumHeight(170);
    }

    private void setupSearchbox() {
        Drawable icon = new IconDrawable(this, FontAwesomeIcons.fa_search).colorRes(R.color.secondary_text).sizeDp(15);
        searchbox.setCompoundDrawables(icon, null, null, null);
        searchbox.setCompoundDrawablePadding(10);
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RealmResults<Product> results  = productPresenter.searchProduct(charSequence.toString());
                productPresenter.recyclerviewUpdated();
                products.addAll(results);
                toolbar.setTitle(getString(R.string.action_search)+" "+charSequence.toString());

                if (charSequence.toString().length() < 1) {
                    toolbar.setTitle(getString(R.string.bar_title_latest_product));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupProductRecyclerview(Bundle savedInstanceState) {
        products = new ArrayList<>();
        adapter = new ProductAdapter(this, products);
        rcvProduct = (RecyclerView) findViewById(R.id.rcv_listProduct);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new GridLayoutManager(context, 2));
        rcvProduct.setAdapter(adapter);

        final RealmResults<Product> results = productPresenter.getProducts();
        products.addAll(results);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.list_product, menu);

        menu.findItem(R.id.action_filter).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_filter)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.action_sort).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_sort_amount_desc)
                        .colorRes(R.color.icons)
                        .actionBarSize());*/

        return true;
    }

    public void sortProduct(String fieldName, Sort sort) {
        //Toast.makeText(context, "SORT BY: "+fieldName+" "+sort, Toast.LENGTH_SHORT).show();
        RealmResults<Product> results  = helper.sortProduct(fieldName, sort);
        productPresenter.recyclerviewUpdated();
        products.addAll(results);
        sortDialogFragment.dismiss();
    }

    public void filterProduct(ProductFilter filter) {
        RealmResults<Product> results  = helper.filterProducts(filter);
        productPresenter.recyclerviewUpdated();
        products.addAll(results);
        filterDialogFragment.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            sortDialogFragment.show(getSupportFragmentManager(),
                    sortDialogFragment.getTag());

            return true;

        }  else if (id == R.id.action_filter) {
            filterDialogFragment.show(getSupportFragmentManager(), filterDialogFragment.getTag());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_category) {
            startActivity(new Intent(App.getContext(), CreateCategoryActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_import) {
            startActivity(new Intent(App.getContext(), BackupActivity.class));
        } else if (id == R.id.nav_location) {
            startActivity(new Intent(App.getContext(), CreateLocationActivity.class));
        } else if (id == R.id.nav_about) {
            setupAboutDialog();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(App.getContext(), SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupAboutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(getString(R.string.about_us));
        try {
            LayoutInflater inflater = this.getLayoutInflater();
            dialogView = inflater.inflate(R.layout.layout_dialog_about, null);
            TextView dialogContent = (TextView) dialogView.findViewById(R.id.dialogContent);
            dialogContent.setMovementMethod(LinkMovementMethod.getInstance());
            dialogContent.setText(Utils.printhtmlText(getString(R.string.translate_false_about_html)));
            alert.setView(dialogView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        alert.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        alert.show();
    }

    @Override
    public void productLoaded() {}

    @Override
    public void productUpdated() {
        products.clear();
        rcvProduct.removeAllViews();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void productSearched() {}

    @Override
    public void productDeleted() {
        products.clear();
        rcvProduct.removeAllViews();
        adapter.notifyDataSetChanged();
    }


    @Override
    public Product createdProduct() {
        return null;
    }
}
