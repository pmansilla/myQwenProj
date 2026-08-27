package com.luck.picture.lib.rxbus2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class SubscriberMethod {
    public int code;
    public Class<?> eventType;
    public Method method;
    public Object subscriber;
    public ThreadMode threadMode;

    public SubscriberMethod(Object obj, Method method, Class<?> cls, int i, ThreadMode threadMode) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = cls;
        this.subscriber = obj;
        this.code = i;
    }

    public void invoke(Object obj) {
        try {
            Class<?>[] parameterTypes = this.method.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length == 1) {
                this.method.invoke(this.subscriber, obj);
            } else if (parameterTypes == null || parameterTypes.length == 0) {
                this.method.invoke(this.subscriber, new Object[0]);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }
}
