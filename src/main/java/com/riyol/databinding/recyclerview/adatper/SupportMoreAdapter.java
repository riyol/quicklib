package com.riyol.databinding.recyclerview.adatper;

import android.databinding.ObservableInt;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import com.riyol.quicklib.BR;
import java.util.List;

import static com.riyol.databinding.recyclerview.adatper.LoadMoreState.END;
import static com.riyol.databinding.recyclerview.adatper.LoadMoreState.HIDE;
import static com.riyol.databinding.recyclerview.adatper.LoadMoreState.LOADING;


public abstract class SupportMoreAdapter<V extends ViewDataBinding> extends ListBindingAdapter<V> {

    private ObservableInt state = new ObservableInt(HIDE);

    private LoadMoreListener listener;

    public SupportMoreAdapter(LoadMoreListener listener) {
        this.listener = listener;
    }

    public SupportMoreAdapter(List<?> items, LoadMoreListener listener) {
        super(items);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<V> holder, int position) {
        if (items.size() > 0 && position == items.size()) {
            holder.getBinding().setVariable(BR.viewModel, state);
            holder.getBinding().setVariable(BR.presenter, listener);
            if (listener.canLoadMore()) {
                state.set(LOADING);
                listener.loadMore();
            } else {
                if (listener.showEnd()) {
                    state.set(END);
                } else {
                    state.set(HIDE);
                }
            }
            holder.getBinding().executePendingBindings();

        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (items.size() > 0) {
            return items.size() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.size() > 0 && position >= items.size()) {
            return layoutBottomRes();
        } else {
            return super.getItemViewType(position);
        }
    }

    protected @LayoutRes
    abstract int layoutBottomRes();


    public void setState(int state) {
        this.state.set(state);
    }

}