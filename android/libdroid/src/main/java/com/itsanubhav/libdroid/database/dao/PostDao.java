package com.itsanubhav.libdroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsanubhav.libdroid.model.post.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from post ORDER BY id DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("select * from post where post.id = :postId")
    LiveData<List<Post>> getPost(int postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post... posts);

    @Delete
    void deletePost(Post post);

    @Query("DELETE FROM post")
    void deleteAll();

}
