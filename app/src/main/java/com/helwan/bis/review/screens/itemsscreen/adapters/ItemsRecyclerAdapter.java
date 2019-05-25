package com.helwan.bis.review.screens.itemsscreen.adapters;

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
import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.screens.itemdetailsscreen.ItemDetailsActivity;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;
    private String mainCategory;
    private String brandName;

    public ItemsRecyclerAdapter(List<Item> items, Context context, String mainCategory, String brandName) {
        this.items = items;
        this.context = context;
        this.mainCategory = mainCategory;
        this.brandName = brandName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_item_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Item item = items.get(position);
        float rate = item.getRateSum() / item.getRateCount();
        holder.txtItemName.setText(item.getName());
        holder.txtItemDesc.setText(item.getDescription());
        holder.txtItemPrice.setText(item.getPrice());
        holder.ratingBarItem.setRating(rate);
        Glide.with(context).load(item.getImages().get("image1")).into(holder.imgItem);

        holder.cardviewItems.setOnClickListener(view -> {
            Intent intent = new Intent(context, ItemDetailsActivity.class);
            intent.putExtra("mainCategory", mainCategory);
            intent.putExtra("brandName", brandName);
            intent.putExtra("itemName", item.getName());
            intent.putExtra("itemDesc", item.getDescription());
            intent.putExtra("itemPrice", item.getPrice());
            intent.putExtra("itemRate", rate);
            intent.putExtra("itemRateSum", item.getRateSum());
            intent.putExtra("itemRateCount", item.getRateCount());
            intent.putExtra("itemImages", item.getImages());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgItem;
        private TextView txtItemName, txtItemDesc, txtItemPrice;
        private ScaleRatingBar ratingBarItem;
        private CardView cardviewItems;

        private ViewHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            txtItemName = itemView.findViewById(R.id.txtUsername);
            txtItemDesc = itemView.findViewById(R.id.txtComment);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            ratingBarItem = itemView.findViewById(R.id.ratingBarItem);
            cardviewItems = itemView.findViewById(R.id.cardviewItems);
        }
    }

}

