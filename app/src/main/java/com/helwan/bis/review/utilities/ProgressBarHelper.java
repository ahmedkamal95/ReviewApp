package com.helwan.bis.review.utilities;

import android.view.View;
import android.widget.LinearLayout;

public class ProgressBarHelper {
    private static ProgressBarHelper instance;

    private ProgressBarHelper() {
    }

    public static ProgressBarHelper getInstance() {
        if (instance == null) {
            instance = new ProgressBarHelper();
        }
        return instance;
    }

    /**
     * Show ProgressBar Layout
     *
     * @param layoutProgressBar object from layout
     */
    public void showProgressBar(LinearLayout layoutProgressBar) {
        if (layoutProgressBar.getVisibility() == View.GONE) {
            layoutProgressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide ProgressBar Layout
     *
     * @param layoutProgressBar object from layout
     */
    public void hideProgressBar(LinearLayout layoutProgressBar) {
        if (layoutProgressBar.getVisibility() == View.VISIBLE) {
            layoutProgressBar.setVisibility(View.GONE);
        }
    }

}
