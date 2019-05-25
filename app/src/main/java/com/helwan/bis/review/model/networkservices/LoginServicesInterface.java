package com.helwan.bis.review.model.networkservices;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.helwan.bis.review.model.dao.User;

public interface LoginServicesInterface {
    void signInWithEmailAndPassword(User user);

    void createUserWithEmailAndPassword(User user);

    FirebaseUser isLoggedIn();

    void forgetPassword(String email);
}
