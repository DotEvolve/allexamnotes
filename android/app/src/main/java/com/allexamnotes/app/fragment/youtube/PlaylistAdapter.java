package com.allexamnotes.app.fragment.youtube;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnItemClickListener;
import com.allexamnotes.app.viewholders.LoadingViewHolder;
import com.allexamnotes.app.viewholders.MyNativeAdView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.allexamnotes.libdroid.model.LoadMoreViewClass;
import com.allexamnotes.libdroid.model.playlist.ItemsItem;
import com.mikepenz.iconics.view.IconicsTextView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int AD_ITEM = 1;
    private static final int PLAYLIST_ITEM = 2;
    private static final int LOADING_ITEM = 3;
    private static LoadMoreViewClass loadMoreViewClass = LoadMoreViewClass.newInstance();
    private OnItemClickListener onItemClickListener;

    private List<Object> playlists = new ArrayList<>();
    private Context context;

    public PlaylistAdapter(Context context,OnItemClickListener clickListener){
        this.context = context;
        this.onItemClickListener = clickListener;
    }

    public void addObjects(List<ItemsItem> tempList){
        int temp = playlists.size();
        playlists.addAll(tempList);
        //notifyItemRangeInserted(temp,tempList.size());
        notifyDataSetChanged();
        addLoadingItem();
    }

    public void insertAd(UnifiedNativeAd nativeAd){
        playlists.add(nativeAd);
        notifyItemInserted(playlists.size());
    }

    public void addLoadingItem(){
        playlists.remove(loadMoreViewClass);
        playlists.add(loadMoreViewClass);
        //notifyItemInserted(playlists.size());
        notifyDataSetChanged();
    }

    public void removeLoadingItem(){
        playlists.remove(loadMoreViewClass);
        notifyItemRemoved(playlists.size()-1);
    }

    public void clear(){
        playlists.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = playlists.get(position);
        if (obj instanceof UnifiedNativeAd){
            return AD_ITEM;
        }else if(obj instanceof ItemsItem){
            return PLAYLIST_ITEM;
        }else if(obj instanceof LoadMoreViewClass){
            return LOADING_ITEM;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==AD_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admob_ad_item,parent,false);
            return new MyNativeAdView(view);
        }else if (viewType==PLAYLIST_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_picture,parent,false);
            return new BigPictureViewHolder(view,onItemClickListener);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_view_item,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemView = getItemViewType(position);
        if (PLAYLIST_ITEM==itemView){
            BigPictureViewHolder viewHolder = (BigPictureViewHolder) holder;
            ItemsItem item = (ItemsItem) playlists.get(position);
            try {
                Glide.with(context)
                        .load(item.getSnippet().getThumbnails().getHigh().getUrl())
                        .into(viewHolder.imageView);
                viewHolder.titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                viewHolder.titleView.setText(item.getSnippet().getTitle());
                viewHolder.postMetaView.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (AD_ITEM==itemView){


        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class BigPictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleView;
        private IconicsTextView postMetaView;
        private ImageView imageView;
        private CardView imageViewContainer;
        private View border;
        private LinearLayout linearLayout;
        private OnItemClickListener clickListener;

        public BigPictureViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            titleView = itemView.findViewById(R.id.post_title);
            imageView = itemView.findViewById(R.id.post_image);
            postMetaView = itemView.findViewById(R.id.post_meta);
            imageViewContainer = itemView.findViewById(R.id.post_image_container);
            border = itemView.findViewById(R.id.vl2_border);
            linearLayout = itemView.findViewById(R.id.linearBigPicture);
            this.clickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getAdapterPosition());
        }

    }
}
