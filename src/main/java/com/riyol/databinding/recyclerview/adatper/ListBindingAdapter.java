package com.riyol.databinding.recyclerview.adatper;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.riyol.quicklib.BR;

import java.util.Collections;
import java.util.List;

public abstract class ListBindingAdapter<V extends ViewDataBinding> extends RecyclerView
        .Adapter<BindingViewHolder<V>> {
    protected List<?> items;

    public ListBindingAdapter() {
        this(Collections.emptyList());
    }

    public ListBindingAdapter(List<?> items) {
        this.items = items;
    }

    public void setItems(List<?> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public BindingViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        V binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return createBindingHolder(binding);
    }


    @Override
    public void onBindViewHolder(BindingViewHolder<V> holder, int position) {
        if (items.size() > 0 && position < items.size()) {
            holder.getBinding().setVariable(BR.viewModel, items.get(position));
            onBindingView((V) holder.getBinding(), position);
            holder.getBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        if (layoutEmptyRes() != 0 && items.isEmpty()) {
            return 1;
        }
        return items.size();
    }

    protected void onBindingView(V binding, int position) {
    }

    protected BindingViewHolder<V> createBindingHolder(V binding) {
        return new BindingViewHolder<>(binding);
    }

    @Override
    public int getItemViewType(int position) {

        if (items.isEmpty() && layoutEmptyRes() != 0) {
            return layoutEmptyRes();
        } else {
            return layoutRes();
        }
    }

    protected abstract @LayoutRes
    int layoutRes();

    protected @LayoutRes
    int layoutEmptyRes() {
        return 0;
    }
}
