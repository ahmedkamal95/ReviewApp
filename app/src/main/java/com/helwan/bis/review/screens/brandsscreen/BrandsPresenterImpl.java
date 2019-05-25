package com.helwan.bis.review.screens.brandsscreen;

import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.model.database.FirebaseServices;

import java.util.List;

public class BrandsPresenterImpl implements BrandsContract.Presenter {

    private BrandsContract.View view;
    private FirebaseServices firebaseServices;

    BrandsPresenterImpl(BrandsContract.View view) {
        this.view = view;
        firebaseServices = FirebaseServices.getInstance();
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
    public void onDestroy() {
        view = null;
    }
}
