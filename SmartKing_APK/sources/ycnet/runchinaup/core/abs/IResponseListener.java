package ycnet.runchinaup.core.abs;

/* loaded from: classes2.dex */
public interface IResponseListener<T> {
    void onCancel();

    void onError(int i, String str);

    void onSuccess(T t);
}
