package com.septianfujianto.inventorymini.ui.location;

import android.content.Context;
import android.location.LocationListener;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Location;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.ui.category.CategoryAdapter;

import java.util.List;

import static com.septianfujianto.inventorymini.R.id.rowCatName;

/**
 * Created by Septian A. Fujianto on 2/5/2017.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private List<Location> locations;
    private Context context;
    private LocationListener listener;
    private MiniRealmHelper helper;

    interface LocationListener {
        void onLocationDelete(int pos);
        void onLocationUpdate(boolean editing, int locationId, String locationLabel);
    }

    public LocationAdapter(Context context, LocationListener listener, List<Location> locations) {
        this.locations = locations;
        this.context = context;
        this.locations = locations;
        this.listener = listener;
        helper = new MiniRealmHelper(context);
    }

    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_location_row, parent, false);
        final LocationHolder holder = new LocationHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(LocationHolder holder, int position) {
        final int pos = holder.getAdapterPosition();

        if (!locations.isEmpty()) {
            holder.rowLocationName.setText(locations.get(pos).getLocation_name());
        }

        holder.rowBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new MiniRealmHelper(view.getContext());
                int id = locations.get(pos).getLocation_id();
                String catName = locations.get(pos).getLocation_name();
                helper.deleteLocationById(id);
                listener.onLocationDelete(pos);
                Toast.makeText(view.getContext(), "Deleting category "+catName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.rowBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLocationUpdate(true, locations.get(pos).getLocation_id(), locations.get(pos).getLocation_name());
                Toast.makeText(view.getContext(), "Updating category "+pos, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder {
        public TextView rowLocationName;
        public Button rowBtnEdit, rowBtnDelete;

        public LocationHolder(View itemView) {
            super(itemView);
            rowLocationName = (TextView) itemView.findViewById(R.id.rowLocationName);
            rowBtnEdit= (Button) itemView.findViewById(R.id.rowBtnEdit);
            rowBtnDelete= (Button) itemView.findViewById(R.id.rowBtnDelete);
        }
    }
}
