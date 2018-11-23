package com.riyol.rx;

import com.riyol.quicklib.BuildConfig;
import com.riyol.viewmodel.BaseViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SingleLoadingObserver<T> implements SingleObserver<T> {
    protected BaseViewModel viewModel;

    private Disposable disposable;

    public SingleLoadingObserver(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        viewModel.setLoading(true);
        viewModel.addDisposable(d);
    }

    @Override
    public void onSuccess(T result) {
        clearDisposable();
        viewModel.setLoading(false);
//        viewModel.checkDisposableList();
    }

    @Override
    public void onError(Throwable e) {
        clearDisposable();
        viewModel.setLoading(false);
        viewModel.setThrowable(e);
//        viewModel.checkDisposableList();

        if(BuildConfig.DEBUG){
            e.printStackTrace();
        }
    }

    private void clearDisposable() {
        viewModel.removeDisposable(disposable);
        disposable = null;
    }
}
