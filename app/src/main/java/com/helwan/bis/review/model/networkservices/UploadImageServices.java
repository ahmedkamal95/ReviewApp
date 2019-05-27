package com.helwan.bis.review.model.networkservices;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.helwan.bis.review.screens.additemscreen.AddItemContract;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UploadImageServices implements UploadImageServicesInterface {

    private AddItemContract.View view;
    private AddItemContract.Presenter presenter;

    public UploadImageServices(AddItemContract.View view, AddItemContract.Presenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void uploadImage(List<Uri> imagesUri, String brandName, String itemName) {
        AtomicBoolean finish = new AtomicBoolean(false);
        final int[] count = {1};
        for (int i = 1; i <= imagesUri.size(); i++) {
            StorageReference mStorageRef = FirebaseStorage.getInstance()
                    .getReference(brandName + "Brand/" + itemName + "Item");

            StorageReference fileReference = mStorageRef
                    .child(itemName + "_" + i + "." + getFileExtension(imagesUri.get(i - 1)));

            int finalI = i;
            fileReference.putFile(imagesUri.get(i - 1))
                    .addOnSuccessListener(taskSnapshot ->
                            fileReference.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        if (count[0] == imagesUri.size()) {
                                            finish.set(true);
                                        }
                                        setImageUrl(uri.toString(), "image" + finalI, finish.get());
                                        count[0]++;
                                    }));
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = ((Activity) view).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setImageUrl(String downloadUrl, String imageName, boolean finish) {
        presenter.setImageUrl(downloadUrl, imageName, finish);
    }

    @Override
    public void onDestroy() {
        view = null;
        presenter = null;
    }


}
