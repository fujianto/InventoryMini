package com.septianfujianto.inventorymini.models;

import com.septianfujianto.inventorymini.models.realm.RecyclerviewItem;

/**
 * Created by Septian A. Fujianto on 2/14/2017.
 */

public class OverviewItem implements RecyclerviewItem {
    private String title, value, detail;

    public OverviewItem(String title, String value, String detail) {
        this.title = title;
        this.value = value;
        this.detail = detail;
    }

    public OverviewItem() {

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
