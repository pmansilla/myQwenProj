package com.amap.location.common.log;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import com.amap.location.common.HeaderConfig;
import com.amap.location.common.log.LogConfig;
import com.amap.location.common.util.FileUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class ALLog {
    private static final long CHECK_FULL_INTERVAL = 20000;
    private static final int LOG_LEVEL_ERROR = 4;
    private static final int LOG_LEVEL_INFO = 1;
    private static final int LOG_LEVEL_WARN = 2;
    private static final long MAX_BUFFER_SIZE = 5;
    private static final long MAX_DUMP_LOG_LINE_NUM = 5000;
    private static final int MSG_BUFFER_FULL = 1;
    private static final int MSG_CHECK_BUFFER = 2;
    private static final String TAG = "ALLog";
    private static final String TRACE_PREFIX = "trace_";
    private static volatile Handler mAALogHandler = null;
    private static volatile HandlerThread mAALogHandlerThread = null;
    private static volatile Context mContext = null;
    private static volatile boolean mGlobalFileLogEnable = false;
    private static volatile boolean mGlobalLogcatEnable = false;
    private static volatile boolean mGlobalServerLogEnable = false;
    private static boolean mIsTraceUpToServer = true;
    private static boolean mIsTraceWriteToFile = false;
    private static long mLogCacheSize = 0;
    private static volatile String mLogFileDir = "";
    private static LogConfig.a mLogToServerImpl;
    private static LogConfig.Product mProduct = LogConfig.Product.SDK;
    private static String mProductStr = "sdk";
    private static long MAX_SINGLE_FILE_LENGTH = PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    private static long MAX_FILE_NUM = 20;
    private static long MAX_LOG_CACHE_SIZE = 204800;
    private static final SimpleDateFormat LOG_LINE_TIME = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.US);
    private static final SimpleDateFormat FILE_NAME_TIME = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS", Locale.US);
    private static final Date sDate = new Date();
    private static volatile File mCurrLogFile = null;
    private static final ArrayDeque<File> mLogFileDeque = new ArrayDeque<>();
    private static volatile LinkedList<String> mLogBuffer = new LinkedList<>();
    private static LinkedList<LinkedList<String>> mBufferList = new LinkedList<>();
    private static final Object mLogBufferLock = new Object();
    private static String mLogFileHeaderString = "";
    private static volatile boolean mFileLogReady = false;
    private static String mPid = "";
    private static final Runnable mInitLogFileTask = new Runnable() { // from class: com.amap.location.common.log.ALLog.2
        @Override // java.lang.Runnable
        public final void run() {
            try {
                if (!ALLog.access$400()) {
                    ALLog.dispose();
                    return;
                }
                File[] filesByLastModify = ALLog.getFilesByLastModify(ALLog.mLogFileDir);
                if (filesByLastModify != null && filesByLastModify.length > 0) {
                    synchronized (ALLog.mLogFileDeque) {
                        for (File file : filesByLastModify) {
                            ALLog.mLogFileDeque.offer(file);
                        }
                    }
                }
                String unused = ALLog.mLogFileHeaderString = a.a(ALLog.mContext);
                File unused2 = ALLog.mCurrLogFile = ALLog.access$1200();
                if (ALLog.mCurrLogFile == null) {
                    ALLog.dispose();
                    return;
                }
                boolean unused3 = ALLog.mFileLogReady = true;
                ALLog.mAALogHandler.sendMessageDelayed(ALLog.mAALogHandler.obtainMessage(2), ALLog.CHECK_FULL_INTERVAL);
            } catch (Exception e) {
                ALLog.e(ALLog.TAG, "InitLogFileTask  error ", e);
            }
        }
    };

    private ALLog() {
    }

    static /* synthetic */ File access$1200() {
        return getNewLogFile();
    }

    static /* synthetic */ boolean access$400() {
        return prepareLogDir();
    }

    private static String currFomatTime(DateFormat dateFormat) {
        String format;
        synchronized (sDate) {
            sDate.setTime(System.currentTimeMillis());
            format = dateFormat.format(sDate);
        }
        return format;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dispose() {
        mFileLogReady = false;
        try {
            try {
                if (mAALogHandlerThread != null) {
                    if (Build.VERSION.SDK_INT > 18) {
                        mAALogHandlerThread.quitSafely();
                    } else {
                        mAALogHandlerThread.quit();
                    }
                }
                mAALogHandler = null;
                mAALogHandlerThread = null;
                synchronized (mLogFileDeque) {
                    mLogFileDeque.clear();
                }
                synchronized (mLogBufferLock) {
                    mLogBuffer.clear();
                    mBufferList.clear();
                }
            } catch (Exception e) {
                e(TAG, "dispose error ", e);
                mAALogHandler = null;
                mAALogHandlerThread = null;
                synchronized (mLogFileDeque) {
                    mLogFileDeque.clear();
                    synchronized (mLogBufferLock) {
                        mLogBuffer.clear();
                        mBufferList.clear();
                    }
                }
            }
        } catch (Throwable th) {
            mAALogHandler = null;
            mAALogHandlerThread = null;
            synchronized (mLogFileDeque) {
                mLogFileDeque.clear();
                synchronized (mLogBufferLock) {
                    mLogBuffer.clear();
                    mBufferList.clear();
                    throw th;
                }
            }
        }
    }

    public static void e(String str, String str2) {
        if (mGlobalLogcatEnable) {
            Log.e(str, str2);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (mGlobalLogcatEnable) {
            Log.e(str, str2, th);
        }
    }

    public static void e(String str, String str2, Throwable th, boolean z) {
        e(str, str2, th);
        writeLog(4, str, str2 + Log.getStackTraceString(th), z, false);
    }

    public static void e(String str, String str2, Throwable th, boolean z, boolean z2) {
        e(str, str2, th);
        writeLog(4, str, str2 + Log.getStackTraceString(th), z, z2);
    }

    public static void e(String str, String str2, boolean z) {
        e(str, str2);
        writeLog(4, str, str2, z, false);
    }

    public static void e(String str, String str2, boolean z, boolean z2) {
        e(str, str2);
        writeLog(4, str, str2, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] getFilesByLastModify(String str) {
        File[] listFiles = new File(str).listFiles(new FileFilter() { // from class: com.amap.location.common.log.ALLog.3
            @Override // java.io.FileFilter
            public final boolean accept(File file) {
                return !file.isDirectory();
            }
        });
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        Arrays.sort(listFiles, new Comparator<File>() { // from class: com.amap.location.common.log.ALLog.4
            @Override // java.util.Comparator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final int compare(File file, File file2) {
                long lastModified = file.lastModified() - file2.lastModified();
                if (lastModified > 0) {
                    return 1;
                }
                return lastModified < 0 ? -1 : 0;
            }
        });
        return listFiles;
    }

    public static String getLogFileDir() {
        return mLogFileDir;
    }

    private static File getNewLogFile() {
        synchronized (mLogFileDeque) {
            File last = mLogFileDeque.size() > 0 ? mLogFileDeque.getLast() : null;
            if (last != null && last.length() < (MAX_SINGLE_FILE_LENGTH * 2) / 3) {
                mLogFileDeque.removeLast();
                return last;
            }
            File file = new File(mLogFileDir, getProductStr() + "_log_" + currFomatTime(FILE_NAME_TIME) + ".txt");
            try {
                file.createNewFile();
                if (!TextUtils.isEmpty(mLogFileHeaderString)) {
                    FileUtil.writeToFile(mLogFileHeaderString + "\r\n-------------------\r\n", file, true);
                }
                return file;
            } catch (IOException unused) {
                return null;
            }
        }
    }

    public static LogConfig.Product getProduct() {
        return mProduct;
    }

    private static String getProductStr() {
        return mProductStr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleMsg(Message message) {
        LinkedList<String> removeFirst;
        switch (message.what) {
            case 1:
                synchronized (mLogBufferLock) {
                    removeFirst = mBufferList.size() > 0 ? mBufferList.removeFirst() : null;
                }
                System.currentTimeMillis();
                syncDataToFile(removeFirst);
                System.currentTimeMillis();
                if (mAALogHandler != null) {
                    mAALogHandler.sendMessageDelayed(mAALogHandler.obtainMessage(2), CHECK_FULL_INTERVAL);
                    return;
                }
                return;
            case 2:
                synchronized (mLogBufferLock) {
                    if (mAALogHandler != null) {
                        mBufferList.add(mLogBuffer);
                        while (mBufferList.size() > 5) {
                            mBufferList.removeFirst();
                        }
                        mAALogHandler.obtainMessage(1).sendToTarget();
                        mLogBuffer = new LinkedList<>();
                    } else {
                        mLogBuffer.clear();
                    }
                    mLogCacheSize = 0L;
                }
                return;
            default:
                return;
        }
    }

    public static void i(String str, String str2) {
        if (mGlobalLogcatEnable) {
            Log.i(str, str2);
        }
    }

    public static void i(String str, String str2, boolean z) {
        i(str, str2);
        writeLog(1, str, str2, z, false);
    }

    public static void i(String str, String str2, boolean z, boolean z2) {
        i(str, str2);
        writeLog(1, str, str2, z, z2);
    }

    public static void init(Context context, LogConfig logConfig) {
        if (mContext != null) {
            return;
        }
        mContext = context.getApplicationContext();
        mGlobalLogcatEnable = logConfig.isLogcatEnable();
        mGlobalFileLogEnable = logConfig.isFileLogEnable();
        mGlobalServerLogEnable = logConfig.isServerLogEnable();
        mLogFileDir = logConfig.getLogFileDir();
        mLogToServerImpl = logConfig.getLogToServerImpl();
        mIsTraceUpToServer = logConfig.isTraceUpToServer();
        mIsTraceWriteToFile = logConfig.isTraceWriteToFile();
        MAX_LOG_CACHE_SIZE = logConfig.getLogMemoryBufferSize();
        MAX_FILE_NUM = logConfig.getLogFileMaxCount();
        MAX_SINGLE_FILE_LENGTH = logConfig.getSignalLogFileLimit();
        setProduct(logConfig.getProduct());
        mPid = String.valueOf(Process.myPid());
        if (mGlobalFileLogEnable) {
            initHandler();
        }
    }

    private static void initHandler() {
        HandlerThread handlerThread = new HandlerThread("allog" + Process.myPid()) { // from class: com.amap.location.common.log.ALLog.1
            @Override // android.os.HandlerThread
            protected final void onLooperPrepared() {
                Looper looper = ALLog.mAALogHandlerThread.getLooper();
                if (looper == null) {
                    return;
                }
                Handler unused = ALLog.mAALogHandler = new Handler(looper) { // from class: com.amap.location.common.log.ALLog.1.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        ALLog.handleMsg(message);
                    }
                };
                ALLog.mAALogHandler.post(ALLog.mInitLogFileTask);
            }
        };
        mAALogHandlerThread = handlerThread;
        handlerThread.start();
    }

    public static boolean isGlobalFileLogEnable() {
        return mGlobalFileLogEnable;
    }

    public static boolean isGlobalLogcatEnable() {
        return mGlobalLogcatEnable;
    }

    public static boolean isGlobalServerLogEnable() {
        return mGlobalServerLogEnable;
    }

    public static boolean isTraceUseful() {
        try {
            if (!mGlobalServerLogEnable || mLogToServerImpl == null) {
                return false;
            }
            return mLogToServerImpl.a();
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String logEncode(String str) {
        return "@@_" + com.amap.location.common.util.a.a(str) + "_@@";
    }

    private static boolean prepareLogDir() {
        File file = new File(mLogFileDir);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        boolean exists = file.exists();
        if (!exists) {
            exists = file.mkdirs();
        }
        if (!exists || !file.canWrite()) {
            return false;
        }
        File file2 = new File(file, HeaderConfig.getProcessName());
        if (!file2.exists() && !file2.mkdir()) {
            return true;
        }
        mLogFileDir = file2.getAbsolutePath();
        return true;
    }

    public static void setGlobalFileLogEnable(boolean z) {
        mGlobalFileLogEnable = z;
    }

    public static void setGlobalLogcatEnable(boolean z) {
        mGlobalLogcatEnable = z;
    }

    public static void setGlobalServerLogEnable(boolean z) {
        mGlobalServerLogEnable = z;
    }

    private static void setProduct(LogConfig.Product product) {
        mProduct = product;
        switch (product) {
            case FLP:
                mProductStr = "flp";
                return;
            case NLP:
                mProductStr = "nlp";
                return;
            default:
                return;
        }
    }

    public static void setTraceUpToServer(boolean z) {
        mIsTraceUpToServer = z;
    }

    public static void setTraceWriteToFile(boolean z) {
        mIsTraceWriteToFile = z;
    }

    private static void syncDataToFile(List<String> list) {
        if (list != null) {
            try {
                if (list.size() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    sb.append(it.next());
                }
                if (!writeToFile(sb.toString(), mCurrLogFile)) {
                    dispose();
                    return;
                }
                synchronized (mLogFileDeque) {
                    while (mLogFileDeque.size() + 1 > MAX_FILE_NUM) {
                        File poll = mLogFileDeque.poll();
                        if (poll != null && poll.exists()) {
                            try {
                                poll.delete();
                            } catch (Exception e) {
                                e(TAG, "MAX_FILE_NUM delete  error ", e);
                            }
                        }
                    }
                }
                if (mCurrLogFile.length() > MAX_SINGLE_FILE_LENGTH) {
                    synchronized (mLogFileDeque) {
                        mLogFileDeque.offer(mCurrLogFile);
                    }
                    File newLogFile = getNewLogFile();
                    mCurrLogFile = newLogFile;
                    if (newLogFile == null) {
                        dispose();
                    }
                }
            } catch (Exception e2) {
                e(TAG, "DumpTask  error ", e2);
            }
        }
    }

    public static void trace(Exception exc) {
        if (isTraceUseful()) {
            e(TRACE_PREFIX, "", exc, mIsTraceWriteToFile, mIsTraceUpToServer);
        }
    }

    public static void trace(String str, String str2) {
        if (isTraceUseful()) {
            w(TRACE_PREFIX + str, str2, mIsTraceWriteToFile, mIsTraceUpToServer);
        }
    }

    public static void trace(String str, String str2, Exception exc) {
        if (isTraceUseful()) {
            e(TRACE_PREFIX + str, str2, exc, mIsTraceWriteToFile, mIsTraceUpToServer);
        }
    }

    public static void w(String str, String str2) {
        if (mGlobalLogcatEnable) {
            Log.w(str, str2);
        }
    }

    public static void w(String str, String str2, boolean z) {
        w(str, str2);
        writeLog(2, str, str2, z, false);
    }

    public static void w(String str, String str2, boolean z, boolean z2) {
        w(str, str2);
        writeLog(2, str, str2, z, z2);
    }

    private static void writeLog(int i, String str, String str2, boolean z, boolean z2) {
        String str3;
        boolean z3 = z && mGlobalFileLogEnable && mFileLogReady;
        boolean z4 = z2 && mGlobalServerLogEnable && mLogToServerImpl != null && mLogToServerImpl.a();
        if (z3 || z4) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(System.currentTimeMillis()));
            sb.append("|");
            if (i != 4) {
                switch (i) {
                    case 1:
                        str3 = "info|";
                        break;
                    case 2:
                        str3 = "warn|";
                        break;
                }
            } else {
                str3 = "error|";
            }
            sb.append(str3);
            long myTid = Process.myTid();
            sb.append(mPid);
            sb.append("|");
            sb.append(String.valueOf(myTid));
            sb.append("|");
            sb.append(str);
            sb.append("|");
            sb.append(str2);
            sb.append("\n");
            if (z3) {
                writeToBuffer(sb.toString());
            }
            if (z4) {
                writeToServer(sb.substring(0, sb.length() - 1));
            }
        }
    }

    private static void writeToBuffer(String str) {
        synchronized (mLogBufferLock) {
            mLogBuffer.add(str);
            mLogCacheSize += str.length();
            if (mLogBuffer.size() >= 5000 || mLogCacheSize > MAX_LOG_CACHE_SIZE) {
                if (mAALogHandler != null) {
                    mBufferList.add(mLogBuffer);
                    while (mBufferList.size() > 5) {
                        mBufferList.removeFirst();
                    }
                    mAALogHandler.obtainMessage(1).sendToTarget();
                    mAALogHandler.removeMessages(2);
                    mLogBuffer = new LinkedList<>();
                } else {
                    mLogBuffer.clear();
                }
                mLogCacheSize = 0L;
            }
        }
    }

    private static boolean writeToFile(String str, File file) {
        if (FileUtil.writeToFile(str + "\r\n-------------------\r\n", file, true)) {
            return true;
        }
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            File file2 = parentFile;
            int i = 0;
            while (true) {
                if (file2 == null) {
                    break;
                }
                if (!file2.exists()) {
                    file2 = file2.getParentFile();
                    i++;
                    if (i >= 2) {
                        break;
                    }
                } else if (file2.isFile()) {
                    file2.delete();
                }
            }
            if (parentFile != null) {
                try {
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                    return FileUtil.writeToFile(str + "\r\n-------------------\r\n", file, true);
                } catch (Exception unused) {
                }
            }
        }
        return false;
    }

    private static void writeToServer(String str) {
        if (mLogToServerImpl != null) {
            mLogToServerImpl.a(str);
        }
    }
}
