package com.helwan.bis.review.model.dao;

import java.util.HashMap;

public class Search {
    private String name;
    private String description;
    private String price;
    private Float rateCount;
    private Float rateSum;
    private HashMap<String, String> images;
    private String brand;
    private String mainCategory;

    public Search() {
    }

    public Search(String name, String description, String price, Float rateCount, Float rateSum, HashMap<String, String> images, String brand, String mainCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rateCount = rateCount;
        this.rateSum = rateSum;
        this.images = images;
        this.brand = brand;
        this.mainCategory = mainCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Float getRateCount() {
        return rateCount;
    }

    public void setRateCount(Float rateCount) {
        this.rateCount = rateCount;
    }

    public Float getRateSum() {
        return rateSum;
    }

    public void setRateSum(Float rateSum) {
        this.rateSum = rateSum;
    }

    public HashMap<String, String> getImages() {
        return images;
    }

    public void setImages(HashMap<String, String> images) {
        this.images = images;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }
}
