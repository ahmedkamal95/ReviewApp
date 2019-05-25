package com.helwan.bis.review.screens.brandsscreen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.screens.itemsscreen.ItemsActivity;

import java.util.List;

public class BrandsRecyclerAdapter extends RecyclerView.Adapter<BrandsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Brand> brands;
    private String mainCategory;

    public BrandsRecyclerAdapter(List<Brand> brands, Context context, String mainCategory) {
        this.brands = brands;
        this.context = context;
        this.mainCategory = mainCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brands_item_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Brand brandItem = brands.get(position);
        holder.txtBrandName.setText(brandItem.getName());
        holder.txtItemCount.setText(brandItem.getItemCount() + " Items");
        Glide.with(context).load(brandItem.getImage()).into(holder.imgBrand);

        holder.cardviewBrands.setOnClickListener(view -> {
            Intent intent = new Intent(context, ItemsActivity.class);
            intent.putExtra("mainCategory", mainCategory);
            intent.putExtra("brandName", brandItem.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBrand;
        private TextView txtBrandName, txtItemCount;
        private CardView cardviewBrands;

        private ViewHolder(View itemView) {
            super(itemView);
            imgBrand = itemView.findViewById(R.id.imgBrand);
            txtBrandName = itemView.findViewById(R.id.txtBrandName);
            txtItemCount = itemView.findViewById(R.id.txtItemCount);
            cardviewBrands = itemView.findViewById(R.id.cardviewBrands);
        }
    }

}

