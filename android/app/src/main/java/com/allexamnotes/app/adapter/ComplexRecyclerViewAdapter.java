package com.allexamnotes.app.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.libdroid.model.LoadMoreViewClass;
import com.allexamnotes.libdroid.model.post.Post;

import java.util.ArrayList;
import java.util.List;

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LoadMoreViewClass loadingItem = LoadMoreViewClass.newInstance();

    private List<Object> itemsList = new ArrayList<>();
    private Context context;

    public ComplexRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = itemsList.get(position);
        if (item instanceof Post){


        }else if (item instanceof LoadMoreViewClass){
            return 0;
        }
        return super.getItemViewType(position);
    }

    public void addItems(List<Object> objects){
        itemsList.addAll(objects);
        notifyItemInserted(itemsList.size()-objects.size());
        addLoadMoreView();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addLoadMoreView(){
        itemsList.remove(loadingItem);
        itemsList.add(loadingItem);
        notifyItemInserted(itemsList.size()-1);
    }

}
