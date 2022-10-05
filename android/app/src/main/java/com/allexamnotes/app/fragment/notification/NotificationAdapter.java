package com.allexamnotes.app.fragment.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.MainActivity;
import com.allexamnotes.app.PostContainerActivity;
import com.allexamnotes.app.R;
import com.allexamnotes.app.fragment.webview.WebViewFragment;
import com.allexamnotes.app.listeners.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.allexamnotes.libdroid.model.notification.Notification;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notification> notificationList = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;

    public NotificationAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void addItems(List<Notification> list){
        notificationList.clear();
        notificationList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item,parent,false);
        return new NotificationViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
        notificationViewHolder.title.setText(Jsoup.parse(notificationList.get(position).getTitle()).text());
        notificationViewHolder.type.setText(notificationList.get(position).getType());
        notificationViewHolder.timestamp.setText(notificationList.get(position).getTimestamp());
        Glide.with(context)
                .load(notificationList.get(position).getImage())
                .error(R.color.md_red_200)
                .into(notificationViewHolder.imageView);
        notificationViewHolder.itemView.setOnClickListener(view -> {
            switch (notificationList.get(position).getType()){
                case "post": {
                    Intent intent = new Intent(context, PostContainerActivity.class);
                    intent.putExtra("postId", notificationList.get(position).getPostId());
                    intent.putExtra("img", notificationList.get(position).getImage());
                    intent.putExtra("title", notificationList.get(position).getTitle());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                break;
                case "web": {
                    if (context instanceof ContainerActivity){
                        try {
                            ContainerActivity a = (ContainerActivity) context;
                            assert a != null;
                            a.addFragment(WebViewFragment.newInstance(notificationList.get(position).url),"");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        Intent intent = new Intent(context, ContainerActivity.class);
                        intent.putExtra("screen", "webview");
                        intent.putExtra("url", notificationList.get(position).url);
                        context.startActivity(intent);
                    }

                }
                break;
                case "message": {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("msgTitle",notificationList.get(position).getMsgTitle());
                    intent.putExtra("msgBody",notificationList.get(position).getMsgBody());
                    context.startActivity(intent);
                }
                break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


    public static class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, type, timestamp;
        private ImageView imageView;
        private OnItemClickListener clickListener;

        public NotificationViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            type = itemView.findViewById(R.id.notification_type);
            timestamp = itemView.findViewById(R.id.timestamp);
            imageView = itemView.findViewById(R.id.notification_image);
            clickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(getAdapterPosition());
        }
    }
}
