package com.helwan.bis.review.screens.homescreen;

import com.helwan.bis.review.model.dao.MainCategoryItem;

import java.util.List;

public interface HomeContract {

    interface View {
        void setMainCategoryList(List<MainCategoryItem> mainCategoryList);
    }

    interface Presenter {
        void getMainCategory();

        void setMainCategoryList(List<MainCategoryItem> mainCategoryList);

        void onDestroy();
    }
}
