package com.helwan.bis.review.screens.additemscreen;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.screens.additemscreen.adapters.MainCategoryRecyclerAdapter;
import com.helwan.bis.review.screens.homescreen.HomePresenterImpl;
import com.helwan.bis.review.screens.homescreen.adapters.HomeRecyclerAdapter;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.ProgressBarHelper;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity implements AddItemContract.View {

    private static final String TAG = "AddItemActivity";
    private List<MainCategoryItem> mainCategoryItemList = new ArrayList<>();
    private MainCategoryRecyclerAdapter mainCategoryRecyclerAdapter;
    private LinearLayout layoutProgressBar;
    private CardView cardviewNoInternet;
    private Button btnTryAgain;
    private AddItemContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupToolbar();
        initViews();
        checkInternet();
    }

    /**
     * Setting Toolbar
     */
    private void setupToolbar() {
        Log.d(TAG, "setupToolbar: Setting Toolbar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Product");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        layoutProgressBar = findViewById(R.id.layoutProgressBar);
        cardviewNoInternet = findViewById(R.id.cardviewNoInternet);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddItemActivity.this));
        mainCategoryRecyclerAdapter = new MainCategoryRecyclerAdapter(mainCategoryItemList, AddItemActivity.this);
        recyclerView.setAdapter(mainCategoryRecyclerAdapter);
        presenter = new AddItemPresenterImpl(this);
    }

    /**
     * Check internet connection
     * And show no internet connection message with button try again
     */
    private void checkInternet() {
        if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
            getAllCatergory();
        } else {
            setCardviewNoInternetVisibility(View.VISIBLE);
            ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);

        }

        btnTryAgain.setOnClickListener(view -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                getAllCatergory();
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        });
    }

    private void getAllCatergory() {
        setCardviewNoInternetVisibility(View.GONE);
        ProgressBarHelper.getInstance().showProgressBar(layoutProgressBar);
        presenter.getMainCategory();
    }

    private void setCardviewNoInternetVisibility(int state) {
        if (state == View.VISIBLE && cardviewNoInternet.getVisibility() == View.GONE) {
            cardviewNoInternet.setVisibility(state);
        } else if (state == View.GONE && cardviewNoInternet.getVisibility() == View.VISIBLE) {
            cardviewNoInternet.setVisibility(state);
        }
    }

    @Override
    public void setMainCategoryList(List<MainCategoryItem> mainCategoryList) {
        this.mainCategoryItemList.clear();
        this.mainCategoryItemList.addAll(mainCategoryList);
        mainCategoryRecyclerAdapter.notifyDataSetChanged();
        ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
