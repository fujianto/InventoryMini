package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.SharedPref;
import com.septianfujianto.inventorymini.utils.SquaredImageView;
import com.septianfujianto.inventorymini.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static android.R.id.input;

public class DetailProductActivity extends AppCompatActivity {
    @BindView(R.id.productTextBrand)
    TextView productTextBrand;
    @BindView(R.id.productTextWeight)
    TextView productTextWeight;
    @BindView(R.id.productTextDateCreated)
    TextView productTextDateCreated;
    @BindView(R.id.productTextDateModified)
    TextView productTextDateModified;
    private int productId;
    private MiniRealmHelper helper;
    private TextView productName, productDetail;
    private ImageView featuredImage;
    private Context context;
    private Bundle extras;

    @BindView(R.id.fabDelete)
    FloatingActionButton fabDelete;
    @BindView(R.id.fabEdit)
    FloatingActionButton fabEdit;
    @BindView(R.id.productTextQty)
    TextView productTextQty;
    @BindView(R.id.productTextPrice)
    TextView productTextPrice;
    @BindView(R.id.productTextPriceBulk)
    TextView productTextPriceBulk;
    @BindView(R.id.productTextCategory)
    TextView productTextCategory;
    @BindView(R.id.productTextLocation)
    TextView productTextLocation;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        MobileAds.initialize(this, getString(R.string.view_product_ad_unit_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setContentView(R.layout.activity_detail_product);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarDetailProduct));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        context = this;

        productName = (TextView) findViewById(R.id.productName);
        featuredImage = (ImageView) findViewById(R.id.featuredImage);
        productDetail = (TextView) findViewById(R.id.productDescription);

        helper = new MiniRealmHelper(this);
        loadSingleProduct();

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(getResources().getString(R.string.question_delete_single_product));
                alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.deleteProductById(productId);
                        finish();
                        startActivity(new Intent(context, ListProductActivity.class));
                    }
                });

                alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alert.show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditProduct();
            }
        });
    }

    private void startEditProduct() {
        Intent intent = new Intent(context, CreateProductActivity.class);
        intent.putExtra(getString(R.string.translate_false_productId), productId);
        finish();
        startActivity(intent);
    }

    private void imagePopup(String imageName, Uri uriImage) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailProductActivity.this);
        dialog.setTitle(imageName);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog_imageview, null);
        final SquaredImageView image = (SquaredImageView) dialogView.findViewById(R.id.imageView);
        image.setImageURI(uriImage);
        dialog.setView(dialogView);

        dialog.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        dialog.show();
    }

    private void loadSingleProduct() {
        extras = getIntent().getExtras();

        if (extras != null) {
            productId = extras.getInt(getString(R.string.translate_false_productId));

            try {
                final Product product = helper.getProductById(productId);

                if (product.getProduct_image() != null) {
                    final Uri uriImage = Uri.parse(product.getProduct_image());
                    featuredImage.setImageURI(uriImage);

                    featuredImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagePopup(product.getProduct_name(), uriImage);
                        }
                    });
                }

                String currency_symbol = SharedPref.getString("currency_symbol") != null ?
                        SharedPref.getString("currency_symbol") : App.getContext().getString(R.string.translate_false_currency_symbol);
                String grouping_separator = SharedPref.getString("grouping_separator") != null ?
                        SharedPref.getString("grouping_separator") : App.getContext().getString(R.string.translate_false_currency_grouping_sep);
                String decimal_separator = SharedPref.getString("decimal_separator") != null ?
                        SharedPref.getString("decimal_separator") : App.getContext().getString(R.string.translate_false_currency_decimal_sep);

                if (product.getPrice() != null) {
                    String formatPrice = Utils.formatCurrency(
                            product.getPrice(), currency_symbol + " ",
                            grouping_separator.charAt(0),
                            decimal_separator.charAt(0));
                    productTextPrice.setText(formatPrice);
                }

                if (product.getBulk_price() != null) {
                    String formatPrice = Utils.formatCurrency(
                            product.getBulk_price(), currency_symbol + " ",
                            grouping_separator.charAt(0),
                            decimal_separator.charAt(0));
                    productTextPriceBulk.setText(formatPrice);
                }

                productName.setText(product.getProduct_name());
                productTextQty.setText(String.valueOf(product.getProduct_qty()) + " " + product.getProduct_qty_label());

                if (product.getCategory_id() != null) {
                    String categoryLabel = helper.getCategoryById(Integer.valueOf(product.getCategory_id())).getCategory_name();
                    productTextCategory.setText(categoryLabel);
                }

                if (String.valueOf(product.getLocation_id()) != null) {
                    String locationLabel = helper.getLocationById(product.getLocation_id()).getLocation_name();
                    productTextLocation.setText(locationLabel);
                }

                productDetail.setText(product.getProduct_desc());
                productTextBrand.setText(product.getProduct_brand());
                productTextWeight.setText(String.valueOf(product.getProduct_weight())+" "+product.getProduct_weight_label());
                productTextDateCreated.setText(product.getDate_created());
                productTextDateModified.setText(product.getDate_modified());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
