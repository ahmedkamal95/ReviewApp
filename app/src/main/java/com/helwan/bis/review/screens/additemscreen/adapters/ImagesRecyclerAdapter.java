package com.helwan.bis.review.screens.additemscreen.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helwan.bis.review.R;
import com.helwan.bis.review.screens.additemscreen.AddItemActivity;

import java.util.List;

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.ViewHolder> {

    private AddItemActivity activity;
    private List<String> images;

    public ImagesRecyclerAdapter(List<String> images, AddItemActivity activity) {
        this.images = images;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String image = images.get(position);
        holder.txtImage.setText(image);

        holder.txtImage.setOnClickListener(view -> {
            images.remove(position);
            activity.removeImage(image,position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtImage;

        private ViewHolder(View itemView) {
            super(itemView);
            txtImage = itemView.findViewById(R.id.txtImage);
        }
    }

}

