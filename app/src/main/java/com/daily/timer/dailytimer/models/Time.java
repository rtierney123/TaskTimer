package com.daily.timer.dailytimer.models;

import android.arch.persistence.room.ColumnInfo;

import com.daily.timer.dailytimer.data.DatabaseColumns;

public class Time implements DatabaseColumns{
    @ColumnInfo(name = HOURS_COLUMN)
    private int mHours;
    @ColumnInfo(name = MINUTES_COLUMN)
    private int mMinutes;
    @ColumnInfo(name = SECONDS_COLUMN)
    private int mSeconds;

    public Time() {

    }

    @Override
    public String toString(){
        String hStr = Integer.toString(mHours);
        String mStr = Integer.toString( mMinutes );
        String sStr = Integer.toString(mSeconds);
        if (mHours < 10){
            hStr = "0"+hStr;
        }
        if (mMinutes < 10){
            mStr = "0"+mStr;
        }
        if (mSeconds < 10){
            sStr = "0"+sStr;
        }
        return hStr+":"+mStr+":"+sStr;
    }

    //add seconds and increments minutes and hours when needed
    public void addSecond(){
        if (mSeconds == 59) {
            mSeconds = 0;
            if (mMinutes == 59) {
                mMinutes = 0;
                mHours++;
            } else {
                mMinutes++;
            }
        } else {
            mSeconds++;
        }
    }

    public int getHours(){
        return mHours;
    }

    public void setHours(int hours){
        mHours = hours;
    }

    public int getMinutes(){
        return mMinutes;
    }

    public void setMinutes(int minutes){
        mMinutes = minutes;
    }

    public int getSeconds(){
        return mSeconds;
    }

    public void setSeconds(int seconds){
        mSeconds = seconds;
    }
}
