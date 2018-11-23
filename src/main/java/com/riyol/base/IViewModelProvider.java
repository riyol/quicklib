package com.riyol.base;

import com.riyol.function.Supplier;
import com.riyol.viewmodel.BaseViewModel;

public interface IViewModelProvider<VM extends BaseViewModel> {
    default Supplier<VM> provideViewModelSupplier() {
        return null;
    }

    Class<VM> provideViewModelClass();
}
