package com.riyol.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riyol.base.fragment.IAppbarLayoutCallback;
import com.riyol.base.fragment.ITabLayoutCallback;
import com.riyol.base.fragment.IToolbarCallback;
import com.riyol.function.Objects;
import com.riyol.function.Optional;
import com.riyol.quicklib.R;


/**
 * Created by riyol on 2018/4/12.
 */

public abstract class BindingActivity<VB extends ViewDataBinding>
        extends BaseActivity
        implements IToolbarCallback,
        ITabLayoutCallback,
        IAppbarLayoutCallback {

    protected VB viewBinding;
    protected CoordinatorLayout coordinatorLayout;
    protected AppBarLayout appBarLayout;
    protected Toolbar toolbar;
    protected TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState;
        if (bundle == null) {
            bundle = getIntent().getExtras();
        }

        if (layoutRes() != 0) {
            if (containerId() == 0) {
                throw new IllegalArgumentException("Because layoutRes is not zero (0), so must supply valid " +
                        "containerId()");
            }
            ViewGroup rootView = findViewById(containerId());
            Objects.requireNonNull(rootView, "root view is null");

            LayoutInflater inflater = LayoutInflater.from(this);
            viewBinding = DataBindingUtil.inflate(inflater, bindingLayoutRes(), rootView, true);
        } else {
            viewBinding = DataBindingUtil.setContentView(this, bindingLayoutRes());
        }
        viewBinding.setLifecycleOwner(this);
        setupView(bundle);
        setupModel(bundle);
    }

    protected void setupModel(Bundle savedState) {
    }

    protected void setupView(Bundle savedState) {
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        appBarLayout = findViewById(R.id.app_bar_layout);
        tabLayout = findViewById(R.id.tab_layout);

        if (tabLayout != null) {
            tabLayout.setVisibility(View.GONE);
        }

        toolbar = findViewById(R.id.tool_bar);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(displayHomeAsUpEnable());
        ab.setDisplayShowTitleEnabled(false);
    }

    protected boolean displayHomeAsUpEnable() {
        return true;
    }

    @Override
    public void setToolbarTitle(int strId) {
        Optional.ofNullable(toolbar).ifPresent(t -> t.setTitle(strId));
    }

    @Override
    public void setToolbarTitle(CharSequence title) {
        Optional.ofNullable(toolbar).ifPresent(t -> t.setTitle(title));
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }


    protected int containerId() {
        return R.id.coordinator_layout;
    }

    @Override
    protected int layoutRes() {
        return 0;
    }

    @LayoutRes
    protected abstract int bindingLayoutRes();
}
