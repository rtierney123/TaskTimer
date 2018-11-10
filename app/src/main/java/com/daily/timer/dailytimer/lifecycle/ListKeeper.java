package com.daily.timer.dailytimer.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.daily.timer.dailytimer.presentors.TimerPresentor;
import com.daily.timer.dailytimer.views.BaseView;

public class ListKeeper implements LifecycleObserver {
    private static final String TAG = ListKeeper.class.getSimpleName();
    private final String mOsVersion;
    private Context mCon;
    private TimerPresentor mLoader;
    private BaseView mView;

    public ListKeeper(Context con, TimerPresentor loader, BaseView view) {
        mCon=con;
        mOsVersion = Build.VERSION.RELEASE;
        ((AppCompatActivity) con).getLifecycle().addObserver(this);
        mLoader = loader;
        mView = view;
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void trackOnCreate() {
        Log.d(TAG, "trackOnCreate() called");
        if (mLoader != null){
            mLoader.updateData();
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void trackOnDestroy() {
        Log.d(TAG, "trackOnDestroy() called");
        ((AppCompatActivity)mCon).getLifecycle().removeObserver(this);
        Lifecycle.State currentState=((AppCompatActivity)mCon).getLifecycle().getCurrentState();
        mCon=null;
        //mLoader.close();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void trackOnStart() {
        Log.d(TAG, "trackOnStart() called");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void trackOnResume() {
        Log.d(TAG, "trackOnResume() called");
        //mLoader.updateData();
        if (mView != null){
            mView.updateView();
        }


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void trackOnPause() {
        Log.d(TAG, "trackOnPause() called");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void trackOnStop() {
        Log.d(TAG, "trackOnStop() called");


    }

}