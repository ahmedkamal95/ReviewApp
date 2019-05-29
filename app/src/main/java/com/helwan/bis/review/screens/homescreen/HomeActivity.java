package com.helwan.bis.review.screens.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.screens.additemscreen.AddItemActivity;
import com.helwan.bis.review.screens.homescreen.adapters.HomeRecyclerAdapter;
import com.helwan.bis.review.screens.loginscreen.LoginActivity;
import com.helwan.bis.review.screens.searchscreen.SearchActivity;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.ProgressBarHelper;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, SearchView.OnQueryTextListener {

    private static final String TAG = "HomeActivity";
    private List<MainCategoryItem> mainCategoryItemList = new ArrayList<>();
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private LinearLayout layoutProgressBar;
    private CardView cardviewNoInternet;
    private Button btnTryAgain;
    private HomeContract.Presenter presenter;
    private long time;
    private SearchView searchView;

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
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        findViewById(R.id.imgLogo).setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
    }

    /**
     * Initialize views
     */
    private void initViews() {
        layoutProgressBar = findViewById(R.id.layoutProgressBar);
        cardviewNoInternet = findViewById(R.id.cardviewNoInternet);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        homeRecyclerAdapter = new HomeRecyclerAdapter(mainCategoryItemList, HomeActivity.this);
        recyclerView.setAdapter(homeRecyclerAdapter);
        presenter = new HomePresenterImpl(this);

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                startActivity(new Intent(HomeActivity.this, AddItemActivity.class));
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        });

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
        homeRecyclerAdapter.notifyDataSetChanged();
        ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_activity_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Product Name");
        searchView.setFocusableInTouchMode(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuBtnLogout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String search) {
        search = search.trim();
        if (search.equals("")) {
            FancyToast.makeText(HomeActivity.this, "Please Enter Product Name", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        } else {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("itemName", search);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String search) {
        return false;
    }

    @Override
    public void toast(String message, String type) {
        if (type.equals("Error")) {
            FancyToast.makeText(this, message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        } else if (type.equals("Success")) {
            FancyToast.makeText(this, message, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
        }
    }

    /**
     * Handle Backpressed to close app
     */
    @Override
    public void onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
            } else {
                FancyToast.makeText(this, "Press back again to close", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                time = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
