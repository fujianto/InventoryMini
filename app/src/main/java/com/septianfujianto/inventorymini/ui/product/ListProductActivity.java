package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.ProductFilter;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.ui.backup.BackupActivity;
import com.septianfujianto.inventorymini.ui.category.CreateCategoryActivity;
import com.septianfujianto.inventorymini.ui.location.CreateLocationActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

import static com.septianfujianto.inventorymini.R.id.fab;

public class ListProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProductPresenter.ProductPresenterListener {
    private Context context;
    private MiniRealmHelper helper;
    private SearchBox search;
    private ProductAdapter adapter;
    private List<Product> products;
    private RecyclerView rcvProduct;
    private ProductPresenter productPresenter;
    private Toolbar toolbar;
    private BottomSheetDialogFragment sortDialogFragment;
    private BottomSheetDialogFragment filterDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        search = (SearchBox) findViewById(R.id.searchbox);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.bar_title_latest_product));
        setSupportActionBar(toolbar);

        context = this;
        helper = new MiniRealmHelper(this);
        productPresenter = new ProductPresenter(this, this);
        sortDialogFragment = new SortDialogFragment();
        filterDialogFragment = new filterDialogFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(view.getContext(), CreateProductActivity.class));
            }
        });

        fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_plus).colorRes(R.color.icons));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupSearchbox();
        setupProductRecyclerview(savedInstanceState);

        RealmResults<Product> res = helper.getProducts();
    }

    private void setupSearchbox() {
        search.enableVoiceRecognition(this);
        search.setMenuVisibility(View.INVISIBLE);
        search.setDrawerLogo(new IconDrawable(context, FontAwesomeIcons.fa_search).colorRes(R.color.secondary_text));
        search.setAnimateDrawerLogo(false);
        search.setSearchWithoutSuggestions(true);
        search.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String s) {
                RealmResults<Product> results  = productPresenter.searchProduct(s);
                productPresenter.recyclerviewUpdated();
                products.addAll(results);
                toolbar.setTitle(getString(R.string.action_search)+" "+s);
            }

            @Override
            public void onSearch(String s) {
                //Toast.makeText(context, "Mencari "+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultClick(SearchResult searchResult) {

            }
        });
    }

    private void setupProductRecyclerview(Bundle savedInstanceState) {
        products = new ArrayList<>();
        adapter = new ProductAdapter(this, products);
        rcvProduct = (RecyclerView) findViewById(R.id.rcv_listProduct);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(context));
        rcvProduct.setAdapter(adapter);

        final RealmResults<Product> results = productPresenter.getProducts();
        products.addAll(results);
    }

    private void deleteAllProduct() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(getResources().getString(R.string.question_delete_all_product));
        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productPresenter.deleteAllProduct();
            }
        });

        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        getMenuInflater().inflate(R.menu.list_product, menu);

        menu.findItem(R.id.action_filter).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_filter)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.action_sort).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_sort_amount_desc)
                        .colorRes(R.color.icons)
                        .actionBarSize());

        menu.findItem(R.id.action_delete_all_product).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_trash)
                        .colorRes(R.color.icons)
                        .actionBarSize());

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
        } else if (id == R.id.action_delete_all_product) {
            deleteAllProduct();

            return true;
        } else if (id == R.id.action_filter) {
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
