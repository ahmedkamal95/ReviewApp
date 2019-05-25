package com.helwan.bis.review.model.dao;

public class Brand {

    private String name;
    private String image;
    private int itemCount;

    public Brand() {
    }

    public Brand(String name, String image, int itemCount) {
        this.name = name;
        this.image = image;
        this.itemCount = itemCount;
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

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

}
