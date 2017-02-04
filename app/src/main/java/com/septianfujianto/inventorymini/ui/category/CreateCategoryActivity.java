package com.septianfujianto.inventorymini.ui.category;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Category;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class CreateCategoryActivity extends AppCompatActivity implements CategoryAdapter.CategoryListener {
    @BindView(R.id.categoryName) EditText catName;
    @BindView(R.id.btnCreateCategory) Button btnCreateCat;
    @BindView(R.id.rcv_category) RecyclerView recyclerViewCat;
    private MiniRealmHelper helper;
    private CategoryAdapter adapter;
    private List<Category> categories;
    private int catId;
    private boolean editing;
    private String catLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        getSupportActionBar().setTitle(getResources().getString(R.string.bar_title_create_cat));
        ButterKnife.bind(this);

        helper = new MiniRealmHelper(this);

        setupRecyclerView();

        btnCreateCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing) {
                    updateCategory(catId, catLabel);
                } else {
                    createCategory();
                }

            }
        });
    }

    private void setupRecyclerView() {
        categories = new ArrayList<>();
        adapter = new CategoryAdapter(this, this, categories);

        recyclerViewCat.setHasFixedSize(true);
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this));

        loadCategories();
    }

    private void loadCategories() {
        RealmResults<Category> results = helper.getCategories();
        categories.addAll(results);

        recyclerViewCat.setAdapter(adapter);
    }

    private void createCategory() {
        if (Utils.isFormFilled(catName.getText().toString())) {
            String name = catName.getText().toString();
            catId = helper.getNextKey(Category.class, "category_id");

            helper.insertCategory(catId, name);
            categories.clear();
            adapter.notifyDataSetChanged();
            loadCategories();

            Toast.makeText(this, getResources().getString(R.string.msg_category_created), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_category_error), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateCategory(int catId, String cat) {
        String name = catName.getText().toString();

        helper.insertCategory(catId, name);

        categories.clear();
        adapter.notifyDataSetChanged();
        loadCategories();

        Toast.makeText(this, getResources().getString(R.string.msg_category_created), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryDelete(int pos) {
        recyclerViewCat.removeViewAt(pos);
        categories.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, categories.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryUpdate(boolean editing, int catId, String catLabel) {
        this.editing = editing;
        this.catId = catId;
        this.catLabel = catLabel;

        catName.setText(catLabel);
    }
}
