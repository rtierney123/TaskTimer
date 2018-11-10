package com.daily.timer.dailytimer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.daily.timer.dailytimer.data.DatabaseColumns;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "times")
public class Timer implements DatabaseColumns {
    @ColumnInfo(name = ID_COLUMN)
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = TITLE_COLUMN)
    private String mTitle;
    @ColumnInfo(name = CURRENT_COLUMN)
    private boolean mCurrent;
    @ColumnInfo(name = DATE_COLUMN)
    @TypeConverters(DateConverter.class)
    private Date mDate;
    @Embedded
    private Time mTime;


    public Timer(String title, boolean current, Date date, Time time) {
        mTitle = title;
        mCurrent = current;
        mDate = date;
        mTime = time;
    }

    public int getId(){
        return mId;
    }

    public void setId(int id){
        this.mId = id;
    }

    public String getTitle(){return mTitle;}

    public void setTitle(String title) {mTitle = title;}

    public boolean getCurrent(){return mCurrent;}

    public void setCurrent(boolean current){mCurrent = current;}

    public Date getDate(){return mDate;}

    public void setDate(Date date){mDate = date;}

    public Time getTime(){return mTime;}

    public void setTime(Time time){mTime = time;}

    public String getDateColumnName(){
        return DATE_COLUMN;
    }

    public String getCurrentColumnName(){
        return CURRENT_COLUMN;
    }


}
