package com.allexamnotes.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.DetailActivity;
import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnHomePageItemClickListener;
import com.allexamnotes.app.others.Constants;
import com.allexamnotes.app.viewholders.EmptySpaceViewHolder;
import com.bumptech.glide.Glide;
import com.allexamnotes.libdroid.model.WorDroidSection;
import com.allexamnotes.libdroid.model.WorDroidSectionItems;
import com.synnapps.carouselview.CarouselView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WorDroidSection> objectList = new ArrayList<>();
    private Context mContext;
    private OnHomePageItemClickListener onHomePageItemClickListener;
    private RecyclerView.RecycledViewPool viewPool;

    public ParentAdapter(Context mContext,OnHomePageItemClickListener listener) {
        this.mContext = mContext;
        this.onHomePageItemClickListener = listener;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void addObject(List<WorDroidSection> o){
        int tempPosition = objectList.size();
        objectList.addAll(o);
        notifyItemInserted(tempPosition);
    }

    public void clearItems(){
        objectList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position)!=null)
            return objectList.get(position).getLayoutType();
        else
            return 1009;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==Constants.AUTO_SLIDER||viewType==Constants.BANNER_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider,parent,false);
            return new AutoSliderVH(view);
        } else if (viewType==1009){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_space,parent,false);
            return new EmptySpaceViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_recyclerview,parent,false);
            ChildRecyclerViewHolder holder = new ChildRecyclerViewHolder(view);
            holder.childRecycler.setRecycledViewPool(viewPool);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==Constants.AUTO_SLIDER){
            //Create slider here
            List<WorDroidSectionItems> posts = objectList.get(position).getItems();
            AutoSliderVH autoSliderVH = (AutoSliderVH) holder;
            autoSliderVH.carouselView.setPageCount(posts.size());
            autoSliderVH.carouselView.setViewListener(position1 -> {
                View customView = LayoutInflater.from(mContext).inflate(R.layout.slider_item_layout, null);
                TextView titleView = customView.findViewById(R.id.sliderTitleView);
                ImageView imageView = customView.findViewById(R.id.sliderImageView);
                titleView.setText(Jsoup.parse(posts.get(position1).getTitle()).text());
//                Glide.with(holder.itemView.getContext())
//                        .load(posts.get(position1).getFeaturedImg())
//                        .into(imageView);
                customView.setOnClickListener(view -> {
                    /*Intent intent = new Intent(mContext, PostContainerActivity.class);
                    intent.putExtra("postId", posts.get(position1).getId());
                    intent.putExtra("img", posts.get(position1).getFeaturedImg());
                    intent.putExtra("title", posts.get(position1).getTitle());
                    mContext.startActivity(intent);*/
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("postId", posts.get(position1).getId());
                    intent.putExtra("img", posts.get(position1).getFeaturedImg());
                    intent.putExtra("title", posts.get(position1).getTitle());
                    mContext.startActivity(intent);

                });
                return customView;
            });
        }else if(getItemViewType(position)==Constants.BANNER_TYPE) {
            String data = objectList.get(position).getData();
            List<String> imageUrls = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            //Toast.makeText(mContext, "Data: "+data, Toast.LENGTH_SHORT).show();
            try {
                if (data.contains(",")){
                    //multiple items in posts
                    String[] items = data.split(",");
                    //items is in the format Title:ID
                    for (String item : items) {
                        if (item.contains("|")) {
                            String[] temp = item.split("\\|");
                            //Toast.makeText(mContext, "Length: "+temp.length, Toast.LENGTH_SHORT).show();
                            if (temp.length == 2) {
                                imageUrls.add(temp[0]);
                                urls.add(temp[1]);

                            }
                        }
                    }
                }else {
                    String[] temp = data.split("|");
                    if (temp.length==2){
                        imageUrls.add(temp[0]);
                        urls.add(temp[1]);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            //Toast.makeText(mContext, "Length: "+imageUrls.size(), Toast.LENGTH_SHORT).show();
            AutoSliderVH autoSliderVH = (AutoSliderVH) holder;
            autoSliderVH.carouselView.setPageCount(imageUrls.size());
            autoSliderVH.carouselView.setViewListener(position1 -> {
                View customView = LayoutInflater.from(mContext).inflate(R.layout.slider_item_layout, null);
                TextView titleView = customView.findViewById(R.id.sliderTitleView);
                ImageView imageView = customView.findViewById(R.id.sliderImageView);
                titleView.setVisibility(View.GONE);
                Glide.with(holder.itemView.getContext())
                        .load(imageUrls.get(position1))
                        .into(imageView);
                customView.setOnClickListener(view -> {
                    /*Intent intent = new Intent(mContext, PostContainerActivity.class);
                    intent.putExtra("postId", posts.get(position1).getId());
                    intent.putExtra("img", posts.get(position1).getFeaturedImg());
                    intent.putExtra("title", posts.get(position1).getTitle());
                    mContext.startActivity(intent);*/
                    Intent intent = new Intent(mContext, ContainerActivity.class);
                    intent.putExtra("screen","webview");
                    intent.putExtra("url",urls.get(position1));
                    mContext.startActivity(intent);

                });
                return customView;
            });
        }else{
            try {
                @SuppressWarnings("unchecked")
                ChildRecyclerViewHolder childRecyclerViewHolder = (ChildRecyclerViewHolder) holder;
                ChildAdapter adapter = new ChildAdapter(mContext,onHomePageItemClickListener);
                adapter.setItemType(objectList.get(position).getLayoutType());
                adapter.addItems(objectList.get(position).getItems());
                adapter.setContentType(objectList.get(position).getContentType());
                if (objectList.get(position).getLayoutType()==2){
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
                    childRecyclerViewHolder.childRecycler.setLayoutManager(gridLayoutManager);
                    //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
                    //childRecyclerViewHolder.childRecycler.addItemDecoration(new EqualSpacingItemDecoration(20, EqualSpacingItemDecoration.GRID));
                }else if (objectList.get(position).getLayoutType()==6){
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    childRecyclerViewHolder.childRecycler.setLayoutManager(linearLayoutManager);
                }else if(objectList.get(position).getLayoutType()==5) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                    childRecyclerViewHolder.childRecycler.setLayoutManager(linearLayoutManager);
                }else {
                    SnapHelper snapHelper = new LinearSnapHelper();
                    snapHelper.attachToRecyclerView(childRecyclerViewHolder.childRecycler);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                    childRecyclerViewHolder.childRecycler.setLayoutManager(linearLayoutManager);
                }

                childRecyclerViewHolder.seeMoreBtn.setOnClickListener(view -> {
                    switch (objectList.get(position).getContentType()){
                        case 1:{
                            Intent intent = new Intent(mContext, ContainerActivity.class);
                            intent.putExtra("title", objectList.get(position).getTitle());
                            intent.putExtra("category",objectList.get(position).getCategory());
                            intent.putExtra("tags",objectList.get(position).getTags());
                            intent.putExtra("screen", "posts");
                            mContext.startActivity(intent);
                            break;
                        }
                        case 2:{
                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(mContext, ContainerActivity.class);
                            intent.putExtra("title", "Categories");
                            intent.putExtra("screen", "categories");
                            mContext.startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(mContext, ContainerActivity.class);
                            intent.putExtra("title", "Tags");
                            intent.putExtra("screen", "tags");
                            mContext.startActivity(intent);
                            break;
                        }
                        default:
                            Intent intent = new Intent(mContext, ContainerActivity.class);
                            intent.putExtra("title", objectList.get(position).getTitle());
                            intent.putExtra("category",objectList.get(position).getCategory());
                            intent.putExtra("tags",objectList.get(position).getTags());
                            intent.putExtra("screen", "posts");
                            mContext.startActivity(intent);
                    }
                });

                childRecyclerViewHolder.childRecycler.setAdapter(adapter);
                if (objectList.get(position).getTitle()==null||TextUtils.isEmpty(objectList.get(position).getTitle()))
                    childRecyclerViewHolder.topPanel.setVisibility(View.GONE);
                else
                    childRecyclerViewHolder.childRecyclerTitle.setText(objectList.get(position).getTitle());
            }catch (Exception e){
                //Toast.makeText(mContext,"Exception in sections",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectList == null ? 0 : objectList.size();
    }

    private static class AutoSliderVH extends RecyclerView.ViewHolder{

        CarouselView carouselView;

        public AutoSliderVH(@NonNull View itemView) {
            super(itemView);
            carouselView = itemView.findViewById(R.id.carouselView);
        }
    }

    private class ChildRecyclerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView childRecycler;
        TextView childRecyclerTitle;
        Button seeMoreBtn;
        RelativeLayout topPanel;

        public ChildRecyclerViewHolder(View view) {
            super(view);
            childRecycler = view.findViewById(R.id.childRecyclerView);
            childRecyclerTitle = view.findViewById(R.id.childRecyclerTitle);
            seeMoreBtn = view.findViewById(R.id.seeMore);
            topPanel = view.findViewById(R.id.topPanel);
        }

    }


}
