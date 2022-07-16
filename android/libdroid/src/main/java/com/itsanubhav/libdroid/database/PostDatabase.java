package com.itsanubhav.libdroid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.itsanubhav.libdroid.database.dao.NotificationDao;
import com.itsanubhav.libdroid.database.dao.PostDao;
import com.itsanubhav.libdroid.model.notification.Notification;
import com.itsanubhav.libdroid.model.post.Post;

@Database(entities = {Post.class, Notification.class}, version = 1, exportSchema = false)
public abstract class PostDatabase extends RoomDatabase {

    private static PostDatabase INSTANCE;

    public abstract PostDao postsDao();

    public abstract NotificationDao notificationDao();

    public static PostDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), PostDatabase.class, "post")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
