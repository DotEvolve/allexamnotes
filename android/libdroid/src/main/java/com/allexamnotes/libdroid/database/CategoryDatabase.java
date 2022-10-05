package com.allexamnotes.libdroid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.allexamnotes.libdroid.database.dao.CategoryDao;
import com.allexamnotes.libdroid.model.category.Category;

@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class CategoryDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();

    private static CategoryDatabase INSTANCE;

    public static CategoryDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), CategoryDatabase.class, "categories")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
