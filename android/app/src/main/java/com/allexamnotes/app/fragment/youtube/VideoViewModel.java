package com.allexamnotes.app.fragment.youtube;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allexamnotes.libdroid.model.playlistvideos.MPlaylistVideos;
import com.allexamnotes.libdroid.repo.PlayListVideosRepository;

public class VideoViewModel extends ViewModel {

    private PlayListVideosRepository videosRepository;

    public VideoViewModel(){
        videosRepository = PlayListVideosRepository.getInstance();
    }

    public LiveData<MPlaylistVideos> getVideos(String key,String chanelID,String pageToken,String playlistId){
        return videosRepository.getVideos(key, chanelID, pageToken, playlistId);
    }
}
