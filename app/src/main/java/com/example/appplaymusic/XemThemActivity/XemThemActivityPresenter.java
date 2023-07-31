package com.example.appplaymusic.XemThemActivity;

import android.content.Context;

public class XemThemActivityPresenter {
    private Context mContext;
    private XemThemActivityView view;
    public XemThemActivityPresenter(XemThemActivityView view,Context mContext){
        this.view=view;
        this.mContext=mContext;
    }
}
