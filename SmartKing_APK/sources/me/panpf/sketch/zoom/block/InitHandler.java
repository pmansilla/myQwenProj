package me.panpf.sketch.zoom.block;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
import me.panpf.sketch.SLog;
import me.panpf.sketch.util.KeyCounter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class InitHandler extends Handler {
    private static final String NAME = "InitHandler";
    private static final int WHAT_INIT = 1002;
    private WeakReference<BlockExecutor> reference;

    /* loaded from: classes2.dex */
    public static class Wrapper {
        public boolean correctImageOrientationDisabled;
        public String imageUri;
        public KeyCounter keyCounter;

        public Wrapper(String str, boolean z, KeyCounter keyCounter) {
            this.imageUri = str;
            this.correctImageOrientationDisabled = z;
            this.keyCounter = keyCounter;
        }
    }

    public InitHandler(Looper looper, BlockExecutor blockExecutor) {
        super(looper);
        this.reference = new WeakReference<>(blockExecutor);
    }

    private void init(BlockExecutor blockExecutor, String str, boolean z, int i, KeyCounter keyCounter) {
        if (blockExecutor == null) {
            SLog.w(NAME, "weak reference break. key: %d, imageUri: %s", Integer.valueOf(i), str);
            return;
        }
        int key = keyCounter.getKey();
        if (i != key) {
            SLog.w(NAME, "init key expired. before init. key: %d, newKey: %d, imageUri: %s", Integer.valueOf(i), Integer.valueOf(key), str);
            return;
        }
        try {
            ImageRegionDecoder build = ImageRegionDecoder.build(blockExecutor.callback.getContext(), str, z);
            if (build == null || !build.isReady()) {
                blockExecutor.callbackHandler.postInitError(new Exception("decoder is null or not ready"), str, i, keyCounter);
                return;
            }
            int key2 = keyCounter.getKey();
            if (i == key2) {
                blockExecutor.callbackHandler.postInitCompleted(build, str, i, keyCounter);
            } else {
                SLog.w(NAME, "init key expired. after init. key: %d, newKey: %d, imageUri: %s", Integer.valueOf(i), Integer.valueOf(key2), str);
                build.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            blockExecutor.callbackHandler.postInitError(e, str, i, keyCounter);
        }
    }

    public void clean(String str) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "clean. %s", str);
        }
        removeMessages(1002);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        BlockExecutor blockExecutor = this.reference.get();
        if (blockExecutor != null) {
            blockExecutor.callbackHandler.cancelDelayDestroyThread();
        }
        if (message.what == 1002) {
            Wrapper wrapper = (Wrapper) message.obj;
            init(blockExecutor, wrapper.imageUri, wrapper.correctImageOrientationDisabled, message.arg1, wrapper.keyCounter);
        }
        if (blockExecutor != null) {
            blockExecutor.callbackHandler.postDelayRecycleDecodeThread();
        }
    }

    public void postInit(String str, boolean z, int i, KeyCounter keyCounter) {
        removeMessages(1002);
        Message obtainMessage = obtainMessage(1002);
        obtainMessage.arg1 = i;
        obtainMessage.obj = new Wrapper(str, z, keyCounter);
        obtainMessage.sendToTarget();
    }
}
