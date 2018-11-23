package com.riyol.base.fragment;

import android.support.design.widget.TabLayout;

public interface ITabLayoutCallback {
    default TabLayout getTabLayout() {
        return null;
    }
}
