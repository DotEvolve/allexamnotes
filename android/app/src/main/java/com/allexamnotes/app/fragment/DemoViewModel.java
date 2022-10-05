package com.allexamnotes.app.fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DemoViewModel extends ViewModel {
    private MutableLiveData<List<String>> list;


    public MutableLiveData<List<String>> getList() {
        List<String> stringList = new ArrayList<>();
        for (int i=0;i<10;i++){
            stringList.add("String "+i);
        }
        list.postValue(stringList);
        return list;
    }
}
