package com.allexamnotes.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.allexamnotes.libdroid.model.playlistvideos.MPlaylistVideos;
import com.allexamnotes.libdroid.network.ApiClient;
import com.allexamnotes.libdroid.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayListVideosRepository {

    private static final String url = "https://www.googleapis.com/youtube/v3/playlistItems";

    private static PlayListVideosRepository videosRepository;
    private ApiInterface apiInterface;

    public static PlayListVideosRepository getInstance(){
        if (videosRepository==null){
            videosRepository = new PlayListVideosRepository();
        }
        return videosRepository;
    }

    public PlayListVideosRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<MPlaylistVideos> getVideos(String key, String chanelId, String pageToken, String playListId){
        final MutableLiveData<MPlaylistVideos> videoList = new MutableLiveData<>();
        Call<MPlaylistVideos> call = apiInterface.getPlaylistVideos(url,key,chanelId,pageToken,playListId,"snippet,contentDetails",25);
        Log.e("Making Request","Url: "+call.request().url());
        call.enqueue(new Callback<MPlaylistVideos>() {
            @Override
            public void onResponse(Call<MPlaylistVideos> call, Response<MPlaylistVideos> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    videoList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MPlaylistVideos> call, Throwable t) {
                videoList.postValue(null);
            }
        });
        return videoList;
    }

}
