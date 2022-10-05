package com.allexamnotes.app.fragment.notification;

import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.allexamnotes.libdroid.database.PostDatabase;
import com.allexamnotes.libdroid.database.dao.NotificationDao;
import com.allexamnotes.app.R;
import com.allexamnotes.app.listeners.OnItemClickListener;

public class NotificationFragment extends Fragment implements OnItemClickListener{

    private NotificationViewModel mViewModel;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private OnItemClickListener onItemClickListener;
    private ExtendedFloatingActionButton deletefab;

    private View no_content;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        deletefab = rootView.findViewById(R.id.deletefab);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        no_content = rootView.findViewById(R.id.no_contents);
        adapter = new NotificationAdapter(getContext(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        deletefab.setOnClickListener(view -> new DeleteAllNotification().execute());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        loadNotifications();
    }

    private void loadNotifications(){
        mViewModel.notificationList.observe(this, list -> {
            adapter.addItems(list);
            if (list.size()==0){
                deletefab.setVisibility(View.GONE);
                no_content.setVisibility(View.VISIBLE);
            }else{
                deletefab.setVisibility(View.VISIBLE);
                no_content.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(int position) {

    }

    class DeleteAllNotification extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            NotificationDao dao = PostDatabase.getAppDatabase(getContext()).notificationDao();
            dao.deleteAllNotifications();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            no_content.setVisibility(View.VISIBLE);
        }
    }
}
