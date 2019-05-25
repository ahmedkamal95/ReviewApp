package com.helwan.bis.review.screens.homescreen.adapters;

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
import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.screens.brandsscreen.BrandsActivity;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<MainCategoryItem> mainCategoryItems;

    public HomeRecyclerAdapter(List<MainCategoryItem> mainCategoryItems, Context context) {
        this.mainCategoryItems = mainCategoryItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_maincategory_item_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MainCategoryItem mainCategoryItem = mainCategoryItems.get(position);
        holder.txtCategoryName.setText(mainCategoryItem.getName());
        holder.txtBrandCount.setText(mainCategoryItem.getBrandCount() + " Items");
        Glide.with(context).load(mainCategoryItem.getImage()).into(holder.imgBackground);

        holder.cardviewMainCategory.setOnClickListener(view -> {
            Intent intent = new Intent(context, BrandsActivity.class);
            intent.putExtra("mainCategory", mainCategoryItem.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mainCategoryItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBackground;
        private TextView txtCategoryName, txtBrandCount;
        private CardView cardviewMainCategory;

        private ViewHolder(View itemView) {
            super(itemView);
            imgBackground = itemView.findViewById(R.id.imgBackground);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtBrandCount = itemView.findViewById(R.id.txtBrandCount);
            cardviewMainCategory = itemView.findViewById(R.id.cardviewMainCategory);
        }
    }

}

