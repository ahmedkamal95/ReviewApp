package com.helwan.bis.review.screens.itemdetailsscreen;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.Comment;
import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.screens.itemdetailsscreen.adapters.CommentsRecyclerAdapter;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.ProgressBarHelper;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ItemDetailsActivity extends AppCompatActivity implements ItemDetailsContract.View {

    private static final String TAG = "ItemDetailsActivity";
    private Item item;
    private List<Comment> commentList = new ArrayList<>();
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private LinearLayout layoutProgressBar;
    private CardView cardviewNoInternet;
    private LinearLayout layoutItemDetails;
    private Button btnTryAgain;
    EditText edtComment;
    private SliderLayout sliderLayout;
    private ScaleRatingBar ratingBarComment;
    private ItemDetailsContract.Presenter presenter;
    private String mainCategory;
    private String brandName;
    private float rate;
    private AlertDialog alertDialog;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        item = new Item();

        mainCategory = getIntent().getStringExtra("mainCategory");
        brandName = getIntent().getStringExtra("brandName");
        rate = getIntent().getFloatExtra("itemRate", 0.0f);
        item.setName(getIntent().getStringExtra("itemName"));
        item.setDescription(getIntent().getStringExtra("itemDesc"));
        item.setPrice(getIntent().getStringExtra("itemPrice"));
        item.setRateSum(getIntent().getFloatExtra("itemRateSum", 0.0f));
        item.setRateCount(getIntent().getFloatExtra("itemRateCount", 0.0f));
        item.setImages((HashMap<String, String>) getIntent().getSerializableExtra("itemImages"));

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
        toolbar.setTitle(item.getName());
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
        layoutItemDetails = findViewById(R.id.layoutItemDetails);
        ratingBarComment = findViewById(R.id.ratingBarComment);
        edtComment = findViewById(R.id.edtComment);
        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();


        Button btnAddComment = findViewById(R.id.btnAddComment);
        btnAddComment.setOnClickListener(v -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                String commentString = edtComment.getText().toString().trim();
                if (commentString.equals("")) {
                    edtComment.setError("Please Enter Your Comment");
                } else {
                    alertDialog.show();
                    Comment comment = new Comment();
                    comment.setUserRate(0);
                    String userId = presenter.getUserId();
                    for (Comment oldComment : commentList) {
                        if (oldComment.getUserId().equals(userId)) {
                            comment.setUserId(oldComment.getUserId());
                            comment.setUserName(oldComment.getUserName());
                            comment.setUserRate(oldComment.getUserRate());
                            comment.setComment(oldComment.getComment());
                        }
                    }
                    if (comment.getUserId() != null) {
                        commentList.remove(comment);
                        commentsRecyclerAdapter.notifyDataSetChanged();
                    }
                    presenter.addComment(mainCategory, brandName, item.getName(), commentString, (int) ratingBarComment.getRating(), item.getRateCount(), item.getRateSum(), comment.getUserRate());
                }
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }

        });

        ScaleRatingBar ratingBarDesc = findViewById(R.id.ratingBarDesc);
        ratingBarDesc.setRating(rate);

        TextView txtDesc = findViewById(R.id.txtDesc);
        txtDesc.setText(item.getDescription());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ItemDetailsActivity.this));
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentList, ItemDetailsActivity.this);
        recyclerView.setAdapter(commentsRecyclerAdapter);

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(3);

        presenter = new ItemDetailsPresenterImpl(this);
    }

    private void setSliderViews() {
        List<String> images = new ArrayList<>(item.getImages().values());
        for (int i = 0; i < images.size(); i++) {
            SliderView sliderView = new DefaultSliderView(this);
            sliderView.setImageUrl(images.get(i));
            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
//            sliderView.setDescription("setDescription " + (i + 1));
//            final int finalI = i;
//            sliderView.setOnSliderClickListener(sliderView1 -> Toast.makeText(ItemDetailsActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show());

            sliderLayout.addSliderView(sliderView);
        }
    }

    /**
     * Check internet connection
     * And show no internet connection message with button try again
     */
    private void checkInternet() {
        if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
            getAllComments();
        } else {
            setCardviewNoInternetVisibility(View.VISIBLE);
            ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
            hideViews();
        }

        btnTryAgain.setOnClickListener(view -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                getAllComments();
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        });
    }

    private void getAllComments() {
        setCardviewNoInternetVisibility(View.GONE);
        ProgressBarHelper.getInstance().showProgressBar(layoutProgressBar);
        hideViews();
        presenter.getComments(mainCategory, brandName, item.getName());
    }

    private void showViews() {
        layoutItemDetails.setVisibility(View.VISIBLE);
        setSliderViews();
    }

    private void hideViews() {
        layoutItemDetails.setVisibility(View.GONE);
    }

    private void setCardviewNoInternetVisibility(int state) {
        if (state == View.VISIBLE && cardviewNoInternet.getVisibility() == View.GONE) {
            cardviewNoInternet.setVisibility(state);
        } else if (state == View.GONE && cardviewNoInternet.getVisibility() == View.VISIBLE) {
            cardviewNoInternet.setVisibility(state);
        }
    }

    @Override
    public void setCommentsList(List<Comment> comments) {
        this.commentList.clear();
        this.commentList.addAll(comments);
        commentsRecyclerAdapter.notifyDataSetChanged();
        ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
        showViews();
    }

    @Override
    public void updateComments(String msg, String comment, String displayName, String uid, Float rateSum) {
        Comment commentAdd = new Comment();
        commentAdd.setUserId(uid);
        commentAdd.setUserName(displayName);
        commentAdd.setComment(comment);
        commentAdd.setUserRate((int) ratingBarComment.getRating());
        commentList.add(commentAdd);
        commentsRecyclerAdapter.notifyDataSetChanged();
        edtComment.setText("");
        ratingBarComment.setRating(1.0f);
        item.setRateSum(rateSum);
        alertDialog.dismiss();
        FancyToast.makeText(this, msg, FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
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
