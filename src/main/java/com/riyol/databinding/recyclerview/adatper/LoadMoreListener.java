package com.riyol.databinding.recyclerview.adatper;

public interface LoadMoreListener {
    boolean canLoadMore();

    void loadMore();

    boolean showEnd();
}
