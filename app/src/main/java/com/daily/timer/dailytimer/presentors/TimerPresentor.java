package com.daily.timer.dailytimer.presentors;

import android.util.Log;
import android.view.View;

import com.daily.timer.dailytimer.data.AppDatabase;
import com.daily.timer.dailytimer.data.TimerDao;
import com.daily.timer.dailytimer.models.Item;
import com.daily.timer.dailytimer.models.Time;
import com.daily.timer.dailytimer.views.BaseView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TimerPresentor {

    public BaseView mView;
    public AppDatabase mDatabase;
    public static final List<Item> mItems = new ArrayList<>();
    private TimerDao mDao;

    public TimerPresentor(BaseView view, AppDatabase db){
        mView = view;
        mDatabase = db;
        mDao = mDatabase.daoAccess();
    }

    public void updateData(){
        mItems.clear();
        loadActiveItems();
        loadOldItems();

    }

    public void addItem(Item newItem){
        mItems.add(newItem);
        //mDao.insertTimer(newItem);
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.daoAccess().insertTimer( newItem );
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
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
        Item item = mItems.remove(position);
        //mDao.deleteTimer( item );
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.daoAccess().deleteTimer( item );
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

    public void updateItem(int position, Item item) {
        mItems.set(position, item);
        //mDao.updateTimer(item);
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.daoAccess().updateTimer( item );
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

    public List<Item> getItems() {
        return mItems;
    }

    public Item getItem(int position) {return mItems.get(position);}

    public int size(){return mItems.size();}

    public void close(){
        mDatabase.close();
    }

    public static Date getDateToday(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = df.format(cal.getTime());
        try {
            return df.parse(dateStr);
        } catch (ParseException ex){
            Log.e("Presentor", "Error parsing date");
        }
        return null;
    }


    private Item resetItem(Item item) {
        item.setCurrent( false );
        //mDao.updateTimer( item );
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.daoAccess().updateTimer( item );
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
        Item newItem = new Item( item.getTitle(), true, getDateToday(), new Time() );
        addItem( newItem );

        return newItem;
    }

    private void loadActiveItems(){
        mDatabase.daoAccess().loadActive(getDateToday()).subscribeOn( Schedulers.newThread()).observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Item>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Item> items) throws Exception {
                        if (items != null){
                            for (Item item : items) {
                                mItems.add(item);
                            }
                        }
                        mView.updateView();
                    }
                });
    }


    private void loadOldItems(){
        mDatabase.daoAccess().loadOld(getDateToday()).subscribeOn( Schedulers.newThread()).observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Item>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Item> items) throws Exception {
                        if (items != null){
                            for (Item item : items){
                                Item newItem = resetItem( item );
                                mItems.add(newItem);
                            }
                        }
                        mView.updateView();
                    }
                });
    }

    private void loadAll(List<Item> totalList){
        mDatabase.daoAccess().loadAll().subscribeOn( Schedulers.newThread()).observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Item>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Item> items) throws Exception {
                        if (items != null){
                            for (Item item : items) {
                                mItems.add(item);
                            }
                        }
                        mView.updateView();
                    }
                });
    }

    private void addListToList(List<Item> bigList, List<Item> littleList){
        for(Item item : littleList){
            bigList.add(item);
        }
    }




}
