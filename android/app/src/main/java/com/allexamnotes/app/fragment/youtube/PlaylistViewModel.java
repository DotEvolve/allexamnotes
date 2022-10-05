package com.allexamnotes.app.fragment.youtube;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allexamnotes.libdroid.model.playlist.Playlist;
import com.allexamnotes.libdroid.repo.PlaylistRepository;

public class PlaylistViewModel extends ViewModel {

    private PlaylistRepository playlistRepository;

    public PlaylistViewModel(){
        playlistRepository = PlaylistRepository.getInstance();
    }

    public LiveData<Playlist> getPlaylist(String key,String chanelId,String pageToken){
        return playlistRepository.getPlaylists(key,chanelId,pageToken);
    }

}
