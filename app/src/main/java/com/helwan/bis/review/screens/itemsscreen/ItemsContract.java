package com.helwan.bis.review.screens.itemsscreen;

import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.model.dao.Item;

import java.util.List;

public interface ItemsContract {

    interface View {
        void setItemsList(List<Item> items);
    }

    interface Presenter {
        void getItems(String mainCategory, String brandName);

        void setItemsList(List<Item> items);

        void onDestroy();
    }
}
