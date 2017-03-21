package com.septianfujianto.inventorymini.ui.product;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.android.job.JobManager;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.ProductFilter;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.process.PushNotif;
import com.septianfujianto.inventorymini.process.StockReminderJob;
import com.septianfujianto.inventorymini.ui.backup.BackupActivity;
import com.septianfujianto.inventorymini.ui.category.CreateCategoryActivity;
import com.septianfujianto.inventorymini.ui.location.CreateLocationActivity;
import com.septianfujianto.inventorymini.ui.settings.SettingsActivity;
import com.septianfujianto.inventorymini.ui.status.StatusActivity;
import com.septianfujianto.inventorymini.utils.SharedPref;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.webkit.WebSettings.PluginState.ON;

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
    private JobManager mJobManager;
    private TextView drawerMenuTitle;

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

        mJobManager = JobManager.instance();

        context = this;
        helper = new MiniRealmHelper(this);
        productPresenter = new ProductPresenter(this, this);
        sortDialogFragment = new SortDialogFragment();
        filterDialogFragment = new filterDialogFragment();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setupDrawerNavigation();
        setupSearchbox();
        setupProductRecyclerview(savedInstanceState);
        setupBottombar();

       /* System.out.println("================");
        System.out.println(SharedPref.getBol("notifications_low_stock"));
        System.out.println(helper.getLowStocksProducts().size());
        System.out.println(SharedPref.getBol("notifications_low_stock") && helper.getLowStocksProducts().size() > 0);*/

        if (savedInstanceState == null) {
            PushNotif notif = new PushNotif(context);
            notif.showStockAlertNotif();
            initPushNotifStockReminder();
        }

    }

    private void initPushNotifStockReminder() {
        Boolean isStockNotifOn = SharedPref.getBol("notifications_low_stock");

        if (isStockNotifOn == true && helper.getLowStocksProducts().size() > 0) {
            StockReminderJob stockReminderJob = new StockReminderJob();
            stockReminderJob.schedulePeriodicJob();
        } else if (helper.getLowStocksProducts().size() <= 0) {
            mJobManager.cancelAll();
        }
    }

    private void setupDrawerNavigation() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        String titleDrawer = SharedPref.getString("store_name") != null || SharedPref.getString("store_name") != "" ?
                SharedPref.getString("store_name") : getString(R.string.pref_default_store_name);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        Menu menu = navigationView.getMenu();
        drawerMenuTitle = (TextView) header.findViewById(R.id.drawerMenuTitle);
        drawerMenuTitle.setText(titleDrawer);

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

        menu.findItem(R.id.nav_summary).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_line_chart)
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
        try {
            //Toast.makeText(context, "SORT BY: "+fieldName+" "+sort, Toast.LENGTH_SHORT).show();
            RealmResults<Product> results  = helper.sortProduct(fieldName, sort);

            if (results != null) {
                productPresenter.recyclerviewUpdated();
                products.addAll(results);
                sortDialogFragment.dismiss();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage() != null ? e.getMessage() : "Something wrong with sort", Toast.LENGTH_SHORT).show();
        }
    }

    public void filterProduct(ProductFilter filter) {
        try {
            RealmResults<Product> results = helper.filterProducts(filter);

            if (results != null) {
                productPresenter.recyclerviewUpdated();
                products.addAll(results);
                filterDialogFragment.dismiss();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage() != null ? e.getMessage() : "Something wrong with filter", Toast.LENGTH_SHORT).show();
        }
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
        } else if (id == R.id.nav_summary) {
            startActivity(new Intent(App.getContext(), StatusActivity.class));
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
