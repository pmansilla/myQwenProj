package ycnet.runchinaup.core.ycimpl.response;

import ycnet.runchinaup.core.abs.IResponseListener;
import ycnet.runchinaup.core.ycimpl.parser.YCErrCodeParser;
import ycnet.runchinaup.log.ycNetLog;

/* loaded from: classes2.dex */
public abstract class YCResponseListener<T> implements IResponseListener<T> {
    private YCErrCodeParser ycErrCodeParser = null;

    public void cfgYCErrCodeParser(YCErrCodeParser yCErrCodeParser) {
        this.ycErrCodeParser = yCErrCodeParser;
    }

    @Override // ycnet.runchinaup.core.abs.IResponseListener
    public void onCancel() {
    }

    @Override // ycnet.runchinaup.core.abs.IResponseListener
    public void onError(int i, String str) {
        if (this.ycErrCodeParser != null) {
            this.ycErrCodeParser.parser(Integer.valueOf(i), str);
        } else {
            ycNetLog.e("没有状态码解析器");
        }
    }

    public void onSuccessWithJson(String str) {
    }
}
