package com.mob.tools.utils;

import android.os.Handler;
import android.os.Message;
import basecamera.module.lib.CameraInterface;
import com.alibaba.fastjson.asm.Opcodes;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import com.czw.smartkit.BuildConfig;
import com.mob.tools.MobLog;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class ApplicationTracker {
    private static HashSet<Tracker> trackers = new HashSet<>();

    /* loaded from: classes2.dex */
    public static abstract class Tracker {
        protected void onActivityConfigurationChanged(Object obj, Message message) {
        }

        protected void onAttachAgent(Object obj, Message message) {
        }

        protected void onBackgroundVisibleBehindChanged(Object obj, Message message) {
        }

        protected void onBindApplication(Object obj, Message message) {
        }

        protected void onBindService(Object obj, Message message) {
        }

        protected void onCancelVisibleBehind(Object obj, Message message) {
        }

        protected void onCleanUpContext(Object obj, Message message) {
        }

        protected void onConfigurationChanged(Object obj, Message message) {
        }

        protected void onCreateBackupAgent(Object obj, Message message) {
        }

        protected void onCreateService(Object obj, Message message) {
        }

        protected void onDestroyActivity(Object obj, Message message) {
        }

        protected void onDestroyBackupAgent(Object obj, Message message) {
        }

        protected void onDispatchPackageBroadcast(Object obj, Message message) {
        }

        protected void onDumpActivity(Object obj, Message message) {
        }

        protected void onDumpHeap(Object obj, Message message) {
        }

        protected void onDumpProvider(Object obj, Message message) {
        }

        protected void onDumpService(Object obj, Message message) {
        }

        protected void onEnableJit(Object obj, Message message) {
        }

        protected void onEnterAnimationComplete(Object obj, Message message) {
        }

        protected void onExitApplication(Object obj, Message message) {
        }

        protected void onGcWhenIdle(Object obj, Message message) {
        }

        protected void onHideWindow(Object obj, Message message) {
        }

        protected void onInstallProvider(Object obj, Message message) {
        }

        protected void onLaunchActivity(Object obj, Message message) {
        }

        protected void onLocalVoiceInteractionStarted(Object obj, Message message) {
        }

        protected void onLowMemory(Object obj, Message message) {
        }

        protected void onMultiWindowModeChanged(Object obj, Message message) {
        }

        protected void onNewIntent(Object obj, Message message) {
        }

        protected void onOnNewActivityOptions(Object obj, Message message) {
        }

        protected void onPauseActivity(Object obj, Message message) {
        }

        protected void onPauseActivityFinishing(Object obj, Message message) {
        }

        protected void onPictureInPictureModeChanged(Object obj, Message message) {
        }

        protected void onProfilerControl(Object obj, Message message) {
        }

        protected void onReceiver(Object obj, Message message) {
        }

        protected void onRelaunchActivity(Object obj, Message message) {
        }

        protected void onRemoveProvider(Object obj, Message message) {
        }

        protected void onRequestAssistContextExtras(Object obj, Message message) {
        }

        protected void onResumeActivity(Object obj, Message message) {
        }

        protected void onScheduleCrash(Object obj, Message message) {
        }

        protected void onSendResult(Object obj, Message message) {
        }

        protected void onServiceArgs(Object obj, Message message) {
        }

        protected void onSetCoreSettings(Object obj, Message message) {
        }

        protected void onShowWindow(Object obj, Message message) {
        }

        protected void onSleeping(Object obj, Message message) {
        }

        protected void onStartBinderTracking(Object obj, Message message) {
        }

        protected void onStopActivityHide(Object obj, Message message) {
        }

        protected void onStopActivityShow(Object obj, Message message) {
        }

        protected void onStopBinderTrackingAndDump(Object obj, Message message) {
        }

        protected void onStopService(Object obj, Message message) {
        }

        protected void onSuicide(Object obj, Message message) {
        }

        protected void onTranslucentConversionComplete(Object obj, Message message) {
        }

        protected void onTrimMemory(Object obj, Message message) {
        }

        protected void onUnbindService(Object obj, Message message) {
        }

        protected void onUnstableProviderDied(Object obj, Message message) {
        }

        protected void onUpdatePackageCompatibilityInfo(Object obj, Message message) {
        }
    }

    static {
        try {
            final Object currentActivityThread = DeviceHelper.currentActivityThread();
            Object instanceField = ReflectHelper.getInstanceField(currentActivityThread, "mH");
            String str = "mCallback";
            final Handler.Callback callback = (Handler.Callback) ReflectHelper.getInstanceField(instanceField, str);
            ReflectHelper.setInstanceField(instanceField, str, new Handler.Callback() { // from class: com.mob.tools.utils.ApplicationTracker.1
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    Iterator it = ApplicationTracker.trackers.iterator();
                    while (it.hasNext()) {
                        Tracker tracker = (Tracker) it.next();
                        try {
                            switch (message.what) {
                                case 100:
                                    tracker.onLaunchActivity(currentActivityThread, message);
                                    continue;
                                case 101:
                                    tracker.onPauseActivity(currentActivityThread, message);
                                    continue;
                                case 102:
                                    tracker.onPauseActivityFinishing(currentActivityThread, message);
                                    continue;
                                case 103:
                                    tracker.onStopActivityShow(currentActivityThread, message);
                                    continue;
                                case 104:
                                    tracker.onStopActivityHide(currentActivityThread, message);
                                    continue;
                                case 105:
                                    tracker.onShowWindow(currentActivityThread, message);
                                    continue;
                                case 106:
                                    tracker.onHideWindow(currentActivityThread, message);
                                    continue;
                                case 107:
                                    tracker.onResumeActivity(currentActivityThread, message);
                                    continue;
                                case 108:
                                    tracker.onSendResult(currentActivityThread, message);
                                    continue;
                                case 109:
                                    tracker.onDestroyActivity(currentActivityThread, message);
                                    continue;
                                case 110:
                                    tracker.onBindApplication(currentActivityThread, message);
                                    continue;
                                case 111:
                                    tracker.onExitApplication(currentActivityThread, message);
                                    continue;
                                case 112:
                                    tracker.onNewIntent(currentActivityThread, message);
                                    continue;
                                case 113:
                                    tracker.onReceiver(currentActivityThread, message);
                                    continue;
                                case 114:
                                    tracker.onCreateService(currentActivityThread, message);
                                    continue;
                                case 115:
                                    tracker.onServiceArgs(currentActivityThread, message);
                                    continue;
                                case 116:
                                    tracker.onStopService(currentActivityThread, message);
                                    continue;
                                case 117:
                                default:
                                    continue;
                                case 118:
                                    tracker.onConfigurationChanged(currentActivityThread, message);
                                    continue;
                                case 119:
                                    tracker.onCleanUpContext(currentActivityThread, message);
                                    continue;
                                case GlMapUtil.DEVICE_DISPLAY_DPI_LOW /* 120 */:
                                    tracker.onGcWhenIdle(currentActivityThread, message);
                                    continue;
                                case BuildConfig.VERSION_CODE /* 121 */:
                                    tracker.onBindService(currentActivityThread, message);
                                    continue;
                                case 122:
                                    tracker.onUnbindService(currentActivityThread, message);
                                    continue;
                                case 123:
                                    tracker.onDumpService(currentActivityThread, message);
                                    continue;
                                case 124:
                                    tracker.onLowMemory(currentActivityThread, message);
                                    continue;
                                case 125:
                                    tracker.onActivityConfigurationChanged(currentActivityThread, message);
                                    continue;
                                case Opcodes.IAND /* 126 */:
                                    tracker.onRelaunchActivity(currentActivityThread, message);
                                    continue;
                                case 127:
                                    tracker.onProfilerControl(currentActivityThread, message);
                                    continue;
                                case 128:
                                    tracker.onCreateBackupAgent(currentActivityThread, message);
                                    continue;
                                case 129:
                                    tracker.onDestroyBackupAgent(currentActivityThread, message);
                                    continue;
                                case 130:
                                    tracker.onSuicide(currentActivityThread, message);
                                    continue;
                                case 131:
                                    tracker.onRemoveProvider(currentActivityThread, message);
                                    continue;
                                case 132:
                                    tracker.onEnableJit(currentActivityThread, message);
                                    continue;
                                case 133:
                                    tracker.onDispatchPackageBroadcast(currentActivityThread, message);
                                    continue;
                                case 134:
                                    tracker.onScheduleCrash(currentActivityThread, message);
                                    continue;
                                case 135:
                                    tracker.onDumpHeap(currentActivityThread, message);
                                    continue;
                                case 136:
                                    tracker.onDumpActivity(currentActivityThread, message);
                                    continue;
                                case 137:
                                    tracker.onSleeping(currentActivityThread, message);
                                    continue;
                                case 138:
                                    tracker.onSetCoreSettings(currentActivityThread, message);
                                    continue;
                                case 139:
                                    tracker.onUpdatePackageCompatibilityInfo(currentActivityThread, message);
                                    continue;
                                case 140:
                                    tracker.onTrimMemory(currentActivityThread, message);
                                    continue;
                                case 141:
                                    tracker.onDumpProvider(currentActivityThread, message);
                                    continue;
                                case 142:
                                    tracker.onUnstableProviderDied(currentActivityThread, message);
                                    continue;
                                case 143:
                                    tracker.onRequestAssistContextExtras(currentActivityThread, message);
                                    continue;
                                case CameraInterface.TYPE_RECORDER /* 144 */:
                                    tracker.onTranslucentConversionComplete(currentActivityThread, message);
                                    continue;
                                case CameraInterface.TYPE_CAPTURE /* 145 */:
                                    tracker.onInstallProvider(currentActivityThread, message);
                                    continue;
                                case 146:
                                    tracker.onOnNewActivityOptions(currentActivityThread, message);
                                    continue;
                                case 147:
                                    tracker.onCancelVisibleBehind(currentActivityThread, message);
                                    continue;
                                case Opcodes.LCMP /* 148 */:
                                    tracker.onBackgroundVisibleBehindChanged(currentActivityThread, message);
                                    continue;
                                case Opcodes.FCMPL /* 149 */:
                                    tracker.onEnterAnimationComplete(currentActivityThread, message);
                                    continue;
                                case 150:
                                    tracker.onStartBinderTracking(currentActivityThread, message);
                                    continue;
                                case Opcodes.DCMPL /* 151 */:
                                    tracker.onStopBinderTrackingAndDump(currentActivityThread, message);
                                    continue;
                                case 152:
                                    tracker.onMultiWindowModeChanged(currentActivityThread, message);
                                    continue;
                                case Opcodes.IFEQ /* 153 */:
                                    tracker.onPictureInPictureModeChanged(currentActivityThread, message);
                                    continue;
                                case Opcodes.IFNE /* 154 */:
                                    tracker.onLocalVoiceInteractionStarted(currentActivityThread, message);
                                    continue;
                                case 155:
                                    tracker.onAttachAgent(currentActivityThread, message);
                                    continue;
                            }
                        } catch (Throwable th) {
                            MobLog.getInstance().w(th);
                        }
                        MobLog.getInstance().w(th);
                    }
                    return callback != null && callback.handleMessage(message);
                }
            });
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    public static void addTracker(Tracker tracker) {
        trackers.add(tracker);
    }

    public static void removeTracker(Tracker tracker) {
        trackers.remove(tracker);
    }
}
