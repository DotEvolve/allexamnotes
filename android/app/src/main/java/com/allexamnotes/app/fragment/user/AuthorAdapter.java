package com.allexamnotes.app.fragment.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.R;
import com.allexamnotes.app.viewholders.LoadingViewHolder;
import com.bumptech.glide.Glide;
import com.allexamnotes.libdroid.model.LoadMoreViewClass;
import com.allexamnotes.libdroid.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW = 1;
    private static final int LOADING_VIEW = 2;

    private static LoadMoreViewClass loadingItem = LoadMoreViewClass.newInstance();


    private List<Object> userList = new ArrayList<>();
    private Context mContext;

    public AuthorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = userList.get(position);
        if (obj instanceof User){
            return ITEM_VIEW;
        }else{
            return LOADING_VIEW;
        }
    }

    public void clearItem(){
        userList.clear();
        notifyItemRemoved(userList.size());
    }

    public void addItems(List<User> objects){
        int tempPos = userList.size();
        userList.addAll(objects);
        addLoadingView();
        notifyItemInserted(tempPos);
    }

    public void addLoadingView(){
        userList.remove(loadingItem);
        userList.add(loadingItem);
        notifyItemInserted(userList.size()-1);
    }

    public void removeLoadingView(){
        userList.remove(loadingItem);
        notifyItemRemoved(userList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_VIEW){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.author_list_item,parent,false);
            return new AuthorViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_view_item,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType==ITEM_VIEW){
            AuthorViewHolder authorViewHolder = (AuthorViewHolder) holder;
            User user = (User) userList.get(position);
            Glide.with(mContext)
                    .load(user.getAvatarUrls().getJsonMember96())
                    .into(authorViewHolder.avatarImg);
            authorViewHolder.titleText.setText(user.getName());
            authorViewHolder.seePostsBtn.setOnClickListener(view -> Toast.makeText(mContext,"Cliecked "+user.getId(),Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public int getItemCount() {
        return userList!=null ? userList.size() : 0;
    }

    private static class AuthorViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImg;
        TextView titleText;
        Button seePostsBtn;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImg = itemView.findViewById(R.id.avatar_img);
            titleText = itemView.findViewById(R.id.title_text);
            seePostsBtn = itemView.findViewById(R.id.postsBtn);
        }
    }
}
