package com.itsanubhav.libdroid.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.itsanubhav.libdroid.model.auth.AuthResponse;
import com.itsanubhav.libdroid.network.ApiClient;
import com.itsanubhav.libdroid.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static AuthRepository authRepository;
    private ApiInterface apiInterface;

    public static AuthRepository getInstance(){
        if (authRepository==null){
            authRepository = new AuthRepository();
        }
        return authRepository;
    }

    public AuthRepository() {
        this.apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public LiveData<AuthResponse> makeAuth(@NonNull String username, @NonNull String password){
        final MutableLiveData<AuthResponse> token = new MutableLiveData<>();
        Call<AuthResponse> call = apiInterface.makeAuth(username, password);
        Log.e("MakingRequest","Url: "+call.request().url());
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    token.postValue(response.body());
                    Log.e("Response",""+response.body().toString());
                }else {
                    token.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return token;
    }

    public MutableLiveData<AuthResponse> validate(){
        final MutableLiveData<AuthResponse> token = new MutableLiveData<>();
        Call<AuthResponse> call = apiInterface.validateAuth();
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    token.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
        return token;
    }
}
