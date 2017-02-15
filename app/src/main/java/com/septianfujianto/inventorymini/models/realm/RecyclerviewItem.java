package com.septianfujianto.inventorymini.models.realm;

/**
 * Created by Septian A. Fujianto on 2/14/2017.
 */

public interface RecyclerviewItem {
    String getTitle();
    String getValue();
    String getDetail();
    void setTitle(String title);
    void setValue(String value);
    void setDetail(String detail);
}
