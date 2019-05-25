package com.helwan.bis.review.screens.itemsscreen;

import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.database.FirebaseServices;

import java.util.List;

public class ItemsPresenterImpl implements ItemsContract.Presenter {

    private ItemsContract.View view;
    private FirebaseServices firebaseServices;

    ItemsPresenterImpl(ItemsContract.View view) {
        this.view = view;
        firebaseServices = FirebaseServices.getInstance();
    }

    @Override
    public void getItems(String mainCategory, String brandName) {
        firebaseServices.fetchItemsList(this, mainCategory, brandName);
    }

    @Override
    public void setItemsList(List<Item> brands) {
        view.setItemsList(brands);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
