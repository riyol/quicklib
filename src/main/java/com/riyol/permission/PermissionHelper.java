package com.riyol.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.riyol.function.Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermissionHelper {
    private static int REQUEST_CODE_FOR_ACTIVITY = 0;
    private static int REQUEST_CODE_FOR_FRAGMENT = 1;
    private GrantedCallback grantedCallback;
    private RationaleCallback rationaleCallback;
    private ProhibitCallback prohibitCallback;
    private DeniedCallback deniedCallback;

    private List<String> permissionList = Collections.EMPTY_LIST;

    private String[] permissionArray;

    public PermissionHelper needPermission(@NonNull String... permissions) {
        if (permissionList == Collections.EMPTY_LIST) {
            permissionList = new ArrayList<>();
        }
        permissionList.addAll(Arrays.asList(permissions));
        return this;
    }

    public PermissionHelper needPermission(@NonNull String[]... permissionGroups) {
        if (permissionList == Collections.EMPTY_LIST) {
            permissionList = new ArrayList<>();
        }
        for (String[] group : permissionGroups) {
            for (String permission : group) {
                permissionList.add(permission);
            }
        }
        return this;
    }

    public PermissionHelper onGranted(GrantedCallback callback) {
        this.grantedCallback = callback;
        return this;
    }

    public PermissionHelper onRationale(RationaleCallback callback) {
        this.rationaleCallback = callback;
        return this;
    }

    public PermissionHelper onProhibit(ProhibitCallback callback) {
        this.prohibitCallback = callback;
        return this;
    }

    public PermissionHelper onDenied(DeniedCallback callback) {
        this.deniedCallback = callback;
        return this;
    }

    private boolean interceptRequest(Activity activity) {
        String[] permissions = getPermissionArray();
        List<String> rationalePermissions = new ArrayList<>();
        boolean granted = true;
        for (String permission: permissions) {
            if (check(activity.getApplicationContext(), permission)) {
                continue;
            } else {
                granted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    rationalePermissions.add(permission);
                }
            }
        }

        if (granted) {
            executeGrantedCallback();
            return true;
        }

        if (rationalePermissions.size() > 0) {
            executeRationaleCallback(rationalePermissions);
            return true;
        }
        return false;
    }

    private String[] getPermissionArray() {
        if (permissionArray == null) {
            if (permissionList.isEmpty()) {
                throw new IllegalArgumentException("permission should not be empty");
            }
        }

        if (permissionList != Collections.EMPTY_LIST) {
            permissionArray = permissionList.toArray(new String[0]);
            permissionList = Collections.EMPTY_LIST;
        }
        return permissionArray;
    }

    public void request(@NonNull Activity activity) {
        if (interceptRequest(activity)) {
            return;
        }
        ActivityCompat.requestPermissions(activity, getPermissionArray(),
                REQUEST_CODE_FOR_ACTIVITY);
    }

    public void requestDisallowIntercept(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(activity, getPermissionArray(),
                REQUEST_CODE_FOR_ACTIVITY);
    }

    public void request(@NonNull Fragment fragment) {
        if (interceptRequest(fragment.getActivity())) {
            return;
        }
        fragment.requestPermissions(getPermissionArray(),
                REQUEST_CODE_FOR_FRAGMENT);
    }

    public void requestDisallowIntercept(@NonNull Fragment fragment) {
        fragment.requestPermissions(getPermissionArray(),
                REQUEST_CODE_FOR_FRAGMENT);
    }

    private void executeRationaleCallback(List<String> permissions) {
        if (rationaleCallback != null) {
            rationaleCallback.onPermissionRationale(permissions);
        }
    }

    private void executeDeniedCallback(List<String> permissions) {
        DeniedCallback callback = deniedCallback;
        onRequestCancel();
        if (callback != null) {
            callback.onPermissionDenied(permissions);
        }
    }

    private void executeProhibitCallback(List<String> permissions) {
        ProhibitCallback callback = prohibitCallback;
        onRequestCancel();
        if (callback != null) {
            callback.onPermissionProhibited(permissions);
        }
    }

    private void executeGrantedCallback() {
        GrantedCallback callback = grantedCallback;
        onRequestCancel();
        if (callback != null) {
            callback.onPermissionGranted();
        }
    }

    public boolean onRequestPermissionsResult(Fragment fragment, int requestCode, @NonNull String[] permissions,
                                              @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE_FOR_FRAGMENT) {
            return false;
        }
        onRequestPermissionsResult(fragment.getActivity(), permissions, grantResults);
        return true;
    }

    public boolean onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions,
                                              @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE_FOR_ACTIVITY) {
            return false;
        }
        onRequestPermissionsResult(activity, permissions, grantResults);
        return true;
    }

    public void onRequestCancel() {
        permissionList = Collections.EMPTY_LIST;
        permissionArray = null;
        grantedCallback = null;
        rationaleCallback = null;
        deniedCallback = null;
        prohibitCallback = null;
    }

    private void onRequestPermissionsResult(Activity activity, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        String[] allPermissions = getPermissionArray();
        List<String> deniedList = new ArrayList<>();
        List<String> prohibitList = new ArrayList<>();
        for (String permission : allPermissions) {
            if (!check(activity.getApplicationContext(), permission)) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    prohibitList.add(permission);
                } else {
                    deniedList.add(permission);
                }
            }
        }
        if (!prohibitList.isEmpty()) {
            executeProhibitCallback(prohibitList);
            return;
        }

        if (!deniedList.isEmpty()) {
            executeDeniedCallback(prohibitList);
            return;
        }
        executeGrantedCallback();
    }

    public static boolean check(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        Objects.requireNonNull(context, "context is null");

        if (permissions == null || permissions.length == 0) {
            return true;
        }

        Context safeContext = context.getApplicationContext();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(safeContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface RationaleCallback {
        void onPermissionRationale(List<String> permissions);
    }

    public interface ProhibitCallback {
        void onPermissionProhibited(List<String> permissions);
    }

    public interface DeniedCallback {
        void onPermissionDenied(List<String> permissions);
    }

    @FunctionalInterface
    public interface GrantedCallback {
        void onPermissionGranted();
    }

}
