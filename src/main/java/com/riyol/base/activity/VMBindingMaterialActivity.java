package com.riyol.base.activity;

import android.databinding.ViewDataBinding;

import com.riyol.quicklib.R;
import com.riyol.viewmodel.BaseViewModel;

public abstract class VMBindingMaterialActivity<VM extends BaseViewModel, VB extends ViewDataBinding>
        extends VMBindingActivity<VM, VB> {
    @Override
    protected int layoutRes() {
        return R.layout.default_material_layout;
    }
}
