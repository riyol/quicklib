package com.riyol.base.fragment;

import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;

public interface IToolbarCallback {
    void setToolbarTitle(CharSequence title);

    void setToolbarTitle(@StringRes int strId);

    default Toolbar getToolbar() {
        return null;
    }
}