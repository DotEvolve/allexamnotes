package com.itsanubhav.libdroid.database.convertors;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsanubhav.libdroid.model.post.CategoriesDetailItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CategoryConvertors {
    @TypeConverter
    public static List<CategoriesDetailItem> fromString(String value) {
        Type listType = new TypeToken<ArrayList<CategoriesDetailItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<CategoriesDetailItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
