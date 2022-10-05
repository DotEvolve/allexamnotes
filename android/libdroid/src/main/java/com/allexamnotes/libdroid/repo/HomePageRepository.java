package com.allexamnotes.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.allexamnotes.libdroid.Utility;
import com.allexamnotes.libdroid.model.WorDroidSection;
import com.allexamnotes.libdroid.network.ApiClient;
import com.allexamnotes.libdroid.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageRepository {

    private static HomePageRepository homePageRepository;
    private ApiInterface apiInterface;

    public static HomePageRepository getInstance(){
        if (homePageRepository==null){
            homePageRepository = new HomePageRepository();
        }
        return homePageRepository;
    }

    public HomePageRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<List<WorDroidSection>> getHomePageData(){
        final MutableLiveData<List<WorDroidSection>> sections = new MutableLiveData<>();
        //network request here

        Call<List<WorDroidSection>> call = apiInterface.getHomePageSections(Utility.getRandomString());
        Log.e("MakingRequest","To: "+call.request().url());
        call.enqueue(new Callback<List<WorDroidSection>>() {
            @Override
            public void onResponse(Call<List<WorDroidSection>> call, Response<List<WorDroidSection>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    sections.postValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<WorDroidSection>> call, Throwable t) {
                t.printStackTrace();
                sections.postValue(null);
            }
        });

        return sections;
    }
}
