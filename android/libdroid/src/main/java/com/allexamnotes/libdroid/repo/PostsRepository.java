package com.allexamnotes.libdroid.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.allexamnotes.libdroid.database.PostsDatabase;
import com.allexamnotes.libdroid.database.dao.PostsDao;
import com.allexamnotes.libdroid.model.posts.Posts;
import com.allexamnotes.libdroid.model.response.PostResponse;
import com.allexamnotes.libdroid.network.ApiClient;
import com.allexamnotes.libdroid.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsRepository {

    private static PostsRepository postsRepository;
    private ApiInterface apiInterface;
    private Context context;


    public static PostsRepository getInstance(Context context){
        if (postsRepository ==null){
            postsRepository = new PostsRepository();
        }
        return postsRepository;
    }

    public PostsRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<PostResponse> getNews(int page, String search, String category, String tag, String author,int perpage) {
        final MutableLiveData<PostResponse> posts = new MutableLiveData<>();
        if(perpage==0)
            perpage = 10;
        Call<List<Posts>> listCall = apiInterface.getNewsList(page, search, category, tag, author,"wordroid",0,perpage);
        Log.e("Making Request", listCall.request().url().toString());
        listCall.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    Log.d("Making Request",response.body().size()+" posts loaded successfully");
                    try{
                        int totalPostPages = Integer.parseInt(response.headers().get("x-wp-totalpages"));
                        posts.postValue(new PostResponse(response.body(),totalPostPages));
                    }catch (ClassCastException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Log.d("Making Request","Error: "+t.getLocalizedMessage());
                posts.postValue(null);
            }
        });
        return posts;
    }

    public LiveData<PostResponse> getOfflinePosts(Context context){
        final MutableLiveData<PostResponse> posts = new MutableLiveData<>();
        PostsDao postsDao = PostsDatabase.getAppDatabase(context).postsDao();
        PostResponse postResponse = new PostResponse(postsDao.getAllPosts().getValue(),1);
        posts.postValue(postResponse);
        return posts;
    }

    /*static class savePostsOffline extends AsyncTask<List<Posts>,Void,Void>{
        @Override
        protected Void doInBackground(List<Posts>... lists) {
            PostsDao postsDao = PostsDatabase.getAppDatabase(context).postsDao();
            return null;
        }
    }*/


}
