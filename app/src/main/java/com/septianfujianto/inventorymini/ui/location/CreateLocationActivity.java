package com.septianfujianto.inventorymini.ui.location;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.septianfujianto.inventorymini.R;
import com.septianfujianto.inventorymini.models.realm.Location;
import com.septianfujianto.inventorymini.models.realm.MiniRealmHelper;
import com.septianfujianto.inventorymini.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import io.realm.RealmResults;


public class CreateLocationActivity extends AppCompatActivity implements LocationAdapter.LocationListener {
    private MiniRealmHelper helper;
    private List<Location> locations;
    private LocationAdapter adapter;
    private RecyclerView mRecyclerView;
    private Context context;
    @BindView(R.id.btnCreateLocation) Button btnCreateLocation;
    @BindView(R.id.locationName) EditText locationName;
    private int locationId;
    private boolean editing;
    private String locationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_create_location);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getString(R.string.bar_title_create_location));
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_location);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        helper = new MiniRealmHelper(this);
        context = this;
        setupRecyclerView();

        btnCreateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing) {
                    updateLocation(locationId, locationLabel);
                } else {
                    createLocation();
                }
            }
        });
    }

    private void createLocation() {
        if (Utils.isFormFilled(locationName.getText().toString())) {
            String name = locationName.getText().toString();
            locationId = helper.getNextKey(Location.class, "location_id");

            helper.insertLocation(locationId, name);
            locations.clear();
            adapter.notifyDataSetChanged();
            loadLocation();

            Toast.makeText(this, getResources().getString(R.string.msg_location_created), Toast.LENGTH_SHORT).show();
            locationName.setText("");
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_location_error), Toast.LENGTH_SHORT).show();
        }

    }

    private void loadLocation() {
        RealmResults<Location> results = helper.getLocations();
        locations.addAll(results);

        mRecyclerView.setAdapter(adapter);
    }

    private void updateLocation(int locationId, String locationLabel) {
        String name = locationName.getText().toString();

        helper.insertLocation(locationId, name);

        locations.clear();
        adapter.notifyDataSetChanged();
        loadLocation();

        Toast.makeText(this, getResources().getString(R.string.msg_location_created), Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        locations = new ArrayList<>();
        adapter = new LocationAdapter(this, this, locations);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation()));
        RealmResults<Location> results = helper.getLocations();
        locations.addAll(results);

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLocationDelete(int pos) {
        mRecyclerView.removeViewAt(pos);
        locations.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, locations.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLocationUpdate(boolean editing, int locationId, String locationLabel) {
        this.editing = editing;
        this.locationId = locationId;
        this.locationLabel = locationLabel;

        locationName.setText(locationLabel);
    }
}
