package ycnet.runchinaup.core.ycimpl.request;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import okhttp3.FormBody;

/* loaded from: classes2.dex */
public class YCReqParaObj {
    private static final SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String domainUrl;
    private HashMap<String, String> paraMap;
    private String reqNumber;
    private String url;

    private YCReqParaObj() {
        this(null, null);
    }

    private YCReqParaObj(String str, String str2) {
        this.paraMap = new HashMap<>();
        this.url = null;
        this.domainUrl = null;
        this.reqNumber = smp.format(Long.valueOf(System.currentTimeMillis())) + "_$_" + UUID.randomUUID().toString();
        this.paraMap.put(str, str2);
    }

    public static YCReqParaObj create() {
        return new YCReqParaObj();
    }

    public static YCReqParaObj create(String str, String str2) {
        return new YCReqParaObj(str, str2);
    }

    public YCReqParaObj addPara(String str, String str2) {
        this.paraMap.put(str, str2);
        return this;
    }

    public void clear() {
        if (this.paraMap == null || this.paraMap.size() <= 0) {
            return;
        }
        this.paraMap.clear();
    }

    public FormBody getFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : this.paraMap.entrySet()) {
            if (entry.getKey() != null && !TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    public String toString() {
        return "\n请求参数:YCReqParaObj{reqNumber='" + this.reqNumber + "', paraMap=" + this.paraMap + ", url='" + this.url + "', domainUrl='" + this.domainUrl + "'}";
    }

    public YCReqParaObj useDomain(String str) {
        this.domainUrl = str;
        return this;
    }

    public YCReqParaObj useUrl(String str) {
        this.url = str;
        return this;
    }
}
