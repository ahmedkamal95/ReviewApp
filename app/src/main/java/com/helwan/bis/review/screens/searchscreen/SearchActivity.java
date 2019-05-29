package com.helwan.bis.review.screens.searchscreen;

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
import android.widget.TextView;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.Search;
import com.helwan.bis.review.screens.searchscreen.adapters.SearchRecyclerAdapter;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.ProgressBarHelper;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    private static final String TAG = "SearchActivity";
    private List<Search> searchList = new ArrayList<>();
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private LinearLayout layoutProgressBar;
    private CardView cardviewNoInternet;
    private Button btnTryAgain;
    private TextView txtNoResult;
    private SearchContract.Presenter presenter;
    private String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        itemName = getIntent().getStringExtra("itemName");

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
        toolbar.setTitle(itemName);
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
        txtNoResult = findViewById(R.id.txtNoResult);
        txtNoResult.setVisibility(View.GONE);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchRecyclerAdapter = new SearchRecyclerAdapter(searchList, SearchActivity.this);
        recyclerView.setAdapter(searchRecyclerAdapter);
        presenter = new SearchPresenterImpl(this);
    }

    /**
     * Check internet connection
     * And show no internet connection message with button try again
     */
    private void checkInternet() {
        if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
            getAllItems();
        } else {
            setCardviewNoInternetVisibility(View.VISIBLE);
            ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
        }

        btnTryAgain.setOnClickListener(view -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                getAllItems();
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        });
    }

    private void getAllItems() {
        setCardviewNoInternetVisibility(View.GONE);
        ProgressBarHelper.getInstance().showProgressBar(layoutProgressBar);
        presenter.getSearchList(itemName);
    }

    private void setCardviewNoInternetVisibility(int state) {
        if (state == View.VISIBLE && cardviewNoInternet.getVisibility() == View.GONE) {
            cardviewNoInternet.setVisibility(state);
        } else if (state == View.GONE && cardviewNoInternet.getVisibility() == View.VISIBLE) {
            cardviewNoInternet.setVisibility(state);
        }
    }

    @Override
    public void setSearchList(List<Search> searchList) {
        this.searchList.clear();
        this.searchList.addAll(searchList);
        if (searchList.isEmpty()) {
            txtNoResult.setVisibility(View.VISIBLE);
        }
        searchRecyclerAdapter.notifyDataSetChanged();
        ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
    }

    @Override
    public void toast(String message, String type) {
        if (type.equals("Error")) {
            FancyToast.makeText(this, message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        } else if (type.equals("Success")) {
            FancyToast.makeText(this, message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
        }
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
