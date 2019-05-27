package com.helwan.bis.review.screens.additemscreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import android.widget.LinearLayout;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.screens.additemscreen.adapters.ImagesRecyclerAdapter;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.ProgressBarHelper;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class AddItemActivity extends AppCompatActivity implements AddItemContract.View {

    private static final String TAG = "AddItemActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    private HashMap<String, String> images = new HashMap<>();
    private List<Uri> imagesUri = new ArrayList<>();
    private List<String> imagesKey = new ArrayList<>(images.keySet());
    private List<Brand> brands = new ArrayList<>();
    private ImagesRecyclerAdapter imagesRecyclerAdapter;
    private LinearLayout layoutProgressBar, linearLayoutContainer;
    private CardView cardviewNoInternet, cardviewImages;
    private Button btnTryAgain;
    private EditText edtItemName, edtItemPrice, edtItemDesc;
    private NiceSpinner spinnerMainCategory, spinnerBrand;
    private AddItemContract.Presenter presenter;
    private int index = 0;
    private String mainCategoryName, brandName;
    private int itemCount;
    private Item item;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

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
        linearLayoutContainer = findViewById(R.id.linearLayoutContainer);
        layoutProgressBar = findViewById(R.id.layoutProgressBar);
        cardviewNoInternet = findViewById(R.id.cardviewNoInternet);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        cardviewImages = findViewById(R.id.cardviewImages);
        edtItemName = findViewById(R.id.edtItemName);
        edtItemPrice = findViewById(R.id.edtItemPrice);
        edtItemDesc = findViewById(R.id.edtItemDesc);
        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        RecyclerView recyclerViewImage = findViewById(R.id.recyclerViewImage);
        recyclerViewImage.setLayoutManager(new LinearLayoutManager(AddItemActivity.this));
        imagesRecyclerAdapter = new ImagesRecyclerAdapter(imagesKey, AddItemActivity.this);
        recyclerViewImage.setAdapter(imagesRecyclerAdapter);
        presenter = new AddItemPresenterImpl(this);

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(view -> addProduct());

        Button btnSelectPhotos = findViewById(R.id.btnSelectPhotos);
        btnSelectPhotos.setOnClickListener(view -> {
            openFileChooser();
        });

        spinnerMainCategory = findViewById(R.id.spinnerMainCategory);
        spinnerMainCategory.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                mainCategoryName = parent.getItemAtPosition(position).toString();
                presenter.getBrands(mainCategoryName);
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        });

        spinnerBrand = findViewById(R.id.spinnerBrand);
        spinnerBrand.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            brandName = parent.getItemAtPosition(position).toString();
            for (Brand brand : brands) {
                if (brand.getName().equals(brandName)) {
                    itemCount = brand.getItemCount();
                }
            }
        });
    }

    private void addProduct() {
        item = new Item();
        item.setName(edtItemName.getText().toString().trim());
        item.setPrice(edtItemPrice.getText().toString().trim());
        item.setDescription(edtItemDesc.getText().toString().trim());
        if (imagesKey.isEmpty()) {
            FancyToast.makeText(this, "Please Select Photos", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        } else if (item.getName().equals("")) {
            edtItemName.setError("Please Product Name");
        } else if (item.getPrice().equals("")) {
            edtItemPrice.setError("Please Product Price");
        } else if (item.getDescription().equals("")) {
            edtItemDesc.setError("Please Product Description");
        } else if (mainCategoryName == null) {
            FancyToast.makeText(this, "Please Select Catrgory", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        } else if (brandName == null) {
            FancyToast.makeText(this, "Please Select Brand", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        } else {
            if (CheckInternetConnectionHelper.getInstance(this).checkInternet()) {
                alertDialog.show();
                item.setRateCount(0.0f);
                item.setRateSum(0.0f);
                presenter.addProduct(item, mainCategoryName, brandName, itemCount);
            } else {
                FancyToast.makeText(this, "No Internet Connection", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            index++;
            Uri imageUri = data.getData();
            imagesUri.add(imageUri);
            imagesKey.add("image" + index);
            imagesRecyclerAdapter.notifyDataSetChanged();
            if (cardviewImages.getVisibility() == View.GONE) {
                cardviewImages.setVisibility(View.VISIBLE);
            }

        }
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
        List<String> mainCategoryNameList = new ArrayList<>();
        for (MainCategoryItem mainCategoryItem : mainCategoryList) {
            mainCategoryNameList.add(mainCategoryItem.getName());
        }
        spinnerMainCategory.attachDataSource(mainCategoryNameList);
        mainCategoryName = mainCategoryNameList.get(0);
        presenter.getBrands(mainCategoryName);
    }

    @Override
    public void setBrandsList(List<Brand> brands) {
        this.brands.addAll(brands);
        if (spinnerBrand.getVisibility() == View.VISIBLE) {
            spinnerBrand.setVisibility(View.GONE);
        }
        List<String> brandNameList = new ArrayList<>();
        for (Brand brandItem : brands) {
            brandNameList.add(brandItem.getName());
        }
        spinnerBrand.attachDataSource(brandNameList);
        brandName = brandNameList.get(0);
        itemCount = brands.get(0).getItemCount();
        ProgressBarHelper.getInstance().hideProgressBar(layoutProgressBar);
        spinnerBrand.setVisibility(View.VISIBLE);
        if (linearLayoutContainer.getVisibility() == View.GONE) {
            linearLayoutContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setImageUrl(String downloadUrl, String imageName, boolean finish) {
        images.put(imageName, downloadUrl);
        if (finish) {
            presenter.addImage(mainCategoryName, brandName, item.getName(), images);
        }
    }

    @Override
    public void uploadFinish() {
        alertDialog.dismiss();
        FancyToast.makeText(this, "Product Added", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
        finish();
    }

    @Override
    public void itemExists(boolean isExist) {
        if (isExist) {
            alertDialog.dismiss();
            FancyToast.makeText(this, "Product Already Exists", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        } else {
            presenter.uploadImage(imagesUri, brandName, item.getName());
        }
    }

    public void removeImage(String imageKey, int position) {
        imagesKey.remove(imageKey);
        imagesUri.remove(position);
        index--;
        if (imagesKey.isEmpty()) {
            cardviewImages.setVisibility(View.GONE);
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
