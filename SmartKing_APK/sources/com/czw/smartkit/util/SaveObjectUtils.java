package com.czw.smartkit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import com.czw.utils.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/* loaded from: classes.dex */
public final class SaveObjectUtils {
    private static Context _context = null;
    private static String _name = "share_pre_local_xml_cfg";

    private SaveObjectUtils() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v2, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r3v5 */
    public static <T> T getObject(String str, Class<T> cls) {
        ObjectInputStream objectInputStream;
        SharedPreferences sharedPreferences = _context.getSharedPreferences(_name, 0);
        if (sharedPreferences.contains(str)) {
            ObjectInputStream decode = Base64.decode(sharedPreferences.getString(str, null), 0);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    T t = (T) objectInputStream.readObject();
                    try {
                        byteArrayInputStream.close();
                        objectInputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return t;
                } catch (StreamCorruptedException e3) {
                    e = e3;
                    e.printStackTrace();
                    byteArrayInputStream.close();
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                    return null;
                } catch (IOException e4) {
                    e = e4;
                    e.printStackTrace();
                    byteArrayInputStream.close();
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                    return null;
                } catch (ClassNotFoundException e5) {
                    e = e5;
                    e.printStackTrace();
                    byteArrayInputStream.close();
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                    return null;
                }
            } catch (StreamCorruptedException e6) {
                e = e6;
                objectInputStream = null;
            } catch (IOException e7) {
                e = e7;
                objectInputStream = null;
            } catch (ClassNotFoundException e8) {
                e = e8;
                objectInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                decode = 0;
                try {
                    byteArrayInputStream.close();
                    if (decode != 0) {
                        decode.close();
                    }
                } catch (IOException e9) {
                    e9.printStackTrace();
                }
                throw th;
            }
        }
        return null;
    }

    private static <T> T getValue(String str, Class<T> cls) {
        if (_context != null) {
            return (T) getValue(str, cls, _context.getSharedPreferences(_name, 0));
        }
        throw new RuntimeException("请先调用带有context，name参数的构造！");
    }

    private static <T> T getValue(String str, Class<T> cls, SharedPreferences sharedPreferences) {
        T newInstance;
        try {
            newInstance = cls.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LogUtil.e("system类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            LogUtil.e("system类型输入错误或者复杂类型无法解析[" + e2.getMessage() + "]");
        }
        if (newInstance instanceof Integer) {
            return (T) Integer.valueOf(sharedPreferences.getInt(str, 0));
        }
        if (newInstance instanceof String) {
            return (T) sharedPreferences.getString(str, "");
        }
        if (newInstance instanceof Boolean) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(str, false));
        }
        if (newInstance instanceof Long) {
            return (T) Long.valueOf(sharedPreferences.getLong(str, 0L));
        }
        if (newInstance instanceof Float) {
            return (T) Float.valueOf(sharedPreferences.getFloat(str, 0.0f));
        }
        LogUtil.e("system无法找到" + str + "对应的值");
        return null;
    }

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, String str) {
        _context = context;
        if (str == null || TextUtils.isEmpty(str)) {
            _name = "share_pre_local_xml_cfg";
        } else {
            _name = str;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setObject(String str, Object obj) {
        ObjectOutputStream objectOutputStream;
        SharedPreferences sharedPreferences = _context.getSharedPreferences(_name, 0);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = null;
        ObjectOutputStream objectOutputStream2 = 0;
        bArr = null;
        try {
            try {
                try {
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th) {
            th = th;
            objectOutputStream = bArr;
        }
        try {
            objectOutputStream.writeObject(obj);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String str2 = new String(Base64.encode(byteArray, 0));
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(str, str2);
            edit.commit();
            byteArrayOutputStream.close();
            objectOutputStream.close();
            bArr = byteArray;
        } catch (IOException e3) {
            e = e3;
            objectOutputStream2 = objectOutputStream;
            e.printStackTrace();
            byteArrayOutputStream.close();
            bArr = objectOutputStream2;
            if (objectOutputStream2 != 0) {
                objectOutputStream2.close();
                bArr = objectOutputStream2;
            }
        } catch (Throwable th2) {
            th = th2;
            try {
                byteArrayOutputStream.close();
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }
}
