package ycnet.runchinaup.core.abs;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* loaded from: classes2.dex */
public abstract class IDataParser {
    public static final int CODE_JSON_RESOVE_EXCEPTION = -9998;
    public static final int CODE_NET_UNAVAILABLE = -10000;
    public static final int CODE_NET_UNKNOWN_EXCEPTION = -9997;
    public static final int CODE_TIMEOUT = -9999;

    /* JADX INFO: Access modifiers changed from: protected */
    public Callback getCallBack() {
        return new Callback() { // from class: ycnet.runchinaup.core.abs.IDataParser.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                IDataParser.this.onFailure(call, iOException);
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                IDataParser.this.parser(call, response);
            }
        };
    }

    public abstract void onFailure(Call call, IOException iOException);

    public abstract void parser(Call call, Response response) throws IOException;
}
