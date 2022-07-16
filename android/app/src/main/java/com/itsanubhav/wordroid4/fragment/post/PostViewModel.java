package com.itsanubhav.wordroid4.fragment.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.itsanubhav.libdroid.model.post.Post;
import com.itsanubhav.libdroid.repo.PostRepository;

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
