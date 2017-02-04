package com.septianfujianto.inventorymini.ui.product;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.ProductFilter;
import com.septianfujianto.inventorymini.models.realm.Category;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

import static android.R.attr.filter;
import static com.septianfujianto.inventorymini.R.id.filterMaxQty;
import static com.septianfujianto.inventorymini.R.id.filterMinQty;

/**
 * Created by Septian A. Fujianto on 2/4/2017.
 */

public class filterDialogFragment extends BottomSheetDialogFragment {
    private Spinner spinnerCat;
    private EditText minPrice, maxPrice, minQty, maxQty;
    private Button btnFilter;
    private List<Integer> listCatId;
    private List<String> listCatLabel;
    private MiniRealmHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = View.inflate(getContext(), R.layout.filter_bottom_sheet_modal, null);
        helper = new MiniRealmHelper(getContext());

        spinnerCat = (Spinner) contentView.findViewById(R.id.filterProductCat);
        minPrice = (EditText) contentView.findViewById(R.id.filterMinPrice);
        maxPrice = (EditText) contentView.findViewById(R.id.filterMaxPrice);
        minQty = (EditText) contentView.findViewById(filterMinQty);
        maxQty = (EditText) contentView.findViewById(filterMaxQty);
        btnFilter = (Button) contentView.findViewById(R.id.btnFilter);

        listCatId = new ArrayList<>();
        listCatLabel = new ArrayList<>();

        setupCatSpinner();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               filterProducts();
            }
        });

        return contentView;

    }

    private void filterProducts() {
        ListProductActivity activity = (ListProductActivity) getActivity();
        String selCatId;

        ProductFilter filter = new ProductFilter();

        if (spinnerCat.getSelectedItemPosition() > 0) {
            selCatId = String.valueOf(listCatId.get(spinnerCat.getSelectedItemPosition() - 1));
            filter.setCategory_id(selCatId);
        }

        if (minPrice.getText().toString().length() > 0 && maxPrice.getText().toString().length() > 0) {
            Double filterMinPrice = Double.valueOf(minPrice.getText().toString());
            Double filterMaxPrice = Double.valueOf(maxPrice.getText().toString());

            filter.setMinPrice(filterMinPrice);
            filter.setMaxPrice(filterMaxPrice);
        }

        if (minQty.getText().toString().length() > 0 && maxQty.getText().toString().length() > 0) {
            int filterMaxQty = Integer.valueOf(maxQty.getText().toString());
            int filterMinQty = Integer.valueOf(minQty.getText().toString());

            filter.setMinQty(filterMinQty);
            filter.setMaxQty(filterMaxQty);
        }

        activity.filterProduct(filter);
    }

    private void setupCatSpinner() {
        RealmResults<Category> results = helper.getCategories();

        if (results != null) {
            listCatLabel.add(getString(R.string.select_cat));

            for (Category item : results) {
                listCatId.add(item.getCategory_id());
                listCatLabel.add(item.getCategory_name());
            }
        }

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listCatLabel);
        catAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinnerCat.setAdapter(catAdapter);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.filter_bottom_sheet_modal, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
