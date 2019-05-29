package com.helwan.bis.review.screens.brandsscreen;

import com.helwan.bis.review.model.dao.Brand;

import java.util.List;

public interface BrandsContract {

    interface View {
        void setBrandsList(List<Brand> brands);

        void toast(String message, String type);
    }

    interface Presenter {
        void getBrands(String mainCategory);

        void setBrandsList(List<Brand> brands);

        void toast(String message, String type);

        void onDestroy();
    }
}
