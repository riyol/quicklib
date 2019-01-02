package com.riyol.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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
        super.onCreateView(inflater, container, savedInstanceState);
        if (viewBinding == null) {
            viewBinding = DataBindingUtil.inflate(
                    inflater, layoutRes(), container, false);
            viewBinding.setLifecycleOwner(this);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        performDataBinding(savedInstanceState);
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