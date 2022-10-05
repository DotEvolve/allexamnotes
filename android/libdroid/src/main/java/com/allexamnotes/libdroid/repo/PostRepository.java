package com.allexamnotes.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.allexamnotes.libdroid.model.post.Post;
import com.allexamnotes.libdroid.network.ApiClient;
import com.allexamnotes.libdroid.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {

    private static PostRepository postRepository;
    private ApiInterface apiInterface;

    public static PostRepository getInstance(){
        if (postRepository==null){
            postRepository = new PostRepository();
        }
        return postRepository;
    }

    public PostRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<Post> getPost(int postId){
        final MutableLiveData<Post> postMutableLiveData = new MutableLiveData<>();
        Call<Post> call = apiInterface.getPost(postId);
        Log.e("Making Request","Url: "+call.request().url());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.e("ResponseCode","ResCode: "+response.code());
                if (response.isSuccessful()&&response.body()!=null){
                    postMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("Library Error",t.getLocalizedMessage());
            }
        });
        return postMutableLiveData;
    }

    public MutableLiveData<Post> getPost(String slug){
        final MutableLiveData<Post> postMutableLiveData = new MutableLiveData<>();
        Call<List<Post>> call = apiInterface.getPost(slug);
        Log.e("Making Request","Url: "+call.request().url());
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.e("ResponseCode","ResCode: "+response.code());
                if (response.isSuccessful()&&response.body()!=null){
                    postMutableLiveData.postValue(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("Library Error",t.getLocalizedMessage());
            }
        });
        return postMutableLiveData;
    }

}
