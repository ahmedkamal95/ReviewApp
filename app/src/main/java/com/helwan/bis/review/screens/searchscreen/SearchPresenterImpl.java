package com.helwan.bis.review.screens.searchscreen;

import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.dao.Search;
import com.helwan.bis.review.model.database.FirebaseServices;
import com.helwan.bis.review.screens.itemsscreen.ItemsContract;

import java.util.List;

public class SearchPresenterImpl implements SearchContract.Presenter {

    private SearchContract.View view;
    private FirebaseServices firebaseServices;

    SearchPresenterImpl(SearchContract.View view) {
        this.view = view;
        firebaseServices = FirebaseServices.getInstance();
    }

    @Override
    public void getSearchList(String itemName) {
        firebaseServices.search(this, itemName);
    }

    @Override
    public void setSearchList(List<Search> searchList) {
        view.setSearchList(searchList);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
