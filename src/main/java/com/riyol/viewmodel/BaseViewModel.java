package com.riyol.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.riyol.function.Optional;
import com.riyol.function.Supplier;
import com.riyol.navigate.ActionEnum;
import com.riyol.navigate.Navigate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by riyol on 2018/4/12.
 */

public abstract class BaseViewModel extends ViewModel {
    protected final Application application;
    private MutableLiveData<Throwable> throwable;

    private MutableLiveData<Navigate> navigate;

    private MutableLiveData<Boolean> loadState;

    private final List<Disposable> disposableList = new ArrayList<>();

    private final CompositeDisposable compositeDisposable;

    public BaseViewModel(Application application) {
        this.application = application;
        compositeDisposable = new CompositeDisposable();
    }

    public BaseViewModel() {
        this(null);
    }

    private Supplier<MutableLiveData<Throwable>> throwableSupplier = () -> {
        if (throwable == null) {
            throwable = new MutableLiveData<>();
        }
        return throwable;
    };

    private Supplier<MutableLiveData<Navigate>> navigateSupplier = () -> {
        if (navigate == null) {
            navigate = new MutableLiveData<>();
        }
        return navigate;
    };

    private Supplier<MutableLiveData<Boolean>> loadStateSupplier = () -> {
        if (loadState == null) {
            loadState = new MutableLiveData<>();
        }
        return loadState;
    };

//    public BaseViewModel() {
//        compositeDisposable = new CompositeDisposable();
//    }

    final public void addDisposable(Disposable disposable) {
        if (!disposable.isDisposed()) {
            compositeDisposable.add(disposable);
            disposableList.add(disposable);
        }
        checkDisposableList();
    }

    final public void removeDisposable(Disposable disposable) {
        compositeDisposable.remove(disposable);
        disposableList.remove(disposable);
    }

    final public void checkDisposableList() {
        Iterator<Disposable> it = disposableList.iterator();
        while (it.hasNext()) {
            Disposable d = it.next();
            if (d.isDisposed()) {
                compositeDisposable.remove(d);
                it.remove();
            }
        }
    }

    final public MutableLiveData<Throwable> getThrowableObservable() {
        return throwableSupplier.get();
    }

    final public void setThrowable(Throwable t) {
        Optional.ofNullable(throwable).ifPresent(data -> data.setValue(t));
        onThrowable(t);
    }

    protected void onThrowable(Throwable t){
        //Todo
    }

    final public MutableLiveData<Navigate> getNavigateObservable() {
        return navigateSupplier.get();
    }

    final public void setNavigate(Navigate nav) {
        Optional.ofNullable(this.navigate).ifPresent(data -> data.setValue(nav));
    }

    final public void setNavigate(ActionEnum action){
        setNavigate(Navigate.create(action));
    }

    final public MutableLiveData<Boolean> getLoadStateObservable() {
        return loadStateSupplier.get();
    }

    private boolean loading = false;

    final public void setLoading(boolean loading) {
        if (this.loading != loading) {
            this.loading = loading;
            Optional.ofNullable(this.loadState).ifPresent(data -> data.setValue(loading));
        }
    }

    @NonNull
    public <T extends Application> T getApplication() {
        //noinspection unchecked
        return (T) application;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposableList.clear();
        compositeDisposable.clear();
    }
}
