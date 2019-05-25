package com.helwan.bis.review.screens.loginscreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.screens.homescreen.HomeActivity;
import com.helwan.bis.review.screens.loginscreen.fragments.LoginFragment;
import com.helwan.bis.review.screens.loginscreen.fragments.SignupFragment;
import com.shashank.sony.fancytoastlib.FancyToast;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, LoginCommunicator {

    private LoginContract.Presenter presenter;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private LoginFragment loginFragment;
    private SignupFragment signupFragment;
    private long time;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenterImpl(this);
        fragmentManager = getSupportFragmentManager();
        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        if (savedInstanceState == null) {
            initFragment();
        }
    }

    /**
     * Initialize Fragment
     */
    private void initFragment() {
        loginFragment = new LoginFragment();
        signupFragment = new SignupFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame, loginFragment, "loginFragment");
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = presenter.onStart();
        if (user != null) {
            login(user);
        }
    }

    @Override
    public void login(FirebaseUser user) {
        alertDialog.dismiss();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void toast(String message, boolean status) {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (status) {
            FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        } else {
            FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
    }

    @Override
    public void replaceFragment(String fragmentTag) {
        transaction = fragmentManager.beginTransaction();
        switch (fragmentTag) {
            case "loginFragment":
                transaction.replace(R.id.frame, loginFragment, "loginFragment");
                break;
            case "signupFragment":
                transaction.replace(R.id.frame, signupFragment, "signupFragment");
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onForgetPasswordClick(String email) {
        presenter.onForgetPasswordClick(email);
    }

    @Override
    public void onButtonLoginClick(User user) {
        presenter.onButtonLoginClick(user);
        alertDialog.show();
    }

    @Override
    public void onButtonRegisterClick(User user) {
        presenter.onButtonRegisterClick(user);
        alertDialog.show();
    }

    /**
     * Handle Backpressed to close app
     */
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            if (time + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                FancyToast.makeText(this, "Press back again to close", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
            time = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
