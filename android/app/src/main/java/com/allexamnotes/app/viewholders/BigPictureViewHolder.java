package com.allexamnotes.app.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnHomePageItemClickListener;
import com.mikepenz.iconics.view.IconicsTextView;

public class BigPictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView titleView;
    public IconicsTextView postMetaView;
    public ImageView imageView;
    public CardView imageViewContainer;
    public View border;
    public LinearLayout linearLayout;
    private OnHomePageItemClickListener clickListener;

    public BigPictureViewHolder(@NonNull View itemView, OnHomePageItemClickListener listener) {
        super(itemView);
        titleView = itemView.findViewById(R.id.post_title);
        imageView = itemView.findViewById(R.id.post_image);
        postMetaView = itemView.findViewById(R.id.post_meta);
        imageViewContainer = itemView.findViewById(R.id.post_image_container);
        border = itemView.findViewById(R.id.vl2_border);
        linearLayout = itemView.findViewById(R.id.linearBigPicture);
        this.clickListener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        clickListener.onClick(getAdapterPosition(),"post");
    }

    @Override
    public boolean onLongClick(View view) {
        clickListener.onLongClick(getAdapterPosition(),"post");
        return true;
    }
}
