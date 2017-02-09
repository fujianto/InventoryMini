package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailProductActivity extends AppCompatActivity {
    private int productId;
    private MiniRealmHelper helper;
    private TextView productName, productDetail;
    private ImageView featuredImage;
    private Context context;
    private Bundle extras;
    @BindView(R.id.fabDelete) FloatingActionButton fabDelete;
    @BindView(R.id.fabEdit)  FloatingActionButton fabEdit;
    @BindView(R.id.productTextQty) TextView productTextQty;
    @BindView(R.id.productTextPrice)  TextView productTextPrice;
    @BindView(R.id.productTextPriceBulk)  TextView productTextPriceBulk;
    @BindView(R.id.productTextCategory)  TextView productTextCategory;
    @BindView(R.id.productTextLocation) TextView productTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    public void onClick(DialogInterface dialogInterface, int i) { }
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

    private void loadSingleProduct() {
        extras = getIntent().getExtras();

        if (extras != null) {
            productId = extras.getInt(getString(R.string.translate_false_productId));

            try {
                Product product = helper.getProductById(productId);

                if (product.getProduct_image() != null) {
                    Uri uriImage = Uri.parse(product.getProduct_image());
                    featuredImage.setImageURI(uriImage);
                }

                if (product.getPrice() != null) {
                    String formatPrice = Utils.formatCurrency(
                            product.getPrice(), getString(R.string.translate_false_currency_symbol)+" ",
                            getString(R.string.translate_false_currency_grouping_sep).charAt(0),
                            getString(R.string.translate_false_currency_decimal_sep).charAt(0));
                    productTextPrice.setText(formatPrice);
                }

                if (product.getBulk_price() != null) {
                    String formatPrice = Utils.formatCurrency(
                            product.getBulk_price(), getString(R.string.translate_false_currency_symbol)+" ",
                            getString(R.string.translate_false_currency_grouping_sep).charAt(0),
                            getString(R.string.translate_false_currency_decimal_sep).charAt(0));
                    productTextPriceBulk.setText(formatPrice);
                }

                productName.setText(product.getProduct_name());
                productTextQty.setText(String.valueOf(product.getProduct_qty())+" "+getString(R.string.product_qty_symbol));

                if (product.getCategory_id() != null) {
                    String categoryLabel = helper.getCategoryById(Integer.valueOf(product.getCategory_id())).getCategory_name();
                    productTextCategory.setText(categoryLabel);
                }

                if (String.valueOf(product.getLocation_id()) != null) {
                    String locationLabel = helper.getLocationById(product.getLocation_id()).getLocation_name();
                    productTextLocation.setText(locationLabel);
                }

                productDetail.setText(product.getProduct_desc());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
