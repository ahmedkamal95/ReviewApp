package com.helwan.bis.review.screens.loginscreen;

import com.google.firebase.auth.FirebaseUser;
import com.helwan.bis.review.model.dao.User;

public interface LoginContract {

    interface View {
        void login(FirebaseUser user);

        void toast(String message, boolean status);

    }

    interface Presenter {
        void toast(String message, boolean status);

        void login(FirebaseUser currentUser);

        void onButtonLoginClick(User user);

        void onButtonRegisterClick(User user);

        FirebaseUser onStart();

        void onDestroy();

        void onForgetPasswordClick(String email);
    }
}
