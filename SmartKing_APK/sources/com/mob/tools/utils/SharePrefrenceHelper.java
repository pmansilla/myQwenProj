package com.mob.tools.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import com.amap.location.common.model.AmapLoc;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class SharePrefrenceHelper {
    private Context context;
    private MobSharePreference prefrence;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class MobSharePreference {
        private static Handler handler = MobHandlerThread.newHandler("s", new Handler.Callback() { // from class: com.mob.tools.utils.SharePrefrenceHelper.MobSharePreference.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                OnCommitListener onCommitListener;
                try {
                    onCommitListener = (OnCommitListener) message.obj;
                } catch (Throwable unused) {
                    onCommitListener = null;
                }
                try {
                    Bundle data = message.getData();
                    String string = data.getString("json");
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(data.getString(AmapLoc.TYPE_OFFLINE_CELL)), "utf-8");
                    outputStreamWriter.append((CharSequence) string);
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    if (onCommitListener == null) {
                        return false;
                    }
                    onCommitListener.onCommit(null);
                    return false;
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                    if (onCommitListener == null) {
                        return false;
                    }
                    onCommitListener.onCommit(th);
                    return false;
                }
            }
        });
        private Hashon hashon;
        private OnCommitListener listener;
        private File spFile;
        private HashMap<String, Object> spMap;

        public MobSharePreference(Context context, String str) {
            if (context != null) {
                try {
                    this.spFile = new File(new File(context.getFilesDir(), "Mob"), str);
                    if (!this.spFile.getParentFile().exists()) {
                        this.spFile.getParentFile().mkdirs();
                    }
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                    return;
                }
            }
            this.spMap = new HashMap<>();
            this.hashon = new Hashon();
            open();
        }

        private Object get(String str) {
            Object obj;
            synchronized (this.spMap) {
                obj = this.spMap.get(str);
            }
            return obj;
        }

        private void open() {
            synchronized (this.spMap) {
                if (this.spFile.exists()) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.spFile), "utf-8"));
                        StringBuilder sb = new StringBuilder();
                        for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                            if (sb.length() > 0) {
                                sb.append("\n");
                            }
                            sb.append(readLine);
                        }
                        bufferedReader.close();
                        this.spMap = this.hashon.fromJson(sb.toString());
                    } catch (Throwable th) {
                        MobLog.getInstance().w(th);
                    }
                }
            }
        }

        private void put(String str, Object obj) {
            synchronized (this.spMap) {
                this.spMap.put(str, obj);
                if (handler != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", this.hashon.fromHashMap(this.spMap));
                    bundle.putString(AmapLoc.TYPE_OFFLINE_CELL, this.spFile.getAbsolutePath());
                    message.setData(bundle);
                    message.what = 1;
                    message.obj = this.listener;
                    handler.sendMessage(message);
                }
            }
        }

        public void clear() {
            synchronized (this.spMap) {
                this.spMap.clear();
            }
            if (handler != null) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("json", this.hashon.fromHashMap(this.spMap));
                bundle.putString(AmapLoc.TYPE_OFFLINE_CELL, this.spFile.getAbsolutePath());
                message.setData(bundle);
                message.what = 1;
                message.obj = this.listener;
                handler.sendMessage(message);
            }
        }

        public HashMap<String, Object> getAll() {
            HashMap<String, Object> hashMap;
            synchronized (this.spMap) {
                hashMap = new HashMap<>();
                hashMap.putAll(this.spMap);
            }
            return hashMap;
        }

        public boolean getBoolean(String str, boolean z) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).byteValue() == 1 : z;
        }

        public byte getByte(String str, byte b) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).byteValue() : b;
        }

        public char getChar(String str, char c) {
            Object obj = get(str);
            return obj != null ? ((String) obj).charAt(0) : c;
        }

        public double getDouble(String str, double d) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).doubleValue() : d;
        }

        public float getFloat(String str, float f) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).floatValue() : f;
        }

        public int getInt(String str, int i) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).intValue() : i;
        }

        public long getLong(String str, long j) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).longValue() : j;
        }

        public short getShort(String str, short s) {
            Object obj = get(str);
            return obj != null ? ((Number) obj).shortValue() : s;
        }

        public String getString(String str, String str2) {
            Object obj = get(str);
            return obj != null ? (String) obj : str2;
        }

        public void putAll(HashMap<String, Object> hashMap) {
            synchronized (this.spMap) {
                this.spMap.putAll(hashMap);
            }
            if (handler != null) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("json", this.hashon.fromHashMap(this.spMap));
                bundle.putString(AmapLoc.TYPE_OFFLINE_CELL, this.spFile.getAbsolutePath());
                message.setData(bundle);
                message.what = 1;
                message.obj = this.listener;
                handler.sendMessage(message);
            }
        }

        public void putBoolean(String str, boolean z) {
            putByte(str, z ? (byte) 1 : (byte) 0);
        }

        public void putByte(String str, byte b) {
            put(str, Byte.valueOf(b));
        }

        public void putChar(String str, char c) {
            putString(str, String.valueOf(c));
        }

        public void putDouble(String str, double d) {
            put(str, Double.valueOf(d));
        }

        public void putFloat(String str, float f) {
            put(str, Float.valueOf(f));
        }

        public void putInt(String str, int i) {
            put(str, Integer.valueOf(i));
        }

        public void putLong(String str, long j) {
            put(str, Long.valueOf(j));
        }

        public void putShort(String str, short s) {
            put(str, Short.valueOf(s));
        }

        public void putString(String str, String str2) {
            put(str, str2);
        }

        public void remove(String str) {
            put(str, null);
        }

        public void setOnCommitListener(OnCommitListener onCommitListener) {
            this.listener = onCommitListener;
        }
    }

    /* loaded from: classes2.dex */
    public interface OnCommitListener {
        void onCommit(Throwable th);
    }

    public SharePrefrenceHelper(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    public void clear() {
        if (this.prefrence != null) {
            this.prefrence.clear();
        }
    }

    public Object get(String str) {
        try {
            String string = getString(str);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(string, 2)));
            Object readObject = objectInputStream.readObject();
            objectInputStream.close();
            return readObject;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public HashMap<String, Object> getAll() {
        return this.prefrence != null ? this.prefrence.getAll() : new HashMap<>();
    }

    public boolean getBoolean(String str) {
        if (this.prefrence != null) {
            return this.prefrence.getBoolean(str, false);
        }
        return false;
    }

    public boolean getBoolean(String str, boolean z) {
        return this.prefrence != null ? this.prefrence.getBoolean(str, z) : z;
    }

    public float getFloat(String str) {
        if (this.prefrence != null) {
            return this.prefrence.getFloat(str, 0.0f);
        }
        return 0.0f;
    }

    public float getFloat(String str, float f) {
        return this.prefrence != null ? this.prefrence.getFloat(str, f) : f;
    }

    public int getInt(String str) {
        if (this.prefrence != null) {
            return this.prefrence.getInt(str, 0);
        }
        return 0;
    }

    public int getInt(String str, int i) {
        return this.prefrence != null ? this.prefrence.getInt(str, i) : i;
    }

    public long getLong(String str) {
        if (this.prefrence != null) {
            return this.prefrence.getLong(str, 0L);
        }
        return 0L;
    }

    public long getLong(String str, long j) {
        return this.prefrence != null ? this.prefrence.getLong(str, j) : j;
    }

    public String getString(String str) {
        return this.prefrence != null ? this.prefrence.getString(str, "") : "";
    }

    public String getString(String str, String str2) {
        return this.prefrence != null ? this.prefrence.getString(str, str2) : str2;
    }

    public void open(String str) {
        open(str, 0);
    }

    public void open(String str, int i) {
        this.prefrence = new MobSharePreference(this.context, str + "_" + i);
    }

    public void put(String str, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            objectOutputStream.close();
            putString(str, Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2));
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    public void putAll(HashMap<String, Object> hashMap) {
        if (this.prefrence != null) {
            this.prefrence.putAll(hashMap);
        }
    }

    public void putBoolean(String str, Boolean bool) {
        if (this.prefrence != null) {
            this.prefrence.putBoolean(str, bool.booleanValue());
        }
    }

    public void putFloat(String str, Float f) {
        if (this.prefrence != null) {
            this.prefrence.putFloat(str, f.floatValue());
        }
    }

    public void putInt(String str, Integer num) {
        if (this.prefrence != null) {
            this.prefrence.putInt(str, num.intValue());
        }
    }

    public void putLong(String str, Long l) {
        if (this.prefrence != null) {
            this.prefrence.putLong(str, l.longValue());
        }
    }

    public void putString(String str, String str2) {
        if (this.prefrence != null) {
            this.prefrence.putString(str, str2);
        }
    }

    public void remove(String str) {
        if (this.prefrence != null) {
            this.prefrence.remove(str);
        }
    }

    public void setOnCommitListener(OnCommitListener onCommitListener) {
        if (this.prefrence != null) {
            this.prefrence.setOnCommitListener(onCommitListener);
        }
    }
}
