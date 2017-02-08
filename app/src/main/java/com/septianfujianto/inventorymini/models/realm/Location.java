package com.septianfujianto.inventorymini.models.realm;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Septian A. Fujianto on 2/5/2017.
 */

public class Location extends RealmObject {
    @PrimaryKey
    private int location_id;
    private String location_name;

    public Location() {

    }

    public Location(int location_id, String location_name) {
        this.location_id = location_id;
        this.location_name = location_name;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
}
