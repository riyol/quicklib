package com.riyol.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.riyol.base.dialog.DefaultLoadingDialog;
import com.riyol.function.Optional;
import com.riyol.permission.PermissionHelper;


/**
 * Created by Administrator on 2018/6/8 0008.
 */
public abstract class BaseFragment extends Fragment {
    private Activity activity;
    private DefaultLoadingDialog loadingDialog;

    private IToolbarCallback toolbarCallback;
    private ITabLayoutCallback tabLayoutCallback;
    private IAppbarLayoutCallback appbarLayoutCallback;
    private PermissionHelper permissionHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            loadingDialog.dismissAllowingStateLoss();
            loadingDialog = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        toolbarCallback = FragmentUtil.getCallback(this, IToolbarCallback.class);
        appbarLayoutCallback = FragmentUtil.getCallback(this, IAppbarLayoutCallback.class);
        tabLayoutCallback = FragmentUtil.getCallback(this, ITabLayoutCallback.class);
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    public void setToolbarTitle(int strId) {
        Optional.ofNullable(toolbarCallback).ifPresent(callback -> callback.setToolbarTitle(strId));
    }

    public void setToolbarTitle(CharSequence title) {
        Optional.ofNullable(toolbarCallback).ifPresent(callback -> callback.setToolbarTitle(title));
    }

    public Toolbar getToolbar() {
        if (toolbarCallback != null) {
            return toolbarCallback.getToolbar();
        }
        return null;
    }

    public TabLayout getTabLayout() {
        if (tabLayoutCallback != null) {
            return tabLayoutCallback.getTabLayout();
        }
        return null;
    }

    public AppBarLayout getAppBarLayout() {
        if (appbarLayoutCallback != null) {
            return appbarLayoutCallback.getAppBarLayout();
        }
        return null;
    }

    protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new DefaultLoadingDialog();
        }

        if (!loadingDialog.isVisible()) {
            loadingDialog.show(this);
        }
    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismissAllowingStateLoss();
        }
    }

    public void hideKeyboard() {
        View view = this.getView().findFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void startActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this.getActivity(), cls);
        startActivity(intent);
    }

    /**
     * Runtime permiss
     **/
    protected final PermissionHelper permissionHelper() {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper();
        }
        return permissionHelper;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (permissionHelper != null) {
            if (permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * end permission
     */

}
