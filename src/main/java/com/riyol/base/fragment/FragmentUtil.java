package com.riyol.base.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.riyol.base.IViewModelProvider;
import com.riyol.viewmodel.BaseViewModel;

public final class FragmentUtil {
    @Nullable
    public static <T> T getCallback(@NonNull Fragment fragment, @NonNull Class<T> callback) {
        if (callback.isInstance(fragment.getTargetFragment())) {
            //noinspection unchecked
            return (T) fragment.getTargetFragment();
        }
        if (fragment.getParentFragment() != null) {
            if (callback.isInstance(fragment.getParentFragment())) {
                //noinspection unchecked
                return (T) fragment.getParentFragment();
            } else if (callback.isInstance(fragment.getParentFragment().getParentFragment())) {
                //noinspection unchecked
                return (T) fragment.getParentFragment().getParentFragment();
            }
        }
        if (callback.isInstance(fragment.getActivity())) {
            //noinspection unchecked
            return (T) fragment.getActivity();
        }
        return null;
    }

    public static <T extends BaseViewModel> T getActivityViewModel(FragmentActivity a) {
        if (!(a instanceof IViewModelProvider)) {
            return null;
        }
        Class<T> clazz = ((IViewModelProvider<T>) a).provideViewModelClass();
        return ViewModelProviders.of(a).get(clazz);
    }

    private FragmentUtil() {
    }
}
