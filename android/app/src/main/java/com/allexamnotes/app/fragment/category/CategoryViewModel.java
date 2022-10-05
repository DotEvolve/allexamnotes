package com.allexamnotes.app.fragment.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allexamnotes.libdroid.model.response.CategoryResponse;
import com.allexamnotes.libdroid.repo.CategoryRepository;

public class CategoryViewModel extends ViewModel {

    private MutableLiveData<CategoryResponse> mutableLiveData;
    private CategoryRepository categoryRepository;

    public CategoryViewModel(){
        init();
    }

    private void init(){
        if (mutableLiveData != null){
            return;
        }
        categoryRepository = CategoryRepository.getInstance();
    }

    public LiveData<CategoryResponse> getCategory(int page, String search, Integer parent,String exclude){
        mutableLiveData = categoryRepository.getCategories(page,search,parent,exclude);
        return mutableLiveData;
    }

}
