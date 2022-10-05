package com.allexamnotes.libdroid.model.response;

import com.allexamnotes.libdroid.model.tag.Tag;

import java.util.List;

public class TagResponse {
    private int totalPages;
    private List<Tag> tags;

    public TagResponse(List<Tag> tags, int totalPages) {
        this.totalPages = totalPages;
        this.tags = tags;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
