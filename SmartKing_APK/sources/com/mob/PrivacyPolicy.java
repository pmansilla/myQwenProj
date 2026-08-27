package com.mob;

import android.text.TextUtils;
import android.util.Base64;
import com.mob.tools.MobLog;
import com.mob.tools.proguard.EverythingKeeper;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.DeviceHelper;
import java.io.Serializable;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PrivacyPolicy implements EverythingKeeper, Serializable {
    private String content;
    private int ppVersion;
    private long timestamp;
    private String title;

    /* loaded from: classes.dex */
    public interface OnPolicyListener extends PublicMemberKeeper {
        void onComplete(PrivacyPolicy privacyPolicy);

        void onFailure(Throwable th);
    }

    public PrivacyPolicy() {
    }

    public PrivacyPolicy(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            setTimestamp(jSONObject.optLong("timestamp"));
            setTitle(decrypt(jSONObject.optString("title")));
            setContent(decrypt(jSONObject.optString("content")));
            String decrypt = decrypt(jSONObject.optString("ppVersion"));
            if (TextUtils.isEmpty(decrypt)) {
                return;
            }
            setPpVersion(Integer.parseInt(decrypt.trim()));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private String decrypt(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            return new String(Data.AES128Decode(Data.rawMD5(MobSDK.getAppkey() + ":" + DeviceHelper.getInstance(MobSDK.getContext()).getPackageName() + ":" + getTimestamp()), Base64.decode(str, 0)), "UTF-8");
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public String getContent() {
        return this.content;
    }

    public int getPpVersion() {
        return this.ppVersion;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setPpVersion(int i) {
        this.ppVersion = i;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
