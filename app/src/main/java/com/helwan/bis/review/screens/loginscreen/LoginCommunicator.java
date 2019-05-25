package com.helwan.bis.review.screens.loginscreen;

import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.screens.loginscreen.fragments.SignupFragment;

public interface LoginCommunicator {

    void replaceFragment(String fragmentTag);

    void onButtonLoginClick(User user);

    void onButtonRegisterClick(User user);

    void toast(String message, boolean status);

    void onForgetPasswordClick(String email);
}
