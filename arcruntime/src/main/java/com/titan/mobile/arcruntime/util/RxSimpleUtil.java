package com.titan.mobile.arcruntime.util;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zy on 2019/5/8.
 */

public final class RxSimpleUtil {

    private RxSimpleUtil() {
    }

    public static <T> void simple(@NonNull final ISimpleBack<T> iCallBack) {
        Observable.create(new RxSimpleSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                T t = iCallBack.run();
                if (t != null) emitter.onNext(t);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSimpleObserver<T>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void onNext(T t) {
                        iCallBack.success(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static <T> void common(@NonNull final IComBack<T> iCallBack) {
        iCallBack.ready();
        Observable.create(new RxSimpleSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                T t = iCallBack.run();
                emitter.onNext(t);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSimpleObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        iCallBack.success(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCallBack.fail(e);
                    }
                });
    }

    public interface IComBack<T> {
        public void ready();

        public T run();

        public void success(T t);

        public void fail(Throwable e);
    }

    public interface ISimpleBack<T> {

        public T run();

        public void success(T t);
    }
}
