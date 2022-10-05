package com.allexamnotes.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.DetailActivity;
import com.allexamnotes.app.MainActivity;
import com.allexamnotes.app.R;
import com.allexamnotes.app.fragment.postlist.PostListFragment;
import com.allexamnotes.app.listeners.OnHomePageItemClickListener;
import com.allexamnotes.app.others.Utils;
import com.allexamnotes.app.viewholders.BigPictureViewHolder;
import com.allexamnotes.app.viewholders.LoadingViewHolder;
import com.bumptech.glide.Glide;
import com.allexamnotes.libdroid.model.WorDroidSectionItems;
import com.mikepenz.iconics.view.IconicsTextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SUB_CATEGORIES = 5;
    private static final int REC_CAROUSEL_POSTS_INNER_TITLE = 4;
    private static final int REC_CAROUSEL_POSTS_OUTER_TITLE = 3;
    private static final int GRID_POST = 2;
    private static final int AUTO_SLIDER = 4;
    private static final int LIST_POST = 6;

    private int itemType;
    private int contentType;

    private List<WorDroidSectionItems> objectList = new ArrayList<>();
    private Context mContext;
    private OnHomePageItemClickListener listener;

    public ChildAdapter(Context mContext,OnHomePageItemClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public ChildAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void addItems(List<WorDroidSectionItems> objects){
        objectList.addAll(objects);
        notifyDataSetChanged();
    }

    public void setContentType(int contentType){
        this.contentType = contentType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemViewType(int position) {
        return itemType == 0 ? 1 : itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SUB_CATEGORIES){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_taxanomy_layout,parent,false);
            return new TaxonomyViewHolder(view,listener);
        }else if (viewType== REC_CAROUSEL_POSTS_INNER_TITLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_post_carousel_item_inner_title,parent,false);
            return new RecyclerCarouselSlider(view,listener);
        }else if (viewType == REC_CAROUSEL_POSTS_OUTER_TITLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_post_carousel_outer_title,parent,false);
            return new RecyclerCarouselSliderOuterTitle(view,listener);
        }else if (viewType == GRID_POST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_picture,parent,false);
            return new BigPictureViewHolder(view,listener);
        }else if(viewType == LIST_POST){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_post_list,parent,false);
            return new SimpleItemViewHolder(view,listener);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_layout,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(holder.getAbsoluteAdapterPosition())==SUB_CATEGORIES){
            WorDroidSectionItems category =  objectList.get(holder.getAbsoluteAdapterPosition());
            Log.e("Making Request",category.toString());
            TaxonomyViewHolder taxanomyViewHolder = (TaxonomyViewHolder) holder;
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{}
            },
                    new int[]{
                            Utils.generateRandomColor()
                    });
            taxanomyViewHolder.firstLetterView.setBackgroundTintList(colorStateList);
            if(category.getTitle()!=null&&category.getTitle().length()>1) {
                taxanomyViewHolder.firstLetterView.setText(category.getTitle().substring(0, 1).toUpperCase());
                taxanomyViewHolder.taxonomyName.setText(Jsoup.parse(category.getTitle()).text());
            }
            taxanomyViewHolder.itemView.setOnClickListener(view -> {

                if (mContext instanceof ContainerActivity){
                    try {
                        ContainerActivity a = (ContainerActivity) mContext;
                        assert a != null;
                        String title = Jsoup.parse(category.getTitle()).text();
                        a.addFragment(PostListFragment.newInstance(String.valueOf(category.getId()),null,null,null),title);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if (mContext instanceof MainActivity){
                    Intent intent = new Intent(mContext, ContainerActivity.class);
                    intent.putExtra("title", Jsoup.parse(category.getTitle()).text());
                    intent.putExtra("screen", "posts");
                    if(contentType==3) {
                        intent.putExtra("category", String.valueOf(category.getId()));
                    }else if(contentType==4){
                        intent.putExtra("tags", String.valueOf(category.getId()));
                    }
                    mContext.startActivity(intent);
                }
            });
        }else if (getItemViewType(holder.getAbsoluteAdapterPosition())== REC_CAROUSEL_POSTS_INNER_TITLE){
            WorDroidSectionItems post = objectList.get(holder.getAbsoluteAdapterPosition());
            RecyclerCarouselSlider recyclerCarouselSlider = (RecyclerCarouselSlider) holder;
            recyclerCarouselSlider.sliderTitleView.setText(Jsoup.parse(post.getTitle()).text());
            recyclerCarouselSlider.sliderCategoryTitle.setText(post.getAuthorName());
            Glide.with(holder.itemView.getContext())
                    .load(post.getFeaturedImg())
                    .into(recyclerCarouselSlider.sliderImageView);
            recyclerCarouselSlider.itemView.setOnClickListener(view -> {
                /*Intent intent = new Intent(mContext, PostContainerActivity.class);
                intent.putExtra("postId", objectList.get(position).getId());
                intent.putExtra("img", objectList.get(position).getFeaturedImg());
                intent.putExtra("title", objectList.get(position).getTitle());
                mContext.startActivity(intent);*/
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("postId", objectList.get(holder.getAbsoluteAdapterPosition()).getId());
                intent.putExtra("img", objectList.get(holder.getAbsoluteAdapterPosition()).getFeaturedImg());
                intent.putExtra("title", objectList.get(holder.getAbsoluteAdapterPosition()).getTitle());
                mContext.startActivity(intent);
            });
        }else if (getItemViewType(holder.getAbsoluteAdapterPosition())== REC_CAROUSEL_POSTS_OUTER_TITLE ){
            WorDroidSectionItems post = (WorDroidSectionItems) objectList.get(holder.getAbsoluteAdapterPosition());
            RecyclerCarouselSliderOuterTitle recyclerCarouselSlider = (RecyclerCarouselSliderOuterTitle) holder;
            recyclerCarouselSlider.sliderTitleView.setText(Jsoup.parse(post.getTitle()).text());
            recyclerCarouselSlider.postMetaTextView.setText("{faw-calendar-day} "  + Utils.parseDate(post.getModified()) +"  {faw-comments} "+post.getCommentCount());
            Glide.with(holder.itemView.getContext())
                    .load(post.getFeaturedImg())
                    .into(recyclerCarouselSlider.sliderImageView);
            recyclerCarouselSlider.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent(mContext, PostContainerActivity.class);
                    intent.putExtra("postId", objectList.get(position).getId());
                    intent.putExtra("img", objectList.get(position).getFeaturedImg());
                    intent.putExtra("title", objectList.get(position).getTitle());
                    mContext.startActivity(intent);*/
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("postId", objectList.get(holder.getAbsoluteAdapterPosition()).getId());
                    intent.putExtra("img", objectList.get(holder.getAbsoluteAdapterPosition()).getFeaturedImg());
                    intent.putExtra("title", objectList.get(holder.getAbsoluteAdapterPosition()).getTitle());
                    mContext.startActivity(intent);
                }
            });
        }else if (getItemViewType(holder.getAbsoluteAdapterPosition())==GRID_POST){
            WorDroidSectionItems post = objectList.get(holder.getAbsoluteAdapterPosition());
            BigPictureViewHolder bigPictureViewHolder = (BigPictureViewHolder) holder;
            /*if(Config.FORCE_RTL){
                bigPictureViewHolder.linearLayout.setPadding(0,0,,0);
            }else {
                bigPictureViewHolder.linearLayout.setPadding(0,0,0,0);
            }*/

            bigPictureViewHolder.titleView.setText(Jsoup.parse(post.getTitle()).text());
            bigPictureViewHolder.postMetaView.setText("{faw-calendar-day} "  + Utils.parseDate(post.getModified()) +"  {faw-comments} "+post.getCommentCount());
            Glide.with(holder.itemView.getContext())
                    .load(post.getFeaturedImg())
                    .into(bigPictureViewHolder.imageView);
            bigPictureViewHolder.border.setVisibility(View.GONE);
            bigPictureViewHolder.itemView.setOnClickListener(view -> {
                /*Intent intent = new Intent(mContext, PostContainerActivity.class);
                intent.putExtra("postId", objectList.get(position).getId());
                intent.putExtra("img", objectList.get(position).getFeaturedImg());
                intent.putExtra("title", objectList.get(position).getTitle());
                mContext.startActivity(intent);*/
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("postId", objectList.get(holder.getAbsoluteAdapterPosition()).getId());
                intent.putExtra("img", objectList.get(holder.getAbsoluteAdapterPosition()).getFeaturedImg());
                intent.putExtra("title", objectList.get(holder.getAbsoluteAdapterPosition()).getTitle());
                mContext.startActivity(intent);
            });
        }else if (getItemViewType(holder.getAbsoluteAdapterPosition())==LIST_POST){
            WorDroidSectionItems post = objectList.get(holder.getAbsoluteAdapterPosition());
            SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
            viewHolder.title.setText(Jsoup.parse(post.getTitle()).text());
            if (post.getFeaturedImg()!=null) {
                Glide.with(holder.itemView.getContext())
                        .load(post.getFeaturedImg())
                        .into(viewHolder.imageView);
            }
            String meta = "";
            if (post.getModified()!=null)
                meta += "  {faw-calendar-day} " + Utils.parseDate(post.getModified());
            meta+= "  {faw-comments} "+post.getCommentCount();
            meta+= "  {faw-eye} "+post.getPostViews();
            viewHolder.postMetaView.setText(meta.trim());
            viewHolder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext,  DetailActivity.class);
                intent.putExtra("postId", objectList.get(holder.getAbsoluteAdapterPosition()).getId());
                intent.putExtra("img", objectList.get(holder.getAbsoluteAdapterPosition()).getFeaturedImg());
                intent.putExtra("title", objectList.get(holder.getAbsoluteAdapterPosition()).getTitle());
                mContext.startActivity(intent);
                /*Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("postId", objectList.get(position).getId());
                intent.putExtra("img", objectList.get(position).getFeaturedImg());
                intent.putExtra("title", objectList.get(position).getTitle());
                mContext.startActivity(intent);*/
            });
        }
    }



    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }


    private static class TaxonomyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        IconicsTextView firstLetterView;
        TextView taxonomyName;
        OnHomePageItemClickListener clickListener;

        public TaxonomyViewHolder(View view, OnHomePageItemClickListener listener) {
            super(view);
            taxonomyName = view.findViewById(R.id.taxonomyName);
            firstLetterView = view.findViewById(R.id.categoryFirstLetter);
            this.clickListener = listener;
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getAdapterPosition(),"posts");
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onLongClick(getAdapterPosition(),"posts");
            return true;
        }
    }

    private static class RecyclerCarouselSlider extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView sliderTitleView,sliderCategoryTitle;
        ImageView sliderImageView;
        OnHomePageItemClickListener clickListener;


        public RecyclerCarouselSlider(@NonNull View itemView, OnHomePageItemClickListener postListener) {
            super(itemView);
            sliderTitleView = itemView.findViewById(R.id.sliderTitleView);
            sliderImageView = itemView.findViewById(R.id.sliderImageView);
            sliderCategoryTitle = itemView.findViewById(R.id.sliderCategoryTitle);
            this.clickListener = postListener;
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

    private static class RecyclerCarouselSliderOuterTitle extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView sliderTitleView;
        ImageView sliderImageView;
        IconicsTextView postMetaTextView;
        OnHomePageItemClickListener clickListener;


        public RecyclerCarouselSliderOuterTitle(@NonNull View itemView, OnHomePageItemClickListener postListener) {
            super(itemView);
            sliderTitleView = itemView.findViewById(R.id.sliderTitleView);
            sliderImageView = itemView.findViewById(R.id.sliderImageView);
            postMetaTextView = itemView.findViewById(R.id.post_meta);
            this.clickListener = postListener;
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

    private static class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView title;
        ImageView imageView;
        IconicsTextView postMetaView;
        CardView imageViewContainer;
        OnHomePageItemClickListener onPostListener;

        SimpleItemViewHolder(@NonNull View itemView, OnHomePageItemClickListener postListener) {
            super(itemView);
            title = itemView.findViewById(R.id.post_title);
            imageView = itemView.findViewById(R.id.post_image);
            postMetaView = itemView.findViewById(R.id.post_meta);
            imageViewContainer = itemView.findViewById(R.id.post_image_container);
            onPostListener = postListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
           onPostListener.onClick(getAdapterPosition(),"post");
        }

        @Override
        public boolean onLongClick(View view) {
            onPostListener.onLongClick(getAdapterPosition(),"post");
            return true;
        }
    }
}
