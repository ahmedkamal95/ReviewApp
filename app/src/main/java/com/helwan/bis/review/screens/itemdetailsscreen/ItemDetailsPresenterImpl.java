package com.helwan.bis.review.screens.itemdetailsscreen;

import com.helwan.bis.review.model.dao.Comment;
import com.helwan.bis.review.model.database.FirebaseServices;

import java.util.List;

public class ItemDetailsPresenterImpl implements ItemDetailsContract.Presenter {

    private ItemDetailsContract.View view;
    private FirebaseServices firebaseServices;

    ItemDetailsPresenterImpl(ItemDetailsContract.View view) {
        this.view = view;
        firebaseServices = FirebaseServices.getInstance();
    }

    @Override
    public void getComments(String mainCategory, String brandName, String itemName) {
        firebaseServices.fetchCommentsList(this, mainCategory, brandName, itemName);
    }

    @Override
    public void setCommentsList(List<Comment> comments) {
        view.setCommentsList(comments);
    }

    @Override
    public void addComment(String mainCategory, String brandName, String itemName, String comment, int rating, Float rateCount, Float rateSum, int oldRating) {
        firebaseServices.insertComment(this, mainCategory, brandName, itemName, comment, rating, rateCount, rateSum, oldRating);
    }

    @Override
    public void updateComments(String msg, String comment, String displayName, String uid, Float rateSum) {
        view.updateComments(msg, comment, displayName, uid, rateSum);
    }

    @Override
    public String getUserId() {
        return firebaseServices.getUserId();
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
