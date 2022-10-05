package com.itsanubhav.wordroid4.fragment.tags;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.itsanubhav.libdroid.model.response.TagResponse;
import com.itsanubhav.libdroid.repo.TagRepository;

public class TagsViewModel extends ViewModel {

    private MutableLiveData<TagResponse> tagLiveData = new MutableLiveData<>();
    private TagRepository tagRepository;

    public TagsViewModel(){
        tagRepository = TagRepository.getInstance();

    }

    public LiveData<TagResponse> getTags(int page,String searchQuery){
        tagLiveData = tagRepository.getTags(page,searchQuery);
        return tagLiveData;
    }

}
