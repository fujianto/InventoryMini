package com.septianfujianto.inventorymini.ui.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Category;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;

import java.util.List;

/**
 * Created by Septian A. Fujianto on 1/31/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private List<Category> categories;
    private Context context;
    private MiniRealmHelper helper;
    private CategoryListener listener;

    public interface CategoryListener{
        void onCategoryDelete(int pos);
        void onCategoryUpdate(boolean editing, int catId, String catLabel);
    }

    public CategoryAdapter(Context context, CategoryListener listener,List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
        helper = new MiniRealmHelper(context);
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_category_row, parent, false);
        final CategoryHolder holder = new CategoryHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        final int pos = holder.getAdapterPosition();

        if (!categories.isEmpty()) {
            holder.rowCatId.setText(String.valueOf(categories.get(pos).getCategory_id()));
            holder.rowCatName.setText(categories.get(pos).getCategory_name());
        }

        holder.rowBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new MiniRealmHelper(view.getContext());
                int id = categories.get(pos).getCategory_id();
                String catName = categories.get(pos).getCategory_name();
                helper.deleteCategoryById(id);
                listener.onCategoryDelete(pos);
                Toast.makeText(view.getContext(), "Deleting category "+catName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.rowBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCategoryUpdate(true, categories.get(pos).getCategory_id(), categories.get(pos).getCategory_name());
                Toast.makeText(view.getContext(), "Updating category "+pos, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        public TextView rowCatId, rowCatName;
        public Button rowBtnEdit, rowBtnDelete;

        public CategoryHolder(View itemView) {
            super(itemView);
            rowCatId = (TextView) itemView.findViewById(R.id.rowCatId);
            rowCatName = (TextView) itemView.findViewById(R.id.rowCatName);
            rowBtnEdit= (Button) itemView.findViewById(R.id.rowBtnEdit);
            rowBtnDelete= (Button) itemView.findViewById(R.id.rowBtnDelete);
        }
    }
}
