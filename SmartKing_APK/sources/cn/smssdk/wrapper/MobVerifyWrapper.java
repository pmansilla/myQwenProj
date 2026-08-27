package cn.smssdk.wrapper;

import cn.smssdk.utils.SMSLog;
import com.mob.mobverify.MobVerify;
import com.mob.mobverify.OperationCallback;
import com.mob.mobverify.datatype.VerifyResult;
import com.mob.mobverify.exception.VerifyException;

/* loaded from: classes.dex */
public class MobVerifyWrapper {
    public static void a(final a<TokenVerifyResult> aVar) {
        try {
            MobVerify.getToken(new OperationCallback<VerifyResult>() { // from class: cn.smssdk.wrapper.MobVerifyWrapper.1
                public void onComplete(VerifyResult verifyResult) {
                    if (a.this != null) {
                        a.this.a((a) new TokenVerifyResult(verifyResult.getOpToken(), verifyResult.getToken(), verifyResult.getOperator()));
                    }
                }

                public void onFailure(VerifyException verifyException) {
                    SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "get token failed: " + verifyException.getMessage());
                    if (a.this != null) {
                        a.this.a(new TokenVerifyException(verifyException.getCode(), verifyException.getMessage()));
                    }
                }
            });
        } catch (Throwable unused) {
            SMSLog.getInstance().d(SMSLog.FORMAT_SIMPLE, "invoke mobverify component error");
        }
    }
}
