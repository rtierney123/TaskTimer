package com.daily.timer.dailytimer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.daily.timer.dailytimer.models.Timer;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TimerDao extends DatabaseColumns {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertTimer(Timer timer);

    @Update
    void updateTimer(Timer timer);

    @Delete
    void deleteTimer(Timer timer);


    @Query("SELECT * FROM times WHERE "+CURRENT_COLUMN +"= 1 AND "+
            DATE_COLUMN+" = :date")
    Maybe<List<Timer>> loadActive(Date date);

    @Query("SELECT * FROM times WHERE "+CURRENT_COLUMN+" = 1 AND "+
            "NOT("+DATE_COLUMN+" = :date)")
    Maybe<List<Timer>> loadOld(Date date);

    @Query("SELECT * FROM times")
    Maybe<List<Timer>> loadAll();

}
