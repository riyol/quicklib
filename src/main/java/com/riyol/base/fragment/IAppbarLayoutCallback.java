package com.riyol.base.fragment;

import android.support.design.widget.AppBarLayout;

public interface IAppbarLayoutCallback {
    default AppBarLayout getAppBarLayout() {
        return null;
    }
}
