package com.allexamnotes.app.fragment.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.R;
import com.bumptech.glide.Glide;
import com.allexamnotes.libdroid.model.LoadMoreViewClass;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MEDIA_ITEM = 1;
    private static final int LOADING_ITEM = 2;

    private static LoadMoreViewClass loadingItem = LoadMoreViewClass.newInstance();

    private List<String> images;
    private Context context;

    public MediaAdapter(Context context) {
        this.context = context;
    }

    public MediaAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media_item,parent,false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;
        Glide.with(context).load(images.get(position)).into(mediaViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
