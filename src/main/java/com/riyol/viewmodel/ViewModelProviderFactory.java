package com.riyol.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.riyol.function.Objects;
import com.riyol.function.Supplier;

public class ViewModelProviderFactory<VM extends ViewModel> implements ViewModelProvider.Factory {
    private Supplier<VM> vmSupplier;

    private ViewModelProviderFactory(Supplier<VM> vmSupplier) {
        this.vmSupplier = Objects.requireNonNull(vmSupplier, "ViewModel supplier is null");
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        VM vm = vmSupplier.get();
        if (modelClass.isAssignableFrom(vm.getClass())) {
            return (T) vm;
        }
        throw new IllegalArgumentException("Unknown class name");
    }

    public static <VM extends ViewModel> ViewModelProviderFactory<VM> create(Supplier<VM> supplier) {
        return new ViewModelProviderFactory<>(supplier);
    }

}
