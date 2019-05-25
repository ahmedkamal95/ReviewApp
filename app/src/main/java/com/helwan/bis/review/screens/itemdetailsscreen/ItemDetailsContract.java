package com.helwan.bis.review.screens.itemdetailsscreen;

import com.helwan.bis.review.model.dao.Comment;

import java.util.List;

public interface ItemDetailsContract {

    interface View {
        void setCommentsList(List<Comment> comments);

        void updateComments(String msg, String comment, String displayName, String uid, Float rateSum);
    }

    interface Presenter {
        void getComments(String mainCategory, String brandName, String itemName);

        void setCommentsList(List<Comment> comments);

        void addComment(String mainCategory, String brandName, String itemName, String comment, int rating, Float rateCount, Float rateSum, int oldRating);

        void updateComments(String msg, String comment, String displayName, String uid, Float rateSum);

        String getUserId();

        void onDestroy();
    }
}
