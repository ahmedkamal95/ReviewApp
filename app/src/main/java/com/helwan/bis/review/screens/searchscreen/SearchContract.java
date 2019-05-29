package com.helwan.bis.review.screens.searchscreen;

import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.dao.Search;

import java.util.List;

public interface SearchContract {

    interface View {
        void setSearchList(List<Search> searchList);

        void toast(String message, String type);
    }

    interface Presenter {
        void getSearchList(String itemName);

        void setSearchList(List<Search> searchList);

        void toast(String message, String type);

        void onDestroy();
    }
}
