package com.itsanubhav.wordroid4.fragment.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.listeners.OnCommentClickListener;
import com.itsanubhav.wordroid4.others.Utils;
import com.itsanubhav.wordroid4.viewholders.LoadingViewHolder;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.itsanubhav.libdroid.model.LoadMoreViewClass;
import com.itsanubhav.libdroid.model.comment.Comment;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PARENT_ITEM = 1;
    private static final int LOADING_ITEM = 3;

    private List<Object> objectList = new ArrayList<>();
    private Context context;
    private OnCommentClickListener clickListener;
    private static LoadMoreViewClass loadingItem = LoadMoreViewClass.newInstance();

    public CommentAdapter(Context context, OnCommentClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void addObjects(List<Comment> obj) {
        int tempPos = objectList.size();
        objectList.addAll(obj);
        notifyDataSetChanged();
        addLoadMoreView();
    }

    public void clearObjects(){
        this.objectList.clear();
        notifyDataSetChanged();
    }

    public void addLoadMoreView(){
        objectList.remove(loadingItem);
        objectList.add(loadingItem);
        notifyDataSetChanged();
    }

    public void removeLoadingView(){
        objectList.remove(loadingItem);
        //notifyItemRemoved(objectList.size());
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        Object obj = objectList.get(position);
        if (obj instanceof Comment){
            return PARENT_ITEM;
        }else {
            return LOADING_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==PARENT_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item,parent,false);
            return new ParentCommentItemViewHolder(view,clickListener);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_more_view_item,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType==PARENT_ITEM){
            if (objectList.get(position) instanceof Comment) {
                ParentCommentItemViewHolder viewHolder = (ParentCommentItemViewHolder) holder;
                Comment comment = (Comment) objectList.get(position);
                viewHolder.content.setText(Jsoup.parse(comment.getContent().getRendered()).text());
                viewHolder.dateView.setText(Utils.parseDate(comment.getDate()));
                viewHolder.nameTextView.setText(comment.getAuthorName());
                viewHolder.repliesBtn.setText(String.format(context.getResources().getString(R.string.replies_comment_text),String.valueOf(comment.getChildCommentCount())));
                Glide.with(context)
                        .load(comment.getAuthorAvatarUrls().getJsonMember96())
                        .into(viewHolder.imageView);
            }
        }else {

        }
    }

    @Override
    public int getItemCount() {
        return objectList!=null ? objectList.size() : 0;
    }

    private static class ParentCommentItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView nameTextView;
        private TextView dateView;
        private TextView content;
        private Button repliesBtn;
        private MaterialButton replyBtn;
        private OnCommentClickListener clickListener;

        public ParentCommentItemViewHolder(@NonNull View itemView,OnCommentClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            imageView = itemView.findViewById(R.id.avatar_img);
            nameTextView = itemView.findViewById(R.id.nameView);
            dateView = itemView.findViewById(R.id.dateTime);
            content = itemView.findViewById(R.id.content);
            repliesBtn = itemView.findViewById(R.id.repliesBtn);
            replyBtn = itemView.findViewById(R.id.replyBtn);
            repliesBtn.setOnClickListener(this);
            replyBtn.setOnClickListener(view -> clickListener.onReplyBtnClicked(getAdapterPosition()));
        }

        @Override
        public void onClick(View view) {
            clickListener.onRepliesBtnClicked(getAdapterPosition());
        }
    }
}
