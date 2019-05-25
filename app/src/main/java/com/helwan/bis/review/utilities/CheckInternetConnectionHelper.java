package com.helwan.bis.review.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnectionHelper {
    private static CheckInternetConnectionHelper instance;
    private boolean check;

    private CheckInternetConnectionHelper(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                check = true;
            }
        } else {
            check = false;
        }
    }

    public static CheckInternetConnectionHelper getInstance(Context context) {
        return new CheckInternetConnectionHelper(context);
    }

    public Boolean checkInternet() {
        return check;
    }

}