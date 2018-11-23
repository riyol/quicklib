package com.riyol.base.activity;

import android.databinding.ViewDataBinding;

import com.riyol.quicklib.R;

public abstract class BindingMaterialActivity<VB extends ViewDataBinding> extends BindingActivity<VB> {
    @Override
    protected int layoutRes() {
        return R.layout.default_material_layout;
    }
}
