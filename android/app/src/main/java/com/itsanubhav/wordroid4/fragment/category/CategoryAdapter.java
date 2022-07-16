package com.itsanubhav.wordroid4.fragment.category;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.itsanubhav.wordroid4.ContainerActivity;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.others.Utils;
import com.itsanubhav.wordroid4.viewholders.LoadingViewHolder;
import com.itsanubhav.libdroid.model.LoadMoreViewClass;
import com.itsanubhav.libdroid.model.category.Category;
import com.itsanubhav.wordroid4.viewholders.UnifiedNativeAdViewHolder;
import com.mikepenz.iconics.view.IconicsTextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int CATEGORY_TYPE = 1;
    private static final int CATEGORY_GRID_TYPE = 2;
    private static final int LOADING_MORE = 3;
    private static final int ADMOB_NATIVE_AD = 5;

    private List<Object> itemsList = new ArrayList<>();
    private boolean layoutType;
    private Context mContext;
    private onCategoryListener onCategoryListener;
    private static LoadMoreViewClass loadingItem = LoadMoreViewClass.newInstance();

    public CategoryAdapter(Context mContext,onCategoryListener listener,boolean layoutType) {
        this.mContext = mContext;
        this.onCategoryListener = listener;
        this.layoutType = layoutType;
    }

    public void addItems(List<Category> itemsList) {
        int tempPos = this.itemsList.size();
        this.itemsList.addAll(itemsList);
        notifyItemRangeInserted(tempPos,itemsList.size());
        addLoadMoreView();

    }

    public void insertAdItem(Object object,int position){
        if (position<itemsList.size()) {
            itemsList.add(position, object);
            notifyItemInserted(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsList.get(position) instanceof Category){
            if (layoutType)
                return CATEGORY_TYPE;
            else
                return CATEGORY_GRID_TYPE;
        }if (itemsList.get(position) instanceof UnifiedNativeAd) {
            return ADMOB_NATIVE_AD;
        }else {
            return LOADING_MORE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==CATEGORY_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout_1,parent,false);
            return new ItemLayout1ViewHolder(view,onCategoryListener);
        }else if(viewType==CATEGORY_GRID_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_taxanomy_layout,parent,false);
            return new TaxonomyViewHolder(view,onCategoryListener);
        }else if(viewType == ADMOB_NATIVE_AD){
            View unifiedAdLayout = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.admob_ad_item,
                    parent, false);
            return new UnifiedNativeAdViewHolder(unifiedAdLayout);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_view_item,parent,false);
            return new LoadingViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemType = holder.getItemViewType();
        if (itemType==CATEGORY_TYPE){
            ItemLayout1ViewHolder viewHolder = (ItemLayout1ViewHolder) holder;
            Category category = (Category) itemsList.get(position);
            viewHolder.categoryName.setText(category.getName());
            viewHolder.firstLetter.setText(category.getName().substring(0,1).toUpperCase());
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{}
            },
                    new int[]{
                            Utils.generateRandomColor()
                    });
            viewHolder.firstLetter.setBackgroundTintList(colorStateList);
            //viewHolder.firstLetter.setBackgroundTintList(ContextCompat.getColorStateList(mContext,));
            viewHolder.countView.setText(String.valueOf(category.getCount()));

        }else if (itemType==CATEGORY_GRID_TYPE){
            Category category = (Category) itemsList.get(position);
            TaxonomyViewHolder taxanomyViewHolder = (TaxonomyViewHolder) holder;
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{}
            },
                    new int[]{
                            Utils.generateRandomColor()
                    });
            taxanomyViewHolder.firstLetterView.setBackgroundTintList(colorStateList);
            taxanomyViewHolder.firstLetterView.setText(category.getName().substring(0,1).toUpperCase());
            taxanomyViewHolder.taxonomyName.setText(Jsoup.parse(category.getName()).text());
            taxanomyViewHolder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ContainerActivity.class);
                intent.putExtra("title",category.getName());
                intent.putExtra("screen","posts");
                intent.putExtra("category",String.valueOf(category.getId()));
                mContext.startActivity(intent);
            });
        }else if (itemType==ADMOB_NATIVE_AD){
            UnifiedNativeAd nativeAd = (UnifiedNativeAd) itemsList.get(position);
            populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
        }
    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }


    public static class ItemLayout1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView categoryName,firstLetter,countView;
        onCategoryListener onCategoryListener;

        public ItemLayout1ViewHolder(@NonNull View itemView,onCategoryListener listener) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_title);
            firstLetter = itemView.findViewById(R.id.categoryFirstLetter);
            countView = itemView.findViewById(R.id.category_count);
            onCategoryListener = listener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onCategoryListener.onCategoryClick(getAdapterPosition());
        }
    }

    public static class ItemLayout2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titleView;
        CardView cardView;
        ImageView imageView;
        onCategoryListener onCategoryListener;

        public ItemLayout2ViewHolder(@NonNull View itemView,onCategoryListener onCategoryListener) {
            super(itemView);
            titleView = itemView.findViewById(R.id.category_title);
            cardView = itemView.findViewById(R.id.category_card);
            imageView = itemView.findViewById(R.id.category_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCategoryListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface onCategoryListener{
        void onCategoryClick(int position);
    }

    private static class TaxonomyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IconicsTextView firstLetterView;
        TextView taxonomyName;
        onCategoryListener clickListener;


        public TaxonomyViewHolder(View view, onCategoryListener listener) {
            super(view);
            taxonomyName = view.findViewById(R.id.taxonomyName);
            firstLetterView = view.findViewById(R.id.categoryFirstLetter);
            this.clickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onCategoryClick(getAdapterPosition());
        }

    }

    public void addLoadMoreView(){
        itemsList.remove(loadingItem);
        itemsList.add(loadingItem);
        //notifyItemInserted(itemsList.size()-1);
    }

    public void removeLoadMoreView(){
        itemsList.remove(loadingItem);
        notifyItemRemoved(itemsList.size());
    }

    public void clearAdapter(){
        itemsList.clear();
        notifyDataSetChanged();
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }


}