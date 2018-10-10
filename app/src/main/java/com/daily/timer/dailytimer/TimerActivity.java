package com.daily.timer.dailytimer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daily.timer.dailytimer.data.AppDatabase;
import com.daily.timer.dailytimer.presentors.TimerPresentor;
import com.daily.timer.dailytimer.models.Item;
import com.daily.timer.dailytimer.views.BaseView;

public class TimerActivity extends AppCompatActivity implements BaseView{

    public static final String ITEM_POSITION = "com.prac.data.notekeeper.ITEM_POSITION";

    //position from the ListView item clicked
    private int mPosition;
    private TimerPresentor mPresentor;
    private Item mItem;
    private TextView mTimeText;
    //whether to continue the timer or not
    private boolean mResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_timer );

        Intent intent = getIntent();

        AppDatabase db = AppDatabase.getDatabase( this );
        mPresentor = new TimerPresentor(this, db);

        mPosition = intent.getIntExtra( ITEM_POSITION, 0 );
        mItem = mPresentor.getItems().get(mPosition);
        mTimeText = findViewById( R.id.timer_time );
        displayItem();

        mResume = false;


        //button to start and stop the timer
        final Button button = findViewById( R.id.button_timer );
        button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                int buttonColor;
                if (button.getText().equals( "Start" )) {
                    button.setText( "Stop" );
                    buttonColor = ResourcesCompat.getColor(getResources(), R.color.stopColor, null);
                    mResume = true;
                } else {
                    button.setText( "Start" );
                    buttonColor = ResourcesCompat.getColor(getResources(), R.color.startColor, null);
                    mResume = false;
                }
                button.setBackgroundTintList( ColorStateList.valueOf(buttonColor ) );

            }
        } );


        //thread keeps track of timer state
        Thread th = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep( 1000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mResume) {
                                    mItem.getTime().addSecond();
                                    mTimeText.setText( mItem.getTime().toString() );
                                    mPresentor.updateItem( mPosition, mItem );
                                }

                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        };
        th.start();

    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
        //mPresentor.close();
    }

    //shows new mTime value
    private void displayItem() {
        setTitle( mItem.getTitle() );
        mTimeText.setText( mItem.getTime().toString() );
    }

    @Override
    public void updateView() {
        mTimeText.setText( mItem.getTime().toString() );
    }

}
