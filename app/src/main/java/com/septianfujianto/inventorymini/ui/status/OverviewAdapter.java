package com.septianfujianto.inventorymini.ui.status;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.RecyclerviewItem;

import java.util.List;


/**
 * Created by Septian A. Fujianto on 2/14/2017.
 */

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewHolder> {
    private Context mContext;
    private List<RecyclerviewItem> item;

    public OverviewAdapter(Context mContext, List<RecyclerviewItem> item) {
        this.mContext = mContext;
        this.item = item;
    }

    @Override
    public OverviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_overview_row, null);
        final OverviewHolder holder = new OverviewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(OverviewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        holder.overviewTitle.setText(item.get(pos).getTitle());
        holder.overviewValue.setText(item.get(pos).getValue());
        holder.overviewDetail.setText(item.get(pos).getDetail());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class OverviewHolder extends RecyclerView.ViewHolder {
        TextView overviewTitle;
        TextView overviewValue;
        TextView overviewDetail;

        public OverviewHolder(View itemView) {
            super(itemView);
            overviewTitle = (TextView) itemView.findViewById(R.id.overviewTitle);
            overviewValue = (TextView) itemView.findViewById(R.id.overviewValue);
            overviewDetail = (TextView) itemView.findViewById(R.id.overviewDetail);
        }
    }
}
