package com.helwan.bis.review.model.networkservices;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.model.database.FirebaseServices;
import com.helwan.bis.review.screens.loginscreen.LoginContract;

import java.util.Objects;

public class LoginServices implements LoginServicesInterface {

    private static final String TAG = "LoginServices";
    private FirebaseAuth mAuth;
    private LoginContract.Presenter loginPresenter;
    private FirebaseServices firebaseServices;

    public LoginServices(LoginContract.Presenter loginPresenter) {
        this.loginPresenter = loginPresenter;
        mAuth = FirebaseAuth.getInstance();
        firebaseServices = FirebaseServices.getInstance();
    }

    @Override
    public void createUserWithEmailAndPassword(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        if (mAuth.getCurrentUser() != null) {
                            firebaseServices.setUser(mAuth.getCurrentUser());
                            firebaseServices.insertUser(user);
                            addUserNameToUser(mAuth.getCurrentUser(), user.getFirstName() + " " + user.getLastName());
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        loginPresenter.toast(Objects.requireNonNull(task.getException()).getMessage(), false);
                    }
                });
    }

    private void addUserNameToUser(FirebaseUser user, String userName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                        loginPresenter.login(mAuth.getCurrentUser());
                    }
                });
    }

    @Override
    public void signInWithEmailAndPassword(User user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        firebaseServices.setUser(mAuth.getCurrentUser());
                        loginPresenter.login(mAuth.getCurrentUser());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        loginPresenter.toast(Objects.requireNonNull(task.getException()).getMessage(), false);
                    }
                });
    }

    @Override
    public FirebaseUser isLoggedIn() {
        firebaseServices.setUser(mAuth.getCurrentUser());
        return mAuth.getCurrentUser();
    }

    @Override
    public void forgetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                        loginPresenter.toast("Reset password instructions was sent", true);
                    } else {
                        Log.w(TAG, "forgetPassword:failure", task.getException());
                        loginPresenter.toast(Objects.requireNonNull(task.getException()).getMessage(), false);
                    }
                });
    }

}
