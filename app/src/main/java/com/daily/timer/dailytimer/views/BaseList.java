package com.daily.timer.dailytimer.views;

public interface BaseList {
    enum ViewState
    {
        Edit, New, Normal;
    }
    public void setViewState(ViewState state);

    public ViewState getViewState();
}
