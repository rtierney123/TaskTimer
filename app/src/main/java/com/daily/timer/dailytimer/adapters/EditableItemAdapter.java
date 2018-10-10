package com.daily.timer.dailytimer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.daily.timer.dailytimer.R;

public class EditableItemAdapter implements IAdapterItem {

    @Override
    public MultiTypeAdapter.RowType getViewType() {
        return MultiTypeAdapter.RowType.ITEM_EDIT;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, Context context) {
        View view = convertView;
        view = inflater.inflate( R.layout.item_editable, null );
        return view;
    }
}
