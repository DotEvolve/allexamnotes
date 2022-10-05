package com.allexamnotes.app.fragment.tags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnItemClickListener;
import com.allexamnotes.app.viewholders.LoadingViewHolder;
import com.allexamnotes.libdroid.model.LoadMoreViewClass;
import com.allexamnotes.libdroid.model.tag.Tag;
import com.mikepenz.iconics.view.IconicsTextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TAG_ITEM = 1;
    public static final int LOAIDNG_ITEM = 2;

    private List<Object> tagsList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private static LoadMoreViewClass loadingItem = LoadMoreViewClass.newInstance();

    public TagsAdapter(Context mContext,OnItemClickListener listener) {
        this.mContext = mContext;
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position<tagsList.size()) {
            Object obj = tagsList.get(position);
            if (obj instanceof Tag) {
                return TAG_ITEM;
            } else {
                return LOAIDNG_ITEM;
            }
        }else {
            return LOAIDNG_ITEM;
        }
    }

    public void addItems(List<Tag> objects){
        int tempPos = tagsList.size();
        tagsList.addAll(objects);
        addLoadingView();
        notifyItemInserted(tempPos);
    }

    public void clearItems(){
        tagsList.clear();
        notifyDataSetChanged();
    }

    public void addLoadingView(){
        tagsList.remove(loadingItem);
        tagsList.add(loadingItem);
        notifyItemInserted(tagsList.size()-1);
    }

    public void removeLoaidngView(){
        tagsList.remove(loadingItem);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TAG_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_layout,parent,false);
            return new TagViewHolder(view,onItemClickListener);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_view_item,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemType = holder.getItemViewType();
        if (itemType==TAG_ITEM){
            TagViewHolder tagViewHolder = (TagViewHolder) holder;
            Tag tag = (Tag) tagsList.get(position);
            String temp = "{faw-hashtag}"+Jsoup.parse(tag.getName()).text();
            tagViewHolder.iconicsTextView.setText(temp);
        }
    }

    @Override
    public int getItemCount() {
        return tagsList!=null ? tagsList.size() : 0;
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IconicsTextView iconicsTextView;
        OnItemClickListener listener;

        public TagViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            iconicsTextView = itemView.findViewById(R.id.tag_title);
            this.listener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }
    }
}
