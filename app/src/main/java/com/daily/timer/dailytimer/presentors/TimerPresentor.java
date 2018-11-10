package com.daily.timer.dailytimer.presentors;

import android.util.Log;

import com.daily.timer.dailytimer.data.AppDatabase;
import com.daily.timer.dailytimer.data.TimerDao;
import com.daily.timer.dailytimer.models.Timer;
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
    public static final List<Timer> M_TIMERS = new ArrayList<>();
    private TimerDao mDao;

    public TimerPresentor(BaseView view, AppDatabase db){
        mView = view;
        mDatabase = db;
        mDao = mDatabase.timerDaoAccess();
    }

    public void updateData(){
        M_TIMERS.clear();
        loadActiveItems();
        loadOldItems();

    }

    public void addItem(Timer newTimer){
        M_TIMERS.add( newTimer );
        //mDao.insertTimer(newItem);
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.timerDaoAccess().insertTimer( newTimer );
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
        Timer timer = M_TIMERS.remove(position);
        //mDao.deleteTimer( item );
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.timerDaoAccess().deleteTimer( timer );
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

    public void updateItem(int position, Timer timer) {
        M_TIMERS.set(position, timer );
        //mDao.updateTimer(item);
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.timerDaoAccess().updateTimer( timer );
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

    public List<Timer> getItems() {
        return M_TIMERS;
    }

    public Timer getItem(int position) {return M_TIMERS.get(position);}

    public int size(){return M_TIMERS.size();}

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


    private Timer resetItem(Timer timer) {
        timer.setCurrent( false );
        //mDao.updateTimer( item );
        Completable.fromAction( new Action() {
            @Override
            public void run() throws Exception {
                mDatabase.timerDaoAccess().updateTimer( timer );
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
        Timer newTimer = new Timer( timer.getTitle(), true, getDateToday(), new Time() );
        addItem( newTimer );

        return newTimer;
    }

    private void loadActiveItems(){
        mDatabase.timerDaoAccess().loadActive(getDateToday()).subscribeOn( Schedulers.newThread()).observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Timer>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Timer> timers) throws Exception {
                        if (timers != null){
                            for (Timer timer : timers) {
                                M_TIMERS.add( timer );
                            }
                        }
                        mView.updateView();
                    }
                });
    }


    private void loadOldItems(){
        mDatabase.timerDaoAccess().loadOld(getDateToday()).subscribeOn( Schedulers.newThread()).observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Timer>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Timer> timers) throws Exception {
                        if (timers != null){
                            for (Timer timer : timers){
                                Timer newTimer = resetItem( timer );
                                M_TIMERS.add( newTimer );
                            }
                        }
                        mView.updateView();
                    }
                });
    }

    private void loadAll(List<Timer> totalList){
        mDatabase.timerDaoAccess().loadAll().subscribeOn( Schedulers.newThread()).observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Timer>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Timer> timers) throws Exception {
                        if (timers != null){
                            for (Timer timer : timers) {
                                M_TIMERS.add( timer );
                            }
                        }
                        mView.updateView();
                    }
                });
    }

    private void addListToList(List<Timer> bigList, List<Timer> littleList){
        for(Timer timer : littleList){
            bigList.add( timer );
        }
    }




}
