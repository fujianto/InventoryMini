package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.SquaredImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.septianfujianto.inventorymini.R.id.producDesc;
import static com.septianfujianto.inventorymini.R.id.productName;

public class DetailProductActivity extends AppCompatActivity {
    private int productId;
    private MiniRealmHelper helper;
    private TextView productName, productDetail;
    private ImageView featuredImage;
    private Context context;
    @BindView(R.id.fabDelete) FloatingActionButton fabDelete;
    @BindView(R.id.fabEdit)  FloatingActionButton fabEdit;

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
                Toast.makeText(context, "EDIT PRODUCT", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSingleProduct() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            productId = extras.getInt(getString(R.string.productId));

            try {
                Product product = helper.getProductById(productId);

                if (product.getProduct_image() != null) {
                    Uri uriImage = Uri.parse(product.getProduct_image());
                    featuredImage.setImageURI(uriImage);
                }

                productName.setText(product.getProduct_name());
                productDetail.setText(product.getProduct_desc());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
