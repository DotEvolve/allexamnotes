package com.itsanubhav.wordroid4.fragment.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.itsanubhav.libdroid.model.response.UsersResponse;
import com.itsanubhav.libdroid.repo.UserRepository;

public class AuthorViewModel extends ViewModel {

    private UserRepository userRepository ;

    public AuthorViewModel(){
        userRepository = UserRepository.newInstance();
    }

    public LiveData<UsersResponse> getUsers(int page,String searchQuery){
        return userRepository.getUsers(page,searchQuery);
    }

}
