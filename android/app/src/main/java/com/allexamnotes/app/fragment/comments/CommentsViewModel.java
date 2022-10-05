package com.allexamnotes.app.fragment.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allexamnotes.libdroid.model.response.CommentResponse;
import com.allexamnotes.libdroid.repo.CommentRepository;

public class CommentsViewModel extends ViewModel {

    private CommentRepository commentRepository;

    public CommentsViewModel(){
       commentRepository = CommentRepository.getInstance();
    }

    public LiveData<CommentResponse> getComments(int page,Integer parent,int postId,String searchQuery){
        return commentRepository.getComments(page,parent,postId,searchQuery);
    }


}
