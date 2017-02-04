package com.septianfujianto.inventorymini.models;

/**
 * Created by Septian A. Fujianto on 2/4/2017.
 */

public class ProductFilter {
    private String category_id;
    private Double minPrice, maxPrice;
    private int minQty, maxQty;

    public ProductFilter() {}

    public ProductFilter(String category_id, Double minPrice, Double maxPrice, int minQty, int maxQty) {
        this.category_id = category_id;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minQty = minQty;
        this.maxQty = maxQty;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }
}
