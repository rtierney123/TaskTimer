package com.daily.timer.dailytimer.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.daily.timer.dailytimer.data.DatabaseColumns;

import java.util.Date;

@Entity(tableName = "points")
public class PointItem implements DatabaseColumns {

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
    @ColumnInfo(name = POINTS_COLUMN)
    private int mPoints;

    public PointItem(String title, boolean current, Date date, int points) {
        mTitle = title;
        mCurrent = current;
        mDate = date;
        mPoints = points;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    public Date getDate() {
        return mDate;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setId(@NonNull int mId) {
        this.mId = mId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setCurrent(boolean mCurrent) {
        this.mCurrent = mCurrent;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setPoints(int mPoints) {
        this.mPoints = mPoints;
    }
}
