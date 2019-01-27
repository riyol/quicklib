package com.riyol.rx;

import com.riyol.quicklib.BuildConfig;
import com.riyol.viewmodel.BaseViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class SingleSimpleObserver<T> implements SingleObserver<T> {
    protected BaseViewModel viewModel;

    private Disposable disposable;

    private SimpleCallback<T> callback;
    private boolean needLoading;

    public static <T> SingleSimpleObserver<T> loading(BaseViewModel viewModel) {
        SingleSimpleObserver observer = new SingleSimpleObserver<T>(viewModel);
        observer.needLoading = true;
        return observer;
    }

    public static <T> SingleSimpleObserver<T> createLoading(BaseViewModel viewModel, SimpleCallback<T> callback) {
        SingleSimpleObserver observer = new SingleSimpleObserver<T>(viewModel);
        observer.needLoading = true;
        observer.callback = callback;
        return observer;
    }

    public static <T> SingleSimpleObserver<T> noneLoading(BaseViewModel viewModel) {
        SingleSimpleObserver observer = new SingleSimpleObserver<T>(viewModel);
        return observer;
    }

    public static <T> SingleSimpleObserver<T> createNoneLoading(BaseViewModel viewModel, SimpleCallback<T> callback) {
        SingleSimpleObserver observer = new SingleSimpleObserver<T>(viewModel);
        observer.callback = callback;
        return observer;
    }

    private SingleSimpleObserver(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @Override
    public final void onSubscribe(Disposable d) {
        disposable = d;
        if (needLoading) {
            viewModel.setLoading(true);
        }
        viewModel.addDisposable(d);
    }

    @Override
    public final void onSuccess(T result) {
        clearDisposable();
        if (needLoading) {
            viewModel.setLoading(false);
        }
        if (callback != null) {
            callback.onSuccess(result);
        }
//        viewModel.checkDisposableList();
    }

    @Override
    public final void onError(Throwable e) {
        clearDisposable();
        if (needLoading) {
            viewModel.setLoading(false);
        }
        viewModel.setThrowable(e);
        if (callback != null) {
            callback.onError(e);
        }
//        viewModel.checkDisposableList();

        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }

    private void clearDisposable() {
        viewModel.removeDisposable(disposable);
        disposable = null;
    }

    @FunctionalInterface
    public interface SimpleCallback<T> {
        void onSuccess(@NonNull T t);

        default void onError(@NonNull Throwable e) {
        }
    }
}
