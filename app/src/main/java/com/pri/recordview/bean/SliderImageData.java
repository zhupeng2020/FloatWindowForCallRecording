package com.pri.recordview.bean;

import android.graphics.drawable.Drawable;

public class SliderImageData {
    private Drawable appIcon;

    public SliderImageData(Drawable drawable) {
        appIcon = drawable;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
