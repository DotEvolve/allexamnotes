package com.allexamnotes.libdroid.database.convertors;

import androidx.room.TypeConverter;

import com.allexamnotes.libdroid.model.post.Title;

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
