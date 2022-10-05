package com.allexamnotes.libdroid.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.allexamnotes.libdroid.model.playlist.Playlist;
import com.allexamnotes.libdroid.network.ApiClient;
import com.allexamnotes.libdroid.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository {

    private static final String url = "https://www.googleapis.com/youtube/v3/playlists";

    private static PlaylistRepository playlistRepository;
    private ApiInterface apiInterface;

    public static PlaylistRepository getInstance(){
        if (playlistRepository==null){
            playlistRepository = new PlaylistRepository();
        }
        return playlistRepository;
    }

    public PlaylistRepository(){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<Playlist> getPlaylists(String key, String chanelId, String pageToken){
        final MutableLiveData<Playlist> videoList = new MutableLiveData<>();
        Call<Playlist> call = apiInterface.getPlaylists(url,key,chanelId,pageToken,"snippet,contentDetails",25);
        Log.e("Making Request","Url: "+call.request().url());
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    videoList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                    videoList.postValue(null);
            }
        });
        return videoList;
    }

}
