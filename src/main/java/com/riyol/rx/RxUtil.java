package com.riyol.rx;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {
    private static final int SUCCESS_CODE = 200;

//    public static <D, T extends QcResponse<D>> SingleSource<Boolean> singlePreProcess(T t, String command) {
//        if (SUCCESS_CODE != t.getCode()) {
//            return Single.error(new RxServiceException(command, t.getMessage(), t.getCode()));
//        }
//        return null;
//    }
//
//    public static <T extends QcResponse<?>> Exception handleRspCode(T t, String command) {
//        if (SUCCESS_CODE != t.getCode()) {
//            return new RxServiceException(command, t.getMessage(), t.getCode());
//        }
//        return null;
//    }

    public static <T> Observable<T> SCHEDULER(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Single<T> SCHEDULER(Single<T> single) {
        return single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
