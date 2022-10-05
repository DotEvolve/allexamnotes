package com.itsanubhav.wordroid4.fragment.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.itsanubhav.libdroid.model.response.CommentResponse;
import com.itsanubhav.libdroid.repo.CommentRepository;

public class CommentsViewModel extends ViewModel {

    private CommentRepository commentRepository;

    public CommentsViewModel(){
       commentRepository = CommentRepository.getInstance();
    }

    public LiveData<CommentResponse> getComments(int page,Integer parent,int postId,String searchQuery){
        return commentRepository.getComments(page,parent,postId,searchQuery);
    }


}
