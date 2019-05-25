package com.helwan.bis.review.screens.brandsscreen;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.screens.brandsscreen.adapters.BrandsRecyclerAdapter;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.ProgressBarHelper;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BrandsActivity extends AppCompatActivity implements BrandsContract.View {

    private static final String TAG = "BrandsActivity";
    private List<Brand> brandList = new ArrayList<>();
    private BrandsRecyclerAdapter brandsRecyclerAdapter;
    private LinearLayout layoutProgressBar;
    private CardView cardviewNoInternet;
    private Button btnTryAgain;
    private BrandsContract.Presenter presenter;
    private String mainCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);

        mainCategory = getIntent().getStringExtra("mainCategory");

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
        toolbar.setTitle(mainCategory);
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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        brandsRecyclerAdapter = new BrandsRecyclerAdapter(brandList, BrandsActivity.this, mainCategory);
        recyclerView.setAdapter(brandsRecyclerAdapter);
        presenter = new BrandsPresenterImpl(this);
    }


    /**
     * Check internet connection
     * And show no internet connection message with button try again
     */
    private void checkInternet() {
        if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
            getAllBrands();
        } else {
            setCardviewNoInternetVisibility(View.VISIBLE);
            ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
        }

        btnTryAgain.setOnClickListener(view -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                getAllBrands();
            } else {
                FancyToast.makeText(this,"No Internet Connection",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            }
        });
    }

    private void getAllBrands() {
        setCardviewNoInternetVisibility(View.GONE);
        ProgressBarHelper.getInstance().showProgressBar(layoutProgressBar);
        presenter.getBrands(mainCategory);
    }

    private void setCardviewNoInternetVisibility(int state) {
        if (state == View.VISIBLE && cardviewNoInternet.getVisibility() == View.GONE) {
            cardviewNoInternet.setVisibility(state);
        } else if (state == View.GONE && cardviewNoInternet.getVisibility() == View.VISIBLE) {
            cardviewNoInternet.setVisibility(state);
        }
    }

    @Override
    public void setBrandsList(List<Brand> brands) {
        this.brandList.clear();
        this.brandList.addAll(brands);
        brandsRecyclerAdapter.notifyDataSetChanged();
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
