package com.itsanubhav.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.itsanubhav.libdroid.model.response.TagResponse;
import com.itsanubhav.libdroid.model.tag.Tag;
import com.itsanubhav.libdroid.network.ApiClient;
import com.itsanubhav.libdroid.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagRepository {

    private static final boolean HIDE_EMPTY_CATEGORIES = true;

    private static TagRepository tagRepository;
    private ApiInterface apiInterface;

    public static TagRepository getInstance(){
        if (tagRepository==null){
            tagRepository = new TagRepository();
        }
        return tagRepository;
    }

    public TagRepository() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<TagResponse> getTags(int page, String search){
        final MutableLiveData<TagResponse> tags = new MutableLiveData<>();
        Call<List<Tag>> call = apiInterface.getTagsList(page,search);
        Log.e("Making Request", call.request().url().toString());
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    try{
                        Log.d("Making Request",response.body().size()+" posts loaded");
                        int totalCategoryPages = Integer.parseInt(response.headers().get("x-wp-totalpages"));
                        tags.postValue(new TagResponse(response.body(),totalCategoryPages));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {

            }
        });
        return tags;
    }

}
