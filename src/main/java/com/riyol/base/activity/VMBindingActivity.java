package com.riyol.base.activity;

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

public abstract class VMBindingActivity<VM extends BaseViewModel, VB extends ViewDataBinding> extends
        BindingActivity<VB> implements IViewModelProvider<VM> {
    protected VM viewModel;

    @Override
    protected void setUpModel(Bundle savedState) {
        super.setUpModel(savedState);

        Supplier<VM> supplier = provideViewModelSupplier();
        ViewModelProvider.Factory factory = supplier == null ? null : ViewModelProviderFactory.create(supplier);

        Class<VM> viewModelClass = provideViewModelClass();
        Objects.requireNonNull(viewModelClass, "viewModelClass is null");
        viewModel = ViewModelProviders.of(this, factory).get(viewModelClass);

        viewModel.getNavigateObservable().observe(this, nav -> onNavigate(nav));
        viewModel.getThrowableObservable().observe(this, throwable -> onThrowable(throwable));
        viewModel.getLoadStateObservable().observe(this, loading -> onLoadState(loading));
        viewBinding.setVariable(BR.viewModel, viewModel);
    }

    protected void onNavigate(Navigate nav) {
    }

    protected void onThrowable(Throwable throwable) {
    }

    protected void onLoadState(boolean loading) {
        if (loading) {
            showLoadingDialog();
        } else {
            hideLoadingDialog();
        }
    }
}
