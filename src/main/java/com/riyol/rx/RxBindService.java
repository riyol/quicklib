package com.riyol.rx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;

public class RxBindService extends Single<IBinder> {
    private static final String TAG = "RxBindService";
    private final Context app;
    private final Intent intent;
    private ServiceConnectionCallback callback;

    public static RxBindService create(final Context app, final Intent intent) {
        return new RxBindService(app, intent);
    }

    private RxBindService(Context app, Intent intent) {
        ObjectHelper.requireNonNull(app, "app is null");
        ObjectHelper.requireNonNull(intent, "intent is null");
        this.app = app;
        this.intent = intent;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super IBinder> observer) {
        if (callback == null) {
            callback = new ServiceConnectionCallback(app, observer);
        }
        observer.onSubscribe(callback);
        callback.setCanBind(app.bindService(intent, callback, Context.BIND_AUTO_CREATE));
    }


    private static final class ServiceConnectionCallback implements ServiceConnection, Disposable {
        private final Context app;
        private final SingleObserver<? super IBinder> observer;
        private boolean canBind;
        private volatile boolean disposed;

        public ServiceConnectionCallback(Context app, SingleObserver<? super IBinder> observer) {
            this.app = app;
            this.observer = observer;
        }

        @Override
        public void dispose() {
            disposed = true;
            if (canBind) {
                app.unbindService(this);
                canBind = false;
            }
        }

        public void setCanBind(boolean canBind) {
            this.canBind = canBind;
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.e(TAG, "onBindingDied:" + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w(TAG, "onServiceConnected:" + name);
            observer.onSuccess(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected:" + name);
        }
    }
}
