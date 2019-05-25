package com.helwan.bis.review.model.dao;

public class MainCategoryItem {

    private String name;
    private String image;
    private int brandCount;

    public MainCategoryItem() {
    }

    public MainCategoryItem(String name, String image, int brandCount) {
        this.name = name;
        this.image = image;
        this.brandCount = brandCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getBrandCount() {
        return brandCount;
    }

    public void setBrandCount(int brandCount) {
        this.brandCount = brandCount;
    }

}
