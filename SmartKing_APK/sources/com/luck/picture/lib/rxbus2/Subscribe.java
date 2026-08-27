package com.luck.picture.lib.rxbus2;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface Subscribe {
    int code() default -1;

    ThreadMode threadMode() default ThreadMode.CURRENT_THREAD;
}
