//package com.estudio.oss_dns_resolver_v1.data.utils;
//
//import android.util.Log;
//
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.Observer;
//import io.reactivex.rxjava3.disposables.Disposable;
//
//public abstract class BaseApiObserver<T> implements Observer<T> {
//
//    @Override
//    public void onSubscribe(@NonNull Disposable d) {
//        Log.d("BaseApiObserver", "BaseApiObserver: =  onSubscribe");
//    }
//
//    @Override
//    public void onNext(@NonNull T t) {
//        Log.d("BaseApiObserver", "BaseApiObserver: =  onNext");
//    }
//
//    @Override
//    public void onError(@NonNull Throwable e) {
//        Log.d("BaseApiObserver", "BaseApiObserver: =  onError: "+e.getLocalizedMessage());
//    }
//
//    @Override
//    public void onComplete() {
//        Log.d("BaseApiObserver", "BaseApiObserver: =  onComplete");
//    }
//}
