package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Subscriber;

/* loaded from: classes2.dex */
public class AppendOnlyLinkedArrayList<T> {
    final int capacity;
    final Object[] head;
    int offset;
    Object[] tail;

    /* loaded from: classes2.dex */
    public interface NonThrowingPredicate<T> extends Predicate<T> {
        @Override // io.reactivex.functions.Predicate
        boolean test(T t);
    }

    public AppendOnlyLinkedArrayList(int i) {
        this.capacity = i;
        this.head = new Object[i + 1];
        this.tail = this.head;
    }

    public <U> boolean accept(Observer<? super U> observer) {
        int i;
        Object[] objArr = this.head;
        int i2 = this.capacity;
        while (true) {
            if (objArr == null) {
                return false;
            }
            while (i < i2) {
                Object[] objArr2 = objArr[i];
                i = (objArr2 == null || NotificationLite.acceptFull(objArr2, observer)) ? 0 : i + 1;
                objArr = objArr[i2];
            }
            objArr = objArr[i2];
        }
    }

    public <U> boolean accept(Subscriber<? super U> subscriber) {
        int i;
        Object[] objArr = this.head;
        int i2 = this.capacity;
        while (true) {
            if (objArr == null) {
                return false;
            }
            while (i < i2) {
                Object[] objArr2 = objArr[i];
                i = (objArr2 == null || NotificationLite.acceptFull(objArr2, subscriber)) ? 0 : i + 1;
                objArr = objArr[i2];
            }
            objArr = objArr[i2];
        }
    }

    public void add(T t) {
        int i = this.capacity;
        int i2 = this.offset;
        if (i2 == i) {
            Object[] objArr = new Object[i + 1];
            this.tail[i] = objArr;
            this.tail = objArr;
            i2 = 0;
        }
        this.tail[i2] = t;
        this.offset = i2 + 1;
    }

    public void forEachWhile(NonThrowingPredicate<? super T> nonThrowingPredicate) {
        int i;
        int i2 = this.capacity;
        for (Object[] objArr = this.head; objArr != null; objArr = (Object[]) objArr[i2]) {
            while (i < i2) {
                Object obj = objArr[i];
                i = (obj == null || nonThrowingPredicate.test(obj)) ? 0 : i + 1;
            }
        }
    }

    public <S> void forEachWhile(S s, BiPredicate<? super S, ? super T> biPredicate) throws Exception {
        Object[] objArr = this.head;
        int i = this.capacity;
        while (true) {
            for (int i2 = 0; i2 < i; i2++) {
                Object obj = objArr[i2];
                if (obj == null || biPredicate.test(s, obj)) {
                    return;
                }
            }
            objArr = (Object[]) objArr[i];
        }
    }

    public void setFirst(T t) {
        this.head[0] = t;
    }
}
