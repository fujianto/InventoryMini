package com.septianfujianto.inventorymini.ui.product;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.SquaredImageView;

import butterknife.BindView;

import static com.septianfujianto.inventorymini.R.id.producDesc;
import static com.septianfujianto.inventorymini.R.id.productName;

public class DetailProductActivity extends AppCompatActivity {
    private int productId;
    private MiniRealmHelper helper;
    private TextView productName, productDetail;
    private ImageView featuredImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarDetailProduct));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        productName = (TextView) findViewById(R.id.productName);
        featuredImage = (ImageView) findViewById(R.id.featuredImage);
        productDetail = (TextView) findViewById(R.id.productDescription);

        helper = new MiniRealmHelper(this);
        loadSingleProduct();
    }

    private void loadSingleProduct() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            productId = extras.getInt(getString(R.string.productId));

            try {
                Product product = helper.getProductById(productId);

                if (product.getProduct_image() != null) {
                    byte[] dbImage = product.getProduct_image();
                    featuredImage.setImageBitmap(BitmapFactory.decodeByteArray(dbImage, 0, dbImage.length));
                }

                productName.setText(product.getProduct_name());
                productDetail.setText(product.getProduct_desc());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
