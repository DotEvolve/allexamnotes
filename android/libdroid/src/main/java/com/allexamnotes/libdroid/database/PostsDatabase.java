package com.allexamnotes.libdroid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.allexamnotes.libdroid.database.dao.PostsDao;
import com.allexamnotes.libdroid.model.posts.Posts;

@Database(entities = {Posts.class}, version = 1,exportSchema = false)
public abstract class PostsDatabase extends RoomDatabase {

    private static PostsDatabase INSTANCE;

    public abstract PostsDao postsDao();

    public static PostsDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), PostsDatabase.class, "offline-posts")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
