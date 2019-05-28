package com.helwan.bis.review.screens.additemscreen;

import android.net.Uri;

import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.dao.MainCategoryItem;

import java.util.HashMap;
import java.util.List;

public interface AddItemContract {

    interface View {
        void setMainCategoryList(List<MainCategoryItem> mainCategoryList);

        void setBrandsList(List<Brand> brands);

        void setImageUrl(String downloadUrl, String imageName, boolean finish);

        void uploadFinish();

        void itemExists(boolean isExist);

        void toast(String message, String type);
    }

    interface Presenter {
        void getMainCategory();

        void setMainCategoryList(List<MainCategoryItem> mainCategoryList);

        void getBrands(String mainCategory);

        void setBrandsList(List<Brand> brands);

        void addProduct(Item item, String mainCategoryName, String brandName, int itemCount);

        void setImageUrl(String url, String downloadUrl, boolean finish);

        void uploadImage(List<Uri> imagesUri, String brandName, String itemName);

        void addImage(String mainCategoryName, String brandName, String itemName, HashMap<String, String> images);

        void uploadFinish();

        void itemExists(boolean isExist);

        void toast(String message, String type);

        void onDestroy();
    }
}
