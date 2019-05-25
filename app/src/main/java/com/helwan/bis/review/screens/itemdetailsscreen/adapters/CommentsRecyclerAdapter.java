package com.helwan.bis.review.screens.itemdetailsscreen.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.Comment;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentsRecyclerAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comments_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Comment comment = comments.get(position);
        holder.txtItemName.setText(comment.getUserName());
        holder.txtItemDesc.setText(comment.getComment());
        holder.ratingBarUserRate.setRating(comment.getUserRate());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtItemName, txtItemDesc;
        private ScaleRatingBar ratingBarUserRate;

        private ViewHolder(View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtUsername);
            txtItemDesc = itemView.findViewById(R.id.txtComment);
            ratingBarUserRate = itemView.findViewById(R.id.ratingBarUserRate);
        }
    }

}

