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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.septianfujianto.inventorymini.R;
import io.realm.Sort;


/**
 * Created by Septian A. Fujianto on 2/3/2017.
 */

public class SortDialogFragment extends BottomSheetDialogFragment {
    Button btnSort, btnReset;
    RadioGroup radGroupSort;
    RadioButton radBtnSortDateDesc, radBtnSortDateAsc, radBtnSortPriceDesc, radBtnSortPriceAsc,
            radBtnSortNameDesc, radBtnSortNameAsc, radBtnSortQtyDesc, radBtnSortQtyAsc;
    private String sortData;
    private Sort sortOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = View.inflate(getContext(), R.layout.sort_bottom_sheet_modal, null);
        btnSort = (Button) contentView.findViewById(R.id.btnSort);
        btnReset = (Button) contentView.findViewById(R.id.btnReset);
        radGroupSort = (RadioGroup) contentView.findViewById(R.id.radGroupSort);

        radBtnSortDateDesc = (RadioButton) contentView.findViewById(R.id.sortDateDesc);
        radBtnSortDateAsc = (RadioButton) contentView.findViewById(R.id.sortDateAsc);
        radBtnSortPriceDesc = (RadioButton) contentView.findViewById(R.id.sortPriceDesc);
        radBtnSortPriceAsc = (RadioButton) contentView.findViewById(R.id.sortPriceAsc);
        radBtnSortNameDesc = (RadioButton) contentView.findViewById(R.id.sortNameDesc);
        radBtnSortNameAsc = (RadioButton) contentView.findViewById(R.id.sortnameAsc);
        radBtnSortQtyDesc = (RadioButton) contentView.findViewById(R.id.sortQtyDesc);
        radBtnSortQtyAsc = (RadioButton) contentView.findViewById(R.id.sortQtyAsc);

        radGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.sortDateDesc) {
                    sortData = "date_created";
                    sortOrder = Sort.DESCENDING;
                } else if (checkedId == R.id.sortDateAsc) {
                    sortData = "date_created";
                    sortOrder = Sort.ASCENDING;
                } else if (checkedId == R.id.sortPriceDesc) {
                    sortData = "price";
                    sortOrder = Sort.DESCENDING;
                } else if (checkedId == R.id.sortPriceAsc) {
                    sortData = "price";
                    sortOrder = Sort.ASCENDING;
                } else if (checkedId == R.id.sortNameDesc) {
                    sortData = "product_name";
                    sortOrder = Sort.DESCENDING;
                } else if (checkedId == R.id.sortnameAsc) {
                    sortData = "product_name";
                    sortOrder = Sort.ASCENDING;
                } else if (checkedId == R.id.sortQtyDesc) {
                    sortData = "product_qty";
                    sortOrder = Sort.DESCENDING;
                } else if (checkedId == R.id.sortQtyAsc) {
                    sortData = "product_qty";
                    sortOrder = Sort.ASCENDING;
                }
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListProductActivity activity = (ListProductActivity) getActivity();
                activity.sortProduct(sortData, sortOrder);

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radGroupSort.clearCheck();
            }
        });

        return contentView;
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
        View contentView = View.inflate(getContext(), R.layout.sort_bottom_sheet_modal, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
