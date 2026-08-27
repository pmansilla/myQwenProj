package ycnet.runchinaup.core.ycimpl.data;

import java.io.Serializable;
import java.util.List;

/* loaded from: classes2.dex */
public class YCRespListData<T> implements Serializable {
    private static final long serialVersionUID = -1687414762384392235L;
    private List<T> data;

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> list) {
        this.data = list;
    }

    public String toString() {
        return "YCRespListData{data=" + this.data + '}';
    }
}
