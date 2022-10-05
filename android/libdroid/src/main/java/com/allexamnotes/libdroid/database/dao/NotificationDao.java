package com.allexamnotes.libdroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.allexamnotes.libdroid.model.notification.Notification;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("select * from notification ORDER BY id DESC")
    LiveData<List<Notification>> getAllNotifications();

    @Query("select * from notification where notification.id = :postId")
    LiveData<List<Notification>> getNotification(int postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotification(Notification... notifications);

    @Delete
    void deleteNotification(Notification notification);

    @Query("DELETE FROM notification")
    void deleteAllNotifications();
}
