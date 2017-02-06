package com.septianfujianto.inventorymini.models.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Septian A. Fujianto on 1/31/2017.
 */

public class Product extends RealmObject {
    @PrimaryKey
    private int product_id;
    private String product_name;
    private String product_desc;
    private String product_image;
    private int product_qty;
    private String category_id;
    private int location_id;
    private Double price;
    private Double sale_price;
    private Double bulk_price;
    private String date_created;
    private String date_modified;

    public Product() {

    }

    public Product(int product_id, String product_name, String product_desc, String product_image,
                   int product_qty, String category_id, int location_id, Double price, Double sale_price,
                   Double bulk_price, String date_created, String date_modified) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_image = product_image;
        this.product_qty = product_qty;
        this.category_id = category_id;
        this.location_id = location_id;
        this.price = price;
        this.sale_price = sale_price;
        this.bulk_price = bulk_price;
        this.date_created = date_created;
        this.date_modified = date_modified;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public Double getBulk_price() {
        return bulk_price;
    }

    public void setBulk_price(Double bulk_price) {
        this.bulk_price = bulk_price;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }
}
