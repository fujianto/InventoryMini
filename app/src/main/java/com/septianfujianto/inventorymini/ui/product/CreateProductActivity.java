package com.septianfujianto.inventorymini.ui.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Category;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.ImageUtils;
import com.septianfujianto.inventorymini.utils.SquaredImageView;
import com.septianfujianto.inventorymini.utils.Utils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;


public class CreateProductActivity extends AppCompatActivity implements ProductPresenter.ProductPresenterListener {
    @BindView(R.id.productImage) SquaredImageView productImage;
    @BindView(R.id.productCat) Spinner spinnerCat;
    @BindView(R.id.btnAddProduct) Button btnAddProduct;
    @BindView(R.id.btnAddImage) Button btnAddImage;
    @BindView(R.id.productName) EditText productName;
    @BindView(R.id.producDesc) EditText producDesc;
    @BindView(R.id.productQty) EditText productQty;
    @BindView(R.id.productPrice) EditText productPrice;
    @BindView(R.id.productPriceBulk) EditText productPriceBulk;

    private List<Integer> listCatId;
    private List<String> listCatLabel;
    private MiniRealmHelper helper;
    private Context context;
    private ProductPresenter productPresenter;
    private Bitmap bmp;
    private byte[] dbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        ButterKnife.bind(this);
        context = this;
        getSupportActionBar().setTitle(getString(R.string.bar_title_create_product));
        helper = new MiniRealmHelper(this);
        ImagePicker.setMinQuality(200, 200);

        productPresenter = new ProductPresenter(this, this);

        if (savedInstanceState == null)
            setUpSpinner();

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage((Activity) context, getString(R.string.select_image));
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productPresenter.addNewProduct();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        if (bitmap != null) {
            Bitmap sBitmap = ImageUtils.scaleDown(bitmap, 300, false);
            productImage.setImageBitmap(sBitmap);
            bmp = bitmap;
            dbImage = ImageUtils.bitmapToByte(bmp, Bitmap.CompressFormat.JPEG, 90);
        }
    }

    private void setUpSpinner() {
        listCatId = new ArrayList<>();
        listCatLabel = new ArrayList<>();
        final ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCatLabel);
        catAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        RealmResults<Category> results = helper.getCategories();

        if (results != null) {
            listCatLabel.add(getString(R.string.select_cat));

            for (Category item : results) {
                listCatId.add(item.getCategory_id());
                listCatLabel.add(item.getCategory_name());
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerCat.setAdapter(catAdapter);
            }
        });
    }

    private Boolean isValidProductFormField() {
        if (Utils.isFormFilled(productName.getText().toString()) &&
            productQty.getText().toString().length() > 0 &&
            Utils.isFormFilled(productPrice.getText().toString())  ) {

            return true;
        }

        return false;
    }

    @Override
    public void productLoaded() {

    }

    @Override
    public void productUpdated() {

    }

    @Override
    public Product createdProduct() {
        Product product = new Product();

        if (isValidProductFormField()) {
            product.setProduct_id(helper.getNextKey(Product.class, "product_id"));
            product.setProduct_image(dbImage);
            product.setProduct_name(productName.getText().toString());
            product.setProduct_qty(Integer.valueOf(productQty.getText().toString()));

            if (spinnerCat.getSelectedItem() != null && spinnerCat.getSelectedItemPosition() > 0) {
                int selCatid = listCatId.get(spinnerCat.getSelectedItemPosition() - 1);

                product.setCategory_id(String.valueOf(selCatid));
            } else {
                product.setCategory_id("0");
            }

            product.setProduct_desc(producDesc.getText().toString());
            product.setPrice(Double.valueOf(productPrice.getText().toString()));

            if (TextUtils.equals(productPriceBulk.getText().toString(), "")) {

            } else {
                product.setBulk_price(Double.valueOf(productPriceBulk.getText().toString()));
            }

            product.setDate_created(Utils.getTodayDate(""));
            product.setDate_modified(Utils.getTodayDate(""));

            Toast.makeText(context, getString(R.string.msg_product_created), Toast.LENGTH_SHORT).show();

            return product;
        } else {
            Toast.makeText(context, getString(R.string.msg_product_form_error), Toast.LENGTH_SHORT).show();
            return product;
        }


    }

    @Override
    public void productSearched() {

    }

    @Override
    public void productDeleted() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(context, ListProductActivity.class));
    }
}
