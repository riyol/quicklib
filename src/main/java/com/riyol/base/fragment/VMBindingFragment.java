package com.riyol.base.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.riyol.base.IViewModelProvider;
import com.riyol.quicklib.BR;
import com.riyol.function.Objects;
import com.riyol.function.Supplier;
import com.riyol.navigate.Navigate;
import com.riyol.viewmodel.BaseViewModel;
import com.riyol.viewmodel.ViewModelProviderFactory;

public abstract class VMBindingFragment<VM extends BaseViewModel, VB extends ViewDataBinding> extends
        BindingFragment<VB> implements IViewModelProvider<VM> {
    protected VM viewModel;

    @Override
    protected void performNext(Bundle savedState) {
        super.performNext(savedState);
        if (viewModel == null) {
            Supplier<VM> supplier = provideViewModelSupplier();
            ViewModelProvider.Factory factory = supplier == null ? null : ViewModelProviderFactory.create(supplier);
            Class<?> viewModelClass = getViewModelClass();//provideViewModelClass();
            Objects.requireNonNull(viewModelClass, "viewModelClass is null");

            boolean shareViewModel = shouldShareViewModel();
            ViewModelProvider viewModelProvider = shareViewModel ?
                    ViewModelProviders.of(getActivity()) : ViewModelProviders.of(this, factory);
            viewModel = viewModelProvider.get((Class<VM>)viewModelClass);
            if (!shareViewModel) {
                viewModel.getNavigateObservable().observe(this, nav -> onNavigate(nav));
                viewModel.getThrowableObservable().observe(this, throwable -> onThrowable(throwable));
                viewModel.getLoadStateObservable().observe(this, loading -> onLoadState(loading));
            }
            viewBinding.setVariable(BR.viewModel, viewModel);
        }
        setupView(savedState);
    }

    private Class<?> getViewModelClass() {
        if (shouldShareViewModel()) {
            Activity a = getActivity();
            if (a instanceof IViewModelProvider) {
                return ((IViewModelProvider) a).provideViewModelClass();
            }
        }
        return provideViewModelClass();
    }

    protected void setupView(Bundle savedState) {
    }

    protected void onNavigate(Navigate nav) {
    }

    protected void onThrowable(Throwable throwable) {
    }

    protected boolean shouldShareViewModel() {
        return false;
    }

    protected void onLoadState(boolean loading) {
        if (loading) {
            showLoadingDialog();
        } else {
            hideLoadingDialog();
        }
    }
}
