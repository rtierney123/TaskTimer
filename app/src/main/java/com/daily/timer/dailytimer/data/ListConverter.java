package com.daily.timer.dailytimer.data;

import android.arch.persistence.room.TypeConverter;

import com.daily.timer.dailytimer.models.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Item> stringToItemList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Item>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someItemListToString(List<Item> someObjects) {
        return gson.toJson(someObjects);
    }

}
