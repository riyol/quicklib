package com.riyol.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.riyol.base.dialog.DefaultLoadingDialog;
import com.riyol.base.dialog.MaterialAlertDialog;
import com.riyol.function.Optional;
import com.riyol.permission.PermissionHelper;
import com.riyol.quicklib.R;
import com.riyol.utils.ToastUtil;

import java.util.List;


/**
 * Created by riyol on 2018/4/12.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static String TAG = "BaseActivity";

    private DefaultLoadingDialog loadingDialog;

    private PermissionHelper permissionHelper;

    protected static Intent makeActivityIntent(Context context, Class<? extends Activity> cls) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutRes() != 0) {
            setContentView(layoutRes());
        }
        debugLifecycleMethod("onCreate");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        debugLifecycleMethod("onWindowFocusChanged");
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        debugLifecycleMethod("onDetachedFromWindow");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        debugLifecycleMethod("onAttachedToWindow");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        debugLifecycleMethod("onNewIntent");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        debugLifecycleMethod("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        debugLifecycleMethod("onRestoreInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        debugLifecycleMethod("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugLifecycleMethod("onResume");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        debugLifecycleMethod("onPostResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        debugLifecycleMethod("onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        debugLifecycleMethod("onStart");
    }

    @Override
    protected void onStop() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
            loadingDialog.dismissAllowingStateLoss();
            loadingDialog = null;
        }
        super.onStop();
        debugLifecycleMethod("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        debugLifecycleMethod("onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    final protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    final protected void startActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    final protected void startActivityForClearTask(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    final protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = createLoadingDialog();
        }

        if (!loadingDialog.isVisible()) {
            loadingDialog.show(this);
        }
    }

    final protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismissAllowingStateLoss();
        }
    }

    final protected void showToast(@StringRes int strId) {
        showToast(getString(strId));
    }

    final protected void showToast(String message) {
        ToastUtil.showToast(getApplicationContext(), message);
    }

    protected DefaultLoadingDialog createLoadingDialog() {
        return new DefaultLoadingDialog();
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


    private MaterialAlertDialog rationaleDialog;
    protected PermissionHelper.RationaleCallback rationaleCallback = (permissions) -> {
        if (rationaleDialog != null) {
            return;
        }
        List<String> message = PermissionHelper.transformText(getApplicationContext(), permissions);
        rationaleDialog = MaterialAlertDialog.newBuilder(getApplicationContext())
                .setTitle(R.string.permission_rationale_title)
                .setMessage(String.format(getString(R.string.permission_rationale_message),
                        TextUtils.join("\n", message)))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (tag, which) ->
                        Optional.ofNullable(permissionHelper).ifPresent(h -> h.requestDisallowIntercept(this))
                )
                .build();
        rationaleDialog.show(this);

    };

    private MaterialAlertDialog deniedDialog;
    protected PermissionHelper.DeniedCallback deniedCallback = permissions -> {
        if (deniedDialog != null) {
            return;
        }
        List<String> message = PermissionHelper.transformText(getApplicationContext(), permissions);
        deniedDialog = MaterialAlertDialog.newBuilder(getApplicationContext())
                .setTitle(R.string.permission_denied_title)
                .setMessage(String.format(getString(R.string.permission_rationale_message),
                        TextUtils.join("\n", message)))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.permission_setting, (tag, which) -> {
//                    Optional.ofNullable(permissionHelper).ifPresent(h -> h.requestDisallowIntercept(this));
                    startActivity(PermissionHelper.defaultApi(getApplicationContext()));
                    finish();
                })
                .build();
        deniedDialog.show(this);
    };

    /**
     * end permission
     */

    @LayoutRes
    protected abstract int layoutRes();

    private String activityName;

    private void debugLifecycleMethod(String lifecycle) {

//        if (activityName == null) {
//            activityName = getClass().getSimpleName();
//        }
//
//        String postFix = " ";
//        if (lifecycle.equals("onPause")) {
//            postFix += ("Finishing:" + isFinishing());
//        }
//
//        Log.d(TAG, "[==== " + activityName + " ====]:" + lifecycle + postFix + "\n");
    }
}
