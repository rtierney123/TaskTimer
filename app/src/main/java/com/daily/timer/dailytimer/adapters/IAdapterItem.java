package com.daily.timer.dailytimer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public interface IAdapterItem {
    MultiTypeAdapter.RowType getViewType();
    View getView(LayoutInflater inflater, View convertView, Context context);
}
