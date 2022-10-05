package com.allexamnotes.libdroid.model.response;


import com.allexamnotes.libdroid.model.posts.Posts;

import java.util.List;

public class PostResponse {
    private int totalPages;
    private List<Posts> posts;

    public PostResponse(List<Posts> posts, int totalPages) {
        this.totalPages = totalPages;
        this.posts = posts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Posts> getPosts() {
        return posts;
    }
}
