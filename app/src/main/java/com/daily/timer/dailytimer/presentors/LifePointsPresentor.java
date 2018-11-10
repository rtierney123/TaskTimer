package com.daily.timer.dailytimer.presentors;

import android.arch.persistence.room.Entity;
import android.util.Log;

import com.daily.timer.dailytimer.data.AppDatabase;
import com.daily.timer.dailytimer.data.PointDao;
import com.daily.timer.dailytimer.models.PointItem;
import com.daily.timer.dailytimer.models.Timer;
import com.daily.timer.dailytimer.views.BaseView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Entity(tableName = "points")
public class LifePointsPresentor {

    public BaseView mView;
    public AppDatabase mDatabase;
    public static final List<PointItem> m_Points= new ArrayList<>();
    private PointDao mDao;

    public LifePointsPresentor(BaseView view, AppDatabase db){
        mView = view;
        mDatabase = db;
        mDao = mDatabase.pointDaoAccess();
    }

    public void addItem(PointItem point){
        m_Points.add( point );
        //mDao.insertTimer(newItem);
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.pointDaoAccess().insertPoint( point );
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe( new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.d("Database", "Inserted Item");
            }

            @Override
            public void onError(Throwable e) {

                Log.e("Database", "Error inserting item");
            }
        });
        mView.updateView();
    }

    public void deleteItem(int position){
        PointItem point  = m_Points.remove(position);
        //mDao.deleteTimer( item );
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.pointDaoAccess().deletePoint( point );
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.d("Database", "Deleted Item");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Database", "Error deleting item");
            }
        });
        mView.updateView();
    }

    public void updateItem(int position, PointItem point) {
        m_Points.set(position, point );
        //mDao.updateTimer(item);
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.pointDaoAccess().updatePoint( point );
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.d("Database", "Updated Item");
            }

            @Override
            public void onError(Throwable e) {

                Log.e("Database", "Error updating item");
            }
        });
        mView.updateView();
    }

    public List<PointItem> getItems() {
        return m_Points;
    }

    public PointItem getItem(int position) {return m_Points.get(position);}

    public int size(){return m_Points.size();}

    public void close(){
        mDatabase.close();
    }
}
