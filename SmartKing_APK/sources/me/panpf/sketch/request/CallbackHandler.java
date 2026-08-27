package me.panpf.sketch.request;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class CallbackHandler {
    private static final String PARAM_CANCELED_CAUSE = "canceledCause";
    private static final String PARAM_FAILED_CAUSE = "failedCause";
    private static final int WHAT_CALLBACK_CANCELED = 44003;
    private static final int WHAT_CALLBACK_FAILED = 44002;
    private static final int WHAT_CALLBACK_STARTED = 44001;
    private static final int WHAT_RUN_CANCELED = 33003;
    private static final int WHAT_RUN_COMPLETED = 33001;
    private static final int WHAT_RUN_FAILED = 33002;
    private static final int WHAT_RUN_UPDATE_PROGRESS = 33004;
    private static final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: me.panpf.sketch.request.CallbackHandler.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            switch (i) {
                case CallbackHandler.WHAT_RUN_COMPLETED /* 33001 */:
                    ((AsyncRequest) message.obj).runCompletedInMainThread();
                    return true;
                case CallbackHandler.WHAT_RUN_FAILED /* 33002 */:
                    ((AsyncRequest) message.obj).runErrorInMainThread();
                    return true;
                case CallbackHandler.WHAT_RUN_CANCELED /* 33003 */:
                    ((AsyncRequest) message.obj).runCanceledInMainThread();
                    return true;
                case CallbackHandler.WHAT_RUN_UPDATE_PROGRESS /* 33004 */:
                    ((AsyncRequest) message.obj).runUpdateProgressInMainThread(message.arg1, message.arg2);
                    return true;
                default:
                    switch (i) {
                        case CallbackHandler.WHAT_CALLBACK_STARTED /* 44001 */:
                            ((Listener) message.obj).onStarted();
                            return true;
                        case CallbackHandler.WHAT_CALLBACK_FAILED /* 44002 */:
                            ((Listener) message.obj).onError(ErrorCause.valueOf(message.getData().getString(CallbackHandler.PARAM_FAILED_CAUSE)));
                            return true;
                        case CallbackHandler.WHAT_CALLBACK_CANCELED /* 44003 */:
                            ((Listener) message.obj).onCanceled(CancelCause.valueOf(message.getData().getString(CallbackHandler.PARAM_CANCELED_CAUSE)));
                            return true;
                        default:
                            return true;
                    }
            }
        }
    });

    private CallbackHandler() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postCallbackCanceled(@Nullable Listener listener, @NonNull CancelCause cancelCause, boolean z) {
        if (listener != null) {
            if (z || SketchUtils.isMainThread()) {
                listener.onCanceled(cancelCause);
                return;
            }
            Message obtainMessage = handler.obtainMessage(WHAT_CALLBACK_CANCELED, listener);
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_CANCELED_CAUSE, cancelCause.name());
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postCallbackError(@Nullable Listener listener, @NonNull ErrorCause errorCause, boolean z) {
        if (listener != null) {
            if (z || SketchUtils.isMainThread()) {
                listener.onError(errorCause);
                return;
            }
            Message obtainMessage = handler.obtainMessage(WHAT_CALLBACK_FAILED, listener);
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_FAILED_CAUSE, errorCause.name());
            obtainMessage.setData(bundle);
            obtainMessage.sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postCallbackStarted(@Nullable Listener listener, boolean z) {
        if (listener != null) {
            if (z || SketchUtils.isMainThread()) {
                listener.onStarted();
            } else {
                handler.obtainMessage(WHAT_CALLBACK_STARTED, listener).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postRunCanceled(@NonNull AsyncRequest asyncRequest) {
        if (asyncRequest.isSync()) {
            asyncRequest.runCanceledInMainThread();
        } else {
            handler.obtainMessage(WHAT_RUN_CANCELED, asyncRequest).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postRunCompleted(@NonNull AsyncRequest asyncRequest) {
        if (asyncRequest.isSync()) {
            asyncRequest.runCompletedInMainThread();
        } else {
            handler.obtainMessage(WHAT_RUN_COMPLETED, asyncRequest).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postRunError(@NonNull AsyncRequest asyncRequest) {
        if (asyncRequest.isSync()) {
            asyncRequest.runErrorInMainThread();
        } else {
            handler.obtainMessage(WHAT_RUN_FAILED, asyncRequest).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void postRunUpdateProgress(@NonNull AsyncRequest asyncRequest, int i, int i2) {
        if (asyncRequest.isSync()) {
            asyncRequest.runUpdateProgressInMainThread(i, i2);
        } else {
            handler.obtainMessage(WHAT_RUN_UPDATE_PROGRESS, i, i2, asyncRequest).sendToTarget();
        }
    }
}
