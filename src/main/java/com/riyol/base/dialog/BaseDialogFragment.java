package com.riyol.base.dialog;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialogFragment;

public abstract class BaseDialogFragment extends AppCompatDialogFragment implements LifecycleObserver {
    private Lifecycle lifecycle;

    private void saveLifecycle(LifecycleOwner owner) {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
        }
        lifecycle = owner.getLifecycle();
        lifecycle.addObserver(this);
    }

    /**
     * Shows the dialog. The  won't be called.
     *
     * @param fragment the hosting fragment
     */
    public void show(Fragment fragment) {
        show(fragment, null);
    }

    public void showNow(Fragment fragment) {
        showNow(fragment, null);
    }

    /**
     * Shows the dialog. Results will be forwarded to the fragment supplied.
     * The tag can be used to identify the dialog in
     *
     * @param fragment the hosting fragment
     * @param tag      the dialogs tag
     */
    public void show(Fragment fragment, String tag) {
        setTargetFragment(fragment, -1);
        saveLifecycle(fragment);
        super.show(fragment.getFragmentManager(), tag);
    }

    public void showNow(Fragment fragment, String tag) {
        setTargetFragment(fragment, -1);
        saveLifecycle(fragment);
        super.showNow(fragment.getFragmentManager(), tag);
    }

    /**
     * Shows the dialog. The  won't be called.
     *
     * @param activity the hosting activity
     */
    public void show(FragmentActivity activity) {
        show(activity, null);
    }

    public void showNow(FragmentActivity activity) {
        showNow(activity, null);
    }

    /**
     * Shows the dialog. Results will be forwarded to the activity supplied.
     * The tag can be used to identify the dialog in {}
     *
     * @param activity the hosting activity
     * @param tag      the dialogs tag
     */
    public void show(FragmentActivity activity, String tag) {
        saveLifecycle(activity);
        super.show(activity.getSupportFragmentManager(), tag);
    }

    public void showNow(FragmentActivity activity, String tag) {
        saveLifecycle(activity);
        super.showNow(activity.getSupportFragmentManager(), tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
        }
        super.onDismiss(dialog);
    }
}
