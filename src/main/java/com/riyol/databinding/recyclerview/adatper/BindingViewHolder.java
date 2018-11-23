package com.riyol.databinding.recyclerview.adatper;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    protected final T binding;

    public BindingViewHolder(T t) {
        super(t.getRoot());
        this.binding = t;
    }

    public T getBinding() {
        return binding;
    }
}
