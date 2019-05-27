package com.helwan.bis.review.screens.loginscreen.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.screens.loginscreen.LoginCommunicator;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText edtEmailLogin, edtPasswordLogin;
    private Button btnLogin, btnSignupLoginPage, btnForgetPassword;
    private LoginCommunicator communicator;
    private User user;

    /**
     * Fragment constructor must be empty
     */
    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        addListeners();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (LoginCommunicator) context;
        user = new User();
    }

    /**
     * Initialize views
     *
     * @param view Created view after inflater
     */
    private void initViews(View view) {
        edtEmailLogin = view.findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = view.findViewById(R.id.edtPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnSignupLoginPage = view.findViewById(R.id.btnSignupLoginPage);
        btnForgetPassword = view.findViewById(R.id.btnForgetPassword);
    }

    /**
     * Add listeners
     */
    private void addListeners() {
        btnLogin.setOnClickListener(v -> {
            if (isDataValid()) {
                if (CheckInternetConnectionHelper.getInstance(getActivity()).checkInternet()) {
                    communicator.onButtonLoginClick(user);
                } else {
                    communicator.toast("No Internet Connection", false);
                }
            }
        });

        btnSignupLoginPage.setOnClickListener(v -> communicator.replaceFragment("signupFragment"));

        btnForgetPassword.setOnClickListener(v -> {
            AlertDialog dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
            Objects.requireNonNull(dialogBuilder.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            LayoutInflater inflater = this.getLayoutInflater();
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.layout_forget_password_dialog, null);

            EditText edtEmailForgetPassword = dialogView.findViewById(R.id.edtEmailForgetPassword);
            Button btnForgetPassword = dialogView.findViewById(R.id.btnForgetPassword);

            btnForgetPassword.setOnClickListener(view -> {
                String email = edtEmailForgetPassword.getText().toString().trim();
                if (email.equals("")) {
                    edtEmailForgetPassword.setError("Please Enter Your Email");
                } else {
                    if (CheckInternetConnectionHelper.getInstance(getActivity()).checkInternet()) {
                        communicator.onForgetPasswordClick(email);
                    } else {
                        communicator.toast("No Internet Connection", false);
                    }
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();

        });
    }

    /**
     * Validate input data
     *
     * @return True if data is valid and False if it isn't valid
     */
    private boolean isDataValid() {
        boolean isValid = false;
        user.setEmail(edtEmailLogin.getText().toString().trim());
        user.setPassword(edtPasswordLogin.getText().toString().trim());
        if (user.getEmail().equals("")) {
            edtEmailLogin.setError("Please Enter Your Email");
        } else if (user.getPassword().equals("")) {
            edtPasswordLogin.setError("Please Enter Your Password");
        } else {
            isValid = true;
        }
        return isValid;
    }

}
