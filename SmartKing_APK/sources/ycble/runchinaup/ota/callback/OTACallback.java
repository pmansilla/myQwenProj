package ycble.runchinaup.ota.callback;

import ycble.runchinaup.ota.OTAState;

/* loaded from: classes2.dex */
public abstract class OTACallback {
    public void onCurrentState(OTAState oTAState) {
    }

    public abstract void onFailure(int i, String str);

    public abstract void onProgress(int i);

    public abstract void onSuccess();
}
