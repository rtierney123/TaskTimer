package com.daily.timer.dailytimer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.daily.timer.dailytimer.R;
import com.daily.timer.dailytimer.models.Timer;
import com.daily.timer.dailytimer.presentors.TimerPresentor;
import com.daily.timer.dailytimer.models.Time;
import com.daily.timer.dailytimer.views.BaseList;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Timer> mTimers;
    private TimerPresentor mPresentor;
    private BaseList mList;
    private List<IAdapterItem> mAdapters;
    public enum RowType {
        ITEM_TIMER, ITEM_EDIT
    }

    public MultiTypeAdapter(Context context, BaseList list, TimerPresentor presentor, List<IAdapterItem> adapters ) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mPresentor = presentor;
        mTimers = (ArrayList<Timer>)presentor.getItems();
        mAdapters = adapters;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType().ordinal();
    }

    @Override
    public int getCount() {
        return mAdapters.size();
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        IAdapterItem adapter = mAdapters.get(position);
        View itemView = adapter.getView( mInflater, convertView, mContext);;
        if (adapter.getViewType() == RowType.ITEM_TIMER) {

            TextView textViewName = itemView.findViewById( R.id.item_title );
            TextView textViewTime = itemView.findViewById( R.id.item_time );
            final ImageButton settings = itemView.findViewById(R.id.more_settings);
            settings.setFocusable(false);
            settings.setFocusableInTouchMode(false);

            final Timer selectedTimer = mTimers.get(position);
            textViewName.setText( selectedTimer.getTitle() );
            textViewTime.setText( selectedTimer.getTime().toString() );

            final int itemPosition = position;
            if (mList.getViewState() == BaseList.ViewState.Normal) {
                settings.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu( mContext, settings );
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate( R.menu.activity_list_settings, popup.getMenu() );
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.delete:
                                        if (mList.getViewState() == BaseList.ViewState.Normal) {
                                            mPresentor.deleteItem( position );
                                        }
                                }
                                return true;
                            }

                        } );

                        popup.show();
                    }
                } );
            }
        } else {
            final TextView textViewName = itemView.findViewById( R.id.title_new);
            Button enterItem = itemView.findViewById(R.id.button_add );

            enterItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = textViewName.getText().toString();
                    Timer newTimer = new Timer(title, true, mPresentor.getDateToday(), new Time( ));
                    mList.setViewState( BaseList.ViewState.Normal);
                    mPresentor.addItem( newTimer );
                }
            });
        }

        return itemView;
    }

    @Override
    public IAdapterItem getItem(int position) {
        return mAdapters.get(position);
    }

}
