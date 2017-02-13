package com.septianfujianto.inventorymini.ui.product;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconButton;
import com.mvc.imagepicker.ImagePicker;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Category;
import com.septianfujianto.inventorymini.models.realm.Location;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.FileUtils;
import com.septianfujianto.inventorymini.utils.InstantAutoComplete;
import com.septianfujianto.inventorymini.utils.SquaredImageView;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

import static com.septianfujianto.inventorymini.R.array.weight_label;


public class CreateProductActivity extends AppCompatActivity implements ProductPresenter.ProductPresenterListener {
    @BindView(R.id.productImage)
    SquaredImageView productImage;
    @BindView(R.id.productCat)
    Spinner spinnerCat;
    @BindView(R.id.productLocation)
    Spinner spinnerLocation;
    @BindView(R.id.btnAddProduct)
    Button btnAddProduct;
    @BindView(R.id.btnAddImage)
    Button btnAddImage;
    @BindView(R.id.productName)
    EditText productName;
    @BindView(R.id.producDesc)
    EditText productDesc;
    @BindView(R.id.productQty)
    EditText productQty;
    @BindView(R.id.productPrice)
    EditText productPrice;
    @BindView(R.id.productPriceBulk)
    EditText productPriceBulk;
    @BindView(R.id.productQtyLabel)
    InstantAutoComplete productQtyLabel;
    @BindView(R.id.productBrand)
    InstantAutoComplete productBrand;
    @BindView(R.id.productWeight)
    EditText productWeight;
    @BindView(R.id.productWeightLabel)
    InstantAutoComplete productWeightLabel;
    @BindView(R.id.productQtyWatch)
    EditText productQtyWatch;
    @BindView(R.id.btnCreateCategory)
    IconButton btnCreateCategory;
    @BindView(R.id.btnCreateLocation)
    IconButton btnCreateLocation;

    private List<Integer> listCatId;
    private List<String> listCatLabel;
    private List<Integer> listLocationId;
    private List<String> listLocationLabel;
    private MiniRealmHelper helper;
    private Context context;
    private ProductPresenter productPresenter;
    private String imagePath;
    private Bundle extras;
    private int productId;
    private ArrayAdapter<String> catAdapter;
    private ArrayAdapter<String> locationAdapter;
    private Activity activity;
    private String validatedMessage = "";
    private String[] qtyLabel;
    private String[] weight_lbl;
    private String[] brandContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        ButterKnife.bind(this);
        context = this;
        activity = this;
        getSupportActionBar().setTitle(getString(R.string.bar_title_create_product));
        helper = new MiniRealmHelper(this);
        ImagePicker.setMinQuality(400, 400);
        extras = getIntent().getExtras();
        ButterKnife.bind(this);
        productPresenter = new ProductPresenter(this, this);

        if (savedInstanceState == null) {
            setUpSpinner();
            setUpSpinnerLocation();
            setupAutoTextview();
        }

        if (extras != null) {
            productId = extras.getInt(getString(R.string.translate_false_productId));
            getSupportActionBar().setTitle(getString(R.string.editing_product) + " #" + productId);
            setupEditForm(productId);
        }

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FileUtils().checkStoragePermissions(activity);
                ImagePicker.pickImage((Activity) context, getString(R.string.select_image));
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productPresenter.addNewProduct();
            }
        });

        btnCreateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSpinnerItem(Category.class);
            }
        });

        btnCreateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSpinnerItem(Location.class);
            }
        });
    }

    private void createSpinnerItem(final Class obj) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        if (Category.class == obj) {
            alert.setTitle(getResources().getString(R.string.bar_title_create_cat));
        }

        if (Location.class == obj) {
            alert.setTitle(getResources().getString(R.string.bar_title_create_location));
        }

        final EditText input = new EditText(CreateProductActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);

        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Category.class == obj) {
                    Toast.makeText(context, getString(R.string.msg_category_created), Toast.LENGTH_SHORT).show();
                    helper.insertCategory(helper.getNextKey(Category.class, "category_id"), input.getText().toString());
                    listCatLabel.clear();
                    catAdapter.notifyDataSetChanged();
                    setUpSpinner();
                }

                if (Location.class == obj) {
                    Toast.makeText(context, getString(R.string.msg_location_created), Toast.LENGTH_SHORT).show();
                    helper.insertLocation(helper.getNextKey(Location.class, "location_id"), input.getText().toString());
                    listLocationLabel.clear();
                    locationAdapter.notifyDataSetChanged();
                    setUpSpinnerLocation();
                }
            }
        });

        alert.setNeutralButton(getString(R.string.cancels), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    private void setupAutoTextview() {
        qtyLabel = getResources().getStringArray(R.array.qty_label);
        List<String> qtyLbl = helper.getProductsAutocompleteLabel("product_qty_label", qtyLabel);

        weight_lbl = getResources().getStringArray(weight_label);
        List<String> weightLbl = helper.getProductsAutocompleteLabel("product_weight_label", weight_lbl);

        brandContent = getResources().getStringArray(R.array.brand_content);
        List<String> brandContentLbl = helper.getProductsAutocompleteLabel("product_brand", brandContent);

        ArrayAdapter<String> qtyLabelAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, qtyLbl);
        productQtyLabel.setText(qtyLbl.get(0));
        productQtyLabel.setAdapter(qtyLabelAdapter);

        ArrayAdapter<String> weightLabelAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, weightLbl);
        productWeightLabel.setText(weightLbl.get(0));
        productWeightLabel.setAdapter(weightLabelAdapter);

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, brandContentLbl);
        productBrand.setText(brandContentLbl.get(0));
        productBrand.setAdapter(brandAdapter);
    }

    private void setupEditForm(int productId) {
        try {
            Product product = helper.getProductById(productId);
            String catId = product.getCategory_id();
            Category categoryResults = helper.getCategoryById(Integer.valueOf(catId));
            String catName = categoryResults.getCategory_name();

            int locationId = product.getLocation_id();
            Location locationResults = helper.getLocationById(locationId);
            String locationName = locationResults.getLocation_name();

            if (!catName.equals(null)) {
                spinnerCat.setSelection(catAdapter.getPosition(catName));
            }

            if (!locationName.equals(null)) {
                spinnerLocation.setSelection(locationAdapter.getPosition(locationName));
            }

            if (product.getProduct_image() != null) {
                imagePath = product.getProduct_image();
                productImage.setImageURI(Uri.parse(imagePath));
            }

            productName.setText(product.getProduct_name());
            productQty.setText(String.valueOf(product.getProduct_qty()));
            productDesc.setText(product.getProduct_desc());
            productPrice.setText(String.valueOf(product.getPrice()));
            productPriceBulk.setText(product.getBulk_price() != null ? String.valueOf(product.getBulk_price()) : "");

            productQtyLabel.setText(product.getProduct_qty_label());
            productBrand.setText(product.getProduct_brand());
            productWeight.setText(String.valueOf(product.getProduct_weight()));
            productWeightLabel.setText(product.getProduct_weight_label());
            productQtyWatch.setText(String.valueOf(product.getProduct_qty_watch()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetFormField() {
        spinnerCat.setSelection(0);
        spinnerLocation.setSelection(0);
        productImage.setImageURI(null);
        productName.setText("");
        productQty.setText("");
        productWeight.setText("");
        productQtyWatch.setText("");
        productDesc.setText("");
        productBrand.setText("");
        productPrice.setText("");
        productPriceBulk.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        if (bitmap != null) {
            Bitmap sBitmap = FileUtils.scaleDown(bitmap, 500, false);
            imagePath = FileUtils.getRealPathFromURI(context, FileUtils.getImageUri(context, sBitmap));
            productImage.setImageURI(Uri.parse(imagePath));
        }
    }

    private void setUpSpinner() {
        listCatId = new ArrayList<>();
        listCatLabel = new ArrayList<>();
        catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCatLabel);
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

    private void setUpSpinnerLocation() {
        listLocationId = new ArrayList<>();
        listLocationLabel = new ArrayList<>();
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLocationLabel);
        locationAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        RealmResults<Location> results = helper.getLocations();

        if (results != null) {
            listLocationLabel.add(getString(R.string.select_location));

            for (Location item : results) {
                listLocationId.add(item.getLocation_id());
                listLocationLabel.add(item.getLocation_name());
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerLocation.setAdapter(locationAdapter);
            }
        });
    }

    private Boolean isValidProductFormField() {
        System.out.println("``` " + productQtyWatch.getText().length());
        if (Utils.isFormFilled(productName.getText().toString()) == false) {
            productName.setError(getString(R.string.msg_product_name_error));
            validatedMessage = getString(R.string.msg_product_name_error);
            return false;
        } else if (productQty.getText().toString().length() < 1) {
            productQty.setError(getString(R.string.msg_product_qty_error));
            validatedMessage = getString(R.string.msg_product_qty_error);
            return false;
        } else if (productPrice.getText().toString().length() < 1) {
            productPrice.setError(getString(R.string.msg_product_price_error));
            validatedMessage = getString(R.string.msg_product_price_error);
            return false;
        } else if (spinnerCat.getSelectedItemPosition() == 0) {
            validatedMessage = getString(R.string.msg_product_cat_error);
            return false;
        } else if (spinnerLocation.getSelectedItemPosition() == 0) {
            validatedMessage = getString(R.string.msg_product_location_error);
            return false;
        } else {

            if (extras != null) {
                productId = extras.getInt(getString(R.string.translate_false_productId));
                validatedMessage = getString(R.string.msg_product_updated);
            } else {
                validatedMessage = getString(R.string.msg_product_created);
            }

            return true;
        }
    }

    @Override
    public void productLoaded() {

    }

    @Override
    public void productUpdated() {
        resetFormField();
    }

    @Override
    public Product createdProduct() {
        Product product = new Product();

        if (isValidProductFormField()) {
            product.setProduct_id(extras != null ? productId : helper.getNextKey(Product.class, "product_id"));
            product.setProduct_image(imagePath);
            product.setProduct_name(productName.getText().toString());
            product.setProduct_qty(Integer.valueOf(productQty.getText().toString()));
            product.setProduct_qty_label(productQtyLabel.getText().length() < 1 ? qtyLabel[0] : productQtyLabel.getText().toString());
            product.setProduct_qty_watch(productQtyWatch.getText().length() < 1 ? 1 : Integer.valueOf(productQtyWatch.getText().toString()));
            product.setProduct_brand(productBrand.getText().toString().length() < 1 ? brandContent[0] : productBrand.getText().toString());
            product.setProduct_weight(productWeight.getText().length() < 1 ? 1 : Integer.valueOf(productWeight.getText().toString()));

            product.setProduct_weight_label(productWeightLabel.getText().length() < 1 ? weight_lbl[0] : productWeightLabel.getText().toString());


            if (spinnerCat.getSelectedItem() != null && spinnerCat.getSelectedItemPosition() > 0) {
                int selCatid = listCatId.get(spinnerCat.getSelectedItemPosition() - 1);

                product.setCategory_id(String.valueOf(selCatid));
            } else {
                product.setCategory_id("0");
            }

            if (spinnerLocation.getSelectedItem() != null && spinnerLocation.getSelectedItemPosition() > 0) {
                int selLocationId = listLocationId.get(spinnerLocation.getSelectedItemPosition() - 1);

                product.setLocation_id(selLocationId);
            } else {
                product.setLocation_id(0);
            }


            product.setProduct_desc(productDesc.getText().toString());
            product.setPrice(Double.valueOf(productPrice.getText().toString()));

            if (TextUtils.equals(productPriceBulk.getText().toString(), "")) {

            } else {
                product.setBulk_price(Double.valueOf(productPriceBulk.getText().toString()));
            }

            if (extras == null) {
                product.setDate_created(Utils.getTodayDate(""));
            } else {
                product.setDate_created(helper.getProductById(productId).getDate_created());
            }

            product.setDate_modified(Utils.getTodayDate(""));

            Toast.makeText(context, validatedMessage, Toast.LENGTH_SHORT).show();

            return product;
        } else {
            Toast.makeText(context, validatedMessage, Toast.LENGTH_SHORT).show();
            return null;
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
