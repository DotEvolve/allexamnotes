package com.itsanubhav.wordroid4.fragment.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.itsanubhav.libdroid.model.WorDroidSection;
import com.itsanubhav.libdroid.repo.HomePageRepository;

import java.util.List;

public class HomePageViewModel extends ViewModel {

    private MutableLiveData<List<WorDroidSection>> sectionLiveData;
    private HomePageRepository homePageRepository;

    public HomePageViewModel(){
        init();
    }

    private void init(){
        if (sectionLiveData != null){
            return;
        }
        homePageRepository = HomePageRepository.getInstance();
    }

    public LiveData<List<WorDroidSection>> getHomePageSections(){
        sectionLiveData = homePageRepository.getHomePageData();
        return  sectionLiveData;
    }


}
