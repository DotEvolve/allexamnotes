package com.itsanubhav.wordroid4.fragment.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.itsanubhav.libdroid.model.response.CategoryResponse;
import com.itsanubhav.libdroid.repo.CategoryRepository;

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

    public LiveData<CategoryResponse> getCategory(int page, String search, Integer parent){
        mutableLiveData = categoryRepository.getCategories(page,search,parent);
        return mutableLiveData;
    }

}
