package com.itsanubhav.wordroid4.fragment.saved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.listeners.OnHomePageItemClickListener;
import com.itsanubhav.wordroid4.viewholders.BigPictureViewHolder;
import com.bumptech.glide.Glide;
import com.itsanubhav.libdroid.model.post.Post;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class SavedPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Post> postList = new ArrayList<>();
    private Context context;
    private OnHomePageItemClickListener clickListener;

    public SavedPostAdapter(Context context, OnHomePageItemClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void addPosts(List<Post> posts){
        postList.clear();
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_picture,parent,false);
        return new BigPictureViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BigPictureViewHolder viewHolder = (BigPictureViewHolder) holder;
        Post post = postList.get(position);
        viewHolder.titleView.setText(Jsoup.parse(post.getTitle().getRendered()).text());
        Glide.with(context)
                .load(post.getFeaturedImgUrl())
                .into(viewHolder.imageView);
        String meta = "";
        if (post.getAuthorName()!=null)
            meta = "{faw-user-edit} "+post.getAuthorName();
        if (post.getModified()!=null)
            meta += "  {faw-calendar-day} " + post.getModified();

        meta+= "  {faw-comments} "+post.getCommentCount();

        viewHolder.postMetaView.setText(meta);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
