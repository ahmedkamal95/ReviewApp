package com.helwan.bis.review.screens.loginscreen;

import com.google.firebase.auth.FirebaseUser;
import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.model.networkservices.LoginServices;
import com.helwan.bis.review.model.networkservices.LoginServicesInterface;

public class LoginPresenterImpl implements LoginContract.Presenter {

    private LoginContract.View view;
    private LoginServicesInterface loginServices;

    LoginPresenterImpl(LoginContract.View view) {
        this.view = view;
        loginServices = new LoginServices(this);
    }

    @Override
    public void toast(String message, boolean status) {
        view.toast(message,status);
    }


    @Override
    public void login(FirebaseUser user) {
        view.login(user);
    }

    @Override
    public void onButtonLoginClick(User user) {
        loginServices.signInWithEmailAndPassword(user);
    }

    @Override
    public void onButtonRegisterClick(User user) {
        loginServices.createUserWithEmailAndPassword(user);
    }

    @Override
    public FirebaseUser onStart() {
        return loginServices.isLoggedIn();
    }

    @Override
    public void onForgetPasswordClick(String email) {
        loginServices.forgetPassword(email);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
