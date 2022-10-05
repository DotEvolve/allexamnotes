package com.allexamnotes.app.fragment.relatedpost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnItemClickListener;
import com.allexamnotes.app.others.Utils;
import com.bumptech.glide.Glide;
import com.allexamnotes.libdroid.model.posts.Posts;
import com.mikepenz.iconics.view.IconicsTextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class RelatedPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Object> objectList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public RelatedPostAdapter(Context context,OnItemClickListener clickListener) {
        this.context = context;
        this.onItemClickListener = clickListener;
    }

    public void addItems(List<Posts> objects){
        objectList.addAll(objects);
        notifyItemRangeInserted(0,objects.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_post_list,parent,false);
        return new SimpleItemViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Posts post = (Posts) objectList.get(position);
        SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
        viewHolder.title.setText(Jsoup.parse(post.getTitle()).text());
        //Adjust the height of the image in grid mode to 120dp
        if (post.getFeaturedImg()!=null) {
            Glide.with(context)
                    .load(post.getFeaturedImg())
                    .into(viewHolder.imageView);
        }
        String meta = "";
        if (post.getModified()!=null)
            meta += "  {faw-calendar-day} " + Utils.parseDate(post.getModified());

        meta+= "  {faw-comments} "+post.getCommentCount();

        meta+= "  {faw-eye} "+post.getPostViews();

        viewHolder.postMetaView.setText(meta.trim());
    }

    @Override
    public int getItemCount() {
        return objectList!=null ? objectList.size() : 0;
    }

    private static class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView imageView;
        IconicsTextView postMetaView;
        CardView imageViewContainer;
        OnItemClickListener onPostListener;

        SimpleItemViewHolder(@NonNull View itemView, OnItemClickListener postListener) {
            super(itemView);
            title = itemView.findViewById(R.id.post_title);
            imageView = itemView.findViewById(R.id.post_image);
            postMetaView = itemView.findViewById(R.id.post_meta);
            imageViewContainer = itemView.findViewById(R.id.post_image_container);
            onPostListener = postListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                onPostListener.onClick(getAdapterPosition());
            }catch (Exception ignored){

            }
        }
    }
}
