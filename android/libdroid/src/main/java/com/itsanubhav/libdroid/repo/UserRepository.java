package com.itsanubhav.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.itsanubhav.libdroid.model.response.UsersResponse;
import com.itsanubhav.libdroid.model.user.User;
import com.itsanubhav.libdroid.network.ApiClient;
import com.itsanubhav.libdroid.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static UserRepository repository;
    private static ApiInterface apiInterface;

    public static UserRepository newInstance(){
        if (repository==null){
            repository = new UserRepository();
        }
        return repository;
    }

    public UserRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<UsersResponse> getUsers(int page, String searchQuery){
        final MutableLiveData<UsersResponse> usersList = new MutableLiveData<>();
        Call<List<User>> call = apiInterface.getUsers(page,searchQuery);
        Log.e("Making Request","Url: "+call.request().url());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    int totalPages = Integer.parseInt(response.headers().get("x-wp-totalpages"));
                    usersList.postValue(new UsersResponse(response.body(),totalPages));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                usersList.postValue(new UsersResponse(null,0));
            }
        });
        return usersList;
    }
}
