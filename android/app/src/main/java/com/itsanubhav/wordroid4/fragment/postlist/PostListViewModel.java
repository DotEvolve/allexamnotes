package com.itsanubhav.wordroid4.fragment.postlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.itsanubhav.libdroid.model.response.CategoryResponse;
import com.itsanubhav.libdroid.model.response.PostResponse;
import com.itsanubhav.libdroid.repo.CategoryRepository;
import com.itsanubhav.libdroid.repo.PostsRepository;

public class PostListViewModel extends AndroidViewModel {

    private MutableLiveData<PostResponse> mutableLiveData;
    private MutableLiveData<CategoryResponse> categoryLiveData;
    private PostsRepository postsRepository;
    private CategoryRepository categoryRepository;

    public PostListViewModel(@NonNull Application application) {
        super(application);
        init();
    }


    public void init(){
        if (mutableLiveData != null){
            return;
        }
        postsRepository = PostsRepository.getInstance(getApplication());
        categoryRepository = CategoryRepository.getInstance();
    }

    public LiveData<PostResponse> getNewsRepository(int page, String category, String search, String tag, String author) {
        mutableLiveData = postsRepository.getNews(page,search,category,tag,author);
        return mutableLiveData;
    }

    public LiveData<CategoryResponse> getSubCategories(int page,Integer parentCategry){
        categoryLiveData = categoryRepository.getCategories(page,null,parentCategry);
        return categoryLiveData;
    }

}
