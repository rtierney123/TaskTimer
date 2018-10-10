package com.daily.timer.dailytimer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.Update;

import com.daily.timer.dailytimer.models.Item;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TimerDao extends DatabaseColumns {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertTimer(Item item);

    @Update
    void updateTimer(Item item);

    @Delete
    void deleteTimer(Item item);


    @Query("SELECT * FROM times WHERE "+CURRENT_COLUMN +"= 1 AND "+
            DATE_COLUMN+" = :date")
    Maybe<List<Item>> loadActive(Date date);

    @Query("SELECT * FROM times WHERE "+CURRENT_COLUMN+" = 1 AND "+
            "NOT("+DATE_COLUMN+" = :date)")
    Maybe<List<Item>> loadOld(Date date);

    @Query("SELECT * FROM times")
    Maybe<List<Item>> loadAll();

}
