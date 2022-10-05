package com.allexamnotes.libdroid.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.allexamnotes.libdroid.model.category.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> posts);

    @Query("SELECT * FROM category")
    List<Category> getAllCategories();
}
