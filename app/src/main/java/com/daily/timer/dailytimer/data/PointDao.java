package com.daily.timer.dailytimer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daily.timer.dailytimer.models.PointItem;
import com.daily.timer.dailytimer.models.Timer;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface PointDao extends DatabaseColumns{

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertPoint(PointItem point);

    @Update
    void updatePoint(PointItem point);

    @Delete
    void deletePoint(PointItem point);

    @Query("SELECT * FROM points")
    Maybe<List<PointItem>> loadAll();
}
