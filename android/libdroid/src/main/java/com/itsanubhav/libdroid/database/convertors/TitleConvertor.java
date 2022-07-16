package com.itsanubhav.libdroid.database.convertors;

import androidx.room.TypeConverter;

import com.itsanubhav.libdroid.model.post.Title;

public class TitleConvertor {
    @TypeConverter
    public static Title fromString(String value) {
        Title title = new Title();
        title.setRendered(value);
        return title;
    }

    @TypeConverter
    public static String fromTitle(Title title) {
        return title.getRendered();
    }
}
