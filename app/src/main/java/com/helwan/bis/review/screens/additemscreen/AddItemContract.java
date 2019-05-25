package com.helwan.bis.review.screens.additemscreen;

import com.helwan.bis.review.model.dao.MainCategoryItem;

import java.util.List;

public interface AddItemContract {

    interface View {
        void setMainCategoryList(List<MainCategoryItem> mainCategoryList);
    }

    interface Presenter {
        void getMainCategory();

        void setMainCategoryList(List<MainCategoryItem> mainCategoryList);

        void onDestroy();
    }
}
