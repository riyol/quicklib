package com.riyol.databinding.recyclerview.adatper;

public interface SupportMoreListener {
    int BOTTOM_LOADING = 1;
    int LOAD_ERROR = 2;
    int BOTTOM_END = 3;

    boolean canLoadMore();
    void loadMore();
}
