package com.daily.timer.dailytimer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daily.timer.dailytimer.adapters.EditableItemAdapter;
import com.daily.timer.dailytimer.adapters.IAdapterItem;
import com.daily.timer.dailytimer.adapters.ItemAdapter;
import com.daily.timer.dailytimer.adapters.MultiTypeAdapter;
import com.daily.timer.dailytimer.data.AppDatabase;
import com.daily.timer.dailytimer.presentors.TimerPresentor;
import com.daily.timer.dailytimer.lifecycle.ListKeeper;
import com.daily.timer.dailytimer.views.BaseList;
import com.daily.timer.dailytimer.views.BaseView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseView, BaseList {

    protected ListView mListViewTimers;
    protected ListKeeper mListKeeper;
    protected TimerPresentor mPresentor;
    private ViewState mViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list );
        mListViewTimers = findViewById( R.id.list_layout);
        bindViews();

        AppDatabase db = AppDatabase.getDatabase( this );
        mPresentor = new TimerPresentor( this, db);
        mViewState = ViewState.Normal;

        mListKeeper = new ListKeeper(this, mPresentor, this);
        getLifecycle().addObserver(mListKeeper);



    }

    private void bindViews() {

        FloatingActionButton addButton = findViewById( R.id.floating_add );
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewState == ViewState.Normal) {
                    mViewState = ViewState.New;
                    updateView();
                }
            }
        });

        mListViewTimers.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mViewState == ViewState.Normal){
                    Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                    intent.putExtra(TimerActivity.ITEM_POSITION, position);
                    startActivity(intent);
                }
            }
        } );
    }

    @Override
    public void updateView() {
        List<IAdapterItem> adapters = new ArrayList<>( );
        for (int i = 0; i< mPresentor.size(); i++){
            adapters.add(new ItemAdapter());
        }
        if (mViewState == ViewState.New) {
            adapters.add(new EditableItemAdapter());
        } else if (mViewState == ViewState.Edit){

        }
        MultiTypeAdapter multiAdapter = new MultiTypeAdapter( this,this, mPresentor, adapters);
        mListViewTimers.setAdapter( multiAdapter );
    }

    @Override
    public void setViewState(ViewState state){
        mViewState = state;
    }

    @Override
    public ViewState getViewState(){
        return mViewState;
    }
}
