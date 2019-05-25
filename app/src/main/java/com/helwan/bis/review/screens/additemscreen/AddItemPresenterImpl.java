package com.helwan.bis.review.screens.additemscreen;

import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.model.database.FirebaseServices;

import java.util.List;

public class AddItemPresenterImpl implements AddItemContract.Presenter {

    private AddItemContract.View view;
    private FirebaseServices firebaseServices;

    AddItemPresenterImpl(AddItemContract.View view) {
        this.view = view;
        firebaseServices = FirebaseServices.getInstance();
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
    public void onDestroy() {
        view = null;
    }
}
