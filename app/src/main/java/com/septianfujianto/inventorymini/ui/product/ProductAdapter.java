package com.septianfujianto.inventorymini.ui.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.septianfujianto.inventorymini.App;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Product;
import com.septianfujianto.inventorymini.utils.SharedPref;
import com.septianfujianto.inventorymini.utils.SquaredImageView;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.List;

/**
 * Created by Septian A. Fujianto on 2/2/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product_row, null);
        final ProductHolder holder = new ProductHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Double dbPrice = products.get(pos).getPrice();
        Double dbBulkPrice = products.get(pos).getBulk_price();
        int dbQty = products.get(pos).getProduct_qty();

        String currency_symbol = SharedPref.getString("currency_symbol") != null ?
                SharedPref.getString("currency_symbol") : App.getContext().getString(R.string.translate_false_currency_symbol);
        String grouping_separator = SharedPref.getString("grouping_separator") != null ?
                SharedPref.getString("grouping_separator") : App.getContext().getString(R.string.translate_false_currency_grouping_sep);
        String decimal_separator = SharedPref.getString("decimal_separator") != null ?
                SharedPref.getString("decimal_separator") : App.getContext().getString(R.string.translate_false_currency_decimal_sep);

        String price = dbPrice != null ? Utils.formatCurrency(
                dbPrice, currency_symbol+" ",
                grouping_separator.charAt(0),
                decimal_separator.charAt(0)) : "";


        String qty = String.valueOf(dbQty) != null ? String.valueOf(dbQty) : "0";

        String dbTitle = products.get(pos).getProduct_name();
        final int productId = products.get(pos).getProduct_id();

        if (products.get(pos).getProduct_image() != null) {
            Uri uriImage = Uri.parse(products.get(pos).getProduct_image());
            holder.featuredImage.setImageURI(uriImage);
        } else {
            holder.featuredImage.setImageResource(R.drawable.no_image);
        }

        String qtyLabel = products.get(pos).getProduct_qty_label() == null ? "pcs" :
                products.get(pos).getProduct_qty_label();

        holder.productTitle.setText(dbTitle);
        holder.productPrice.setText(price);
        holder.productBrand.setText(products.get(pos).getProduct_brand());
        holder.productQty.setText(qty+" "+qtyLabel);

        holder.productWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra(context.getString(R.string.translate_false_productId), productId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        private SquaredImageView featuredImage;
        private TextView productTitle, productPrice, productQty, productBrand;
        private LinearLayout productWrapper;

        public ProductHolder(View itemView) {
            super(itemView);

            productWrapper = (LinearLayout) itemView.findViewById(R.id.productWrapper);
            featuredImage = (SquaredImageView) itemView.findViewById(R.id.featuredImage);
            productBrand = (TextView) itemView.findViewById(R.id.productBrand);
            productTitle = (TextView) itemView.findViewById(R.id.productTitle);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
            productQty = (TextView) itemView.findViewById(R.id.productQty);
        }
    }
}
