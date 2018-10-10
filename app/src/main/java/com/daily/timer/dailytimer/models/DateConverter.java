package com.daily.timer.dailytimer.models;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static SimpleDateFormat mDateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    @TypeConverter
    public static Date toDate(String value) {
        try{
            return value == null ? null : mDateFormatter.parse(value);
        } catch (ParseException ex){
            Log.e("Date format", "Data parsing error.");
        }
        return null;
    }

    @TypeConverter
    public static String toLong(Date value) {
        return value == null ? null : mDateFormatter.format(value);
    }
}
