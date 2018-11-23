package com.riyol.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BindingFragment<VB extends ViewDataBinding>
        extends BaseFragment {
    protected VB viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(
                inflater, layoutRes(), container, false);
        viewBinding.setLifecycleOwner(this);
        performDataBinding(savedInstanceState);
        return viewBinding.getRoot();
    }

    private void performDataBinding(Bundle savedState) {
        performNext(savedState);
        viewBinding.executePendingBindings();
    }

    protected void performNext(Bundle savedState) {
    }

    @LayoutRes
    protected abstract int layoutRes();
}