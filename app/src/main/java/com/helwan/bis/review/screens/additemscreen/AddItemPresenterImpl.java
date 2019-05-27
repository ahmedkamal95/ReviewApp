package com.helwan.bis.review.screens.additemscreen;

import android.net.Uri;

import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.model.database.FirebaseServices;
import com.helwan.bis.review.model.networkservices.UploadImageServices;
import com.helwan.bis.review.model.networkservices.UploadImageServicesInterface;

import java.util.HashMap;
import java.util.List;

public class AddItemPresenterImpl implements AddItemContract.Presenter {

    private AddItemContract.View view;
    private FirebaseServices firebaseServices;
    private UploadImageServicesInterface uploadImageServices;

    AddItemPresenterImpl(AddItemContract.View view) {
        this.view = view;
        firebaseServices = FirebaseServices.getInstance();
        uploadImageServices = new UploadImageServices(view, this);
    }

    @Override
    public void getMainCategory() {
        firebaseServices.fetchMainCategoryList(this);
    }

    @Override
    public void setMainCategoryList(List<MainCategoryItem> mainCategoryList) {
        view.setMainCategoryList(mainCategoryList);
    }

    @Override
    public void getBrands(String mainCategory) {
        firebaseServices.fetchBrandsList(this, mainCategory);
    }

    @Override
    public void setBrandsList(List<Brand> brands) {
        view.setBrandsList(brands);
    }

    @Override
    public void addProduct(Item item, String mainCategoryName, String brandName, int itemCount) {
        firebaseServices.insertProduct(this, item, mainCategoryName, brandName, itemCount);
    }

    @Override
    public void setImageUrl(String downloadUrl, String imageName, boolean finish) {
        view.setImageUrl(downloadUrl, imageName, finish);
    }

    @Override
    public void uploadImage(List<Uri> imagesUri, String brandName, String itemName) {
        uploadImageServices.uploadImage(imagesUri, brandName, itemName);
    }

    @Override
    public void addImage(String mainCategoryName, String brandName, String itemName, HashMap<String, String> images) {
        firebaseServices.insertImage(this, mainCategoryName, brandName, itemName, images);
    }

    @Override
    public void uploadFinish() {
        view.uploadFinish();
    }

    @Override
    public void itemExists(boolean isExist) {
        view.itemExists(isExist);
    }

    @Override
    public void onDestroy() {
        uploadImageServices.onDestroy();
        view = null;
    }
}
