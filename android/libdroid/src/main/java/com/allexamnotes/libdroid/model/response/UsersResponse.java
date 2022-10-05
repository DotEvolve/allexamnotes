package com.allexamnotes.libdroid.model.response;

import com.allexamnotes.libdroid.model.user.User;

import java.util.List;

public class UsersResponse {

    private int totalPages;
    private List<User> users;

    public UsersResponse(List<User> users, int totalPages) {
        this.totalPages = totalPages;
        this.users = users;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<User> getUsers() {
        return users;
    }

}
