package com.titan.mobile.arcruntime.util;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public final class FutureUtil {

    public static <T> T getT(Future future) {
        try {
            T t = (T) future.get();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            Throwable throwable = e.getCause();
            throwable.printStackTrace();
            return null;
        }
    }

    public static Boolean getVoid(Future future) throws Exception {
        boolean flag = false;
        try {
            future.get(1,TimeUnit.SECONDS);
            future.get();
            if(future.isDone()){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            throw new Exception(e);
        } finally {
            return flag;
        }
    }

    public static class WaitingForYou<V> extends FutureTask<V> {

        public WaitingForYou() {
            super(() -> {
                throw new IllegalStateException("this should never be called");
            });
        }

        public void setResult(V v) {
            set(v);
        }

        @Override
        public V get() {
            try {
                return super.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public V get(long timeout, TimeUnit unit) {
            try {
                return super.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
