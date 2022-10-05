package com.allexamnotes.libdroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.allexamnotes.libdroid.model.posts.Posts;

import java.util.List;

@Dao
public interface PostsDao {

    @Query("SELECT * FROM posts")
    LiveData<List<Posts>> getAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Posts> posts);

    @Delete
    void delete(Posts post);
}
