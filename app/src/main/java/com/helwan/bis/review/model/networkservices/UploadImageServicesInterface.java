package com.helwan.bis.review.model.networkservices;

import android.net.Uri;

import java.util.List;

public interface UploadImageServicesInterface {
    void uploadImage(List<Uri> imagesUri, String brandName, String itemName);

    void onDestroy();
}
