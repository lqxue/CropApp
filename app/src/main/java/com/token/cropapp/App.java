package com.token.cropapp;

import android.app.Application;
import android.util.Log;

import com.xxf.arch.XXF;
import com.xxf.arch.core.Logger;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("=======",Log.getStackTraceString(throwable));
            }
        });
        XXF.init(this, new Logger() {
            @Override
            public boolean isLoggable() {
                return true;
            }

            @Override
            public void d(String msg) {
                Log.d("=======",msg);
            }

            @Override
            public void d(String msg, Throwable tr) {
                Log.d("=======",msg);
            }

            @Override
            public void e(String msg) {
                Log.d("=======",msg);
            }

            @Override
            public void e(String msg, Throwable tr) {
                Log.d("=======",msg);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) throws Exception {
                ToastUtils.showToast(throwable.getMessage());
                return Log.getStackTraceString(throwable);
            }
        });
    }
}
