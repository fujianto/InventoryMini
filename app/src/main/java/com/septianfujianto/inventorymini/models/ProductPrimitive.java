package com.septianfujianto.inventorymini.models;

/**
 * Created by Septian A. Fujianto on 2/5/2017.
 */

public class ProductPrimitive {

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
    private String product_brand;
    private String product_qty_label;
    private int product_weight;
    private String product_weight_label;
    private int product_qty_watch;

    public ProductPrimitive() {}

    public int getLocation_id() {
        return location_id;
    }

    public String getProduct_qty_label() {
        return product_qty_label;
    }

    public void setProduct_qty_label(String product_qty_label) {
        this.product_qty_label = product_qty_label;
    }

    public int getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(int product_weight) {
        this.product_weight = product_weight;
    }

    public String getProduct_weight_label() {
        return product_weight_label;
    }

    public void setProduct_weight_label(String product_weight_label) {
        this.product_weight_label = product_weight_label;
    }

    public int getProduct_qty_watch() {
        return product_qty_watch;
    }

    public void setProduct_qty_watch(int product_qty_watch) {
        this.product_qty_watch = product_qty_watch;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
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
