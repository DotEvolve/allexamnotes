package com.allexamnotes.app.fragment.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allexamnotes.libdroid.model.post.Post;
import com.allexamnotes.libdroid.repo.PostRepository;

public class PostViewModel extends ViewModel {

    private PostRepository repository;

    public PostViewModel(){
        repository = PostRepository.getInstance();
    }

    public LiveData<Post> getPost(int postId){
        return repository.getPost(postId);
    }

    public LiveData<Post> getPost(String slug){
        return repository.getPost(slug);
    }


}
