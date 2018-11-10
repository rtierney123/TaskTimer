package com.daily.timer.dailytimer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daily.timer.dailytimer.adapters.EditableItemAdapter;
import com.daily.timer.dailytimer.adapters.IAdapterItem;
import com.daily.timer.dailytimer.adapters.ItemAdapter;
import com.daily.timer.dailytimer.adapters.MultiTypeAdapter;
import com.daily.timer.dailytimer.data.AppDatabase;
import com.daily.timer.dailytimer.lifecycle.ListKeeper;
import com.daily.timer.dailytimer.presentors.TimerPresentor;
import com.daily.timer.dailytimer.views.BaseList;
import com.daily.timer.dailytimer.views.BaseView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerListFragment extends Fragment implements BaseList, BaseView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected ListView mListViewTimers;

    private BaseList.ViewState mViewState;
    protected ListKeeper mListKeeper;
    protected TimerPresentor mPresentor;
    private Context mContext;
    private View mView;

    private OnFragmentInteractionListener mListener;

    public TimerListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerListActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerListFragment newInstance(String param1, String param2) {
        TimerListFragment fragment = new TimerListFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_timer_list, container, false );
        mContext = view.getContext();
        mView = view;

        mListViewTimers = view.findViewById( R.id.list_layout);
        bindViews();
        AppDatabase db = AppDatabase.getDatabase( mContext );
        mPresentor = new TimerPresentor( this, db);
        mListKeeper = new ListKeeper(mContext, mPresentor, this);
        getLifecycle().addObserver(mListKeeper);
        mViewState = BaseList.ViewState.Normal;
        return view;
    }

    private void bindViews() {

        FloatingActionButton addButton = mView.findViewById( R.id.floating_add );
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
                    Intent intent = new Intent(mContext, TimerActivity.class);
                    intent.putExtra(TimerActivity.ITEM_POSITION, position);
                    startActivity(intent);
                }
            }
        } );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setViewState(ViewState state){
        mViewState = state;
    }

    @Override
    public ViewState getViewState(){
        return mViewState;
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
        MultiTypeAdapter multiAdapter = new MultiTypeAdapter( mContext,this, mPresentor, adapters);
        mListViewTimers.setAdapter( multiAdapter );
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
