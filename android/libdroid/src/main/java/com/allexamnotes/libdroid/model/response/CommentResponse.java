package com.allexamnotes.libdroid.model.response;

import com.allexamnotes.libdroid.model.comment.Comment;

import java.util.List;

public class CommentResponse {
    int totalPages;
    List<Comment> comments;

    public CommentResponse(List<Comment> comments, int totalPages) {
        this.totalPages = totalPages;
        this.comments = comments;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
