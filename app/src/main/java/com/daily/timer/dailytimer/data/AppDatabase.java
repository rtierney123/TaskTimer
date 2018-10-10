package com.daily.timer.dailytimer.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.daily.timer.dailytimer.models.DateConverter;
import com.daily.timer.dailytimer.models.Item;

@Database(entities = {Item.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class, ListConverter.class})
public abstract class  AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "roomtimer.db";
    private static final int DATABASE_VERSION = 8;
    private static final String TABLE_NAME = "times";

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }



    public abstract TimerDao daoAccess();


    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
