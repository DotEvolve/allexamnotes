package com.itsanubhav.libdroid.database.convertors;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.itsanubhav.libdroid.model.post.Content;

public class ContentConvertor {

    @TypeConverter
    public static Content fromString(String value) {
        Gson gson = new Gson();
        return gson.fromJson(value,Content.class);
    }

    @TypeConverter
    public static String fromContent(Content content) {
        Gson gson = new Gson();
        return gson.toJson(content);
    }

}