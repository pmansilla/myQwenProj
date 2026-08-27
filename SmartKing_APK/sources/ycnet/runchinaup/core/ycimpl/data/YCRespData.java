package ycnet.runchinaup.core.ycimpl.data;

/* loaded from: classes2.dex */
public class YCRespData<T> extends YCResp {
    private T data;

    public T getData() {
        return this.data;
    }

    public void setData(T t) {
        this.data = t;
    }

    @Override // ycnet.runchinaup.core.ycimpl.data.YCResp
    public String toString() {
        return "YCRespData{data=" + this.data + "} " + super.toString();
    }
}
