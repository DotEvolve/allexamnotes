package com.allexamnotes.app.fragment.notification;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.allexamnotes.libdroid.database.PostDatabase;
import com.allexamnotes.libdroid.database.dao.NotificationDao;
import com.allexamnotes.libdroid.model.notification.Notification;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    LiveData<List<Notification>> notificationList;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        loadNotification(getApplication());
    }


    private void loadNotification(Context c){
        NotificationDao dao = PostDatabase.getAppDatabase(c).notificationDao();
        notificationList = dao.getAllNotifications();
    }


}
