package com.helwan.bis.review.model.dao;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class Comment {

    private String userId;
    private String userName;
    private String comment;
    private int userRate;

    public Comment() {
    }

    public Comment(String userId, String userName, String comment, int userRate) {
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.userRate = userRate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserRate() {
        return userRate;
    }

    public void setUserRate(int userRate) {
        this.userRate = userRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Comment)) return false;
        Comment comment = (Comment) obj;
        return userId.equals(comment.userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, comment, userRate);
    }
}
