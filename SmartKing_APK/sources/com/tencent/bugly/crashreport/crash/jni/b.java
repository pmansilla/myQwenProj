package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class b {
    private static List<File> a = new ArrayList();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v4, types: [java.io.BufferedInputStream] */
    /* JADX WARN: Type inference failed for: r7v6 */
    public static CrashDetailBean a(Context context, String str, NativeExceptionHandler nativeExceptionHandler) {
        BufferedInputStream bufferedInputStream;
        String str2;
        String a2;
        if (context == null || str == null || nativeExceptionHandler == null) {
            x.e("get eup record file args error", new Object[0]);
            return null;
        }
        File file = new File(str, "rqd_record.eup");
        if (file.exists()) {
            ?? canRead = file.canRead();
            try {
                if (canRead != 0) {
                    try {
                        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        try {
                            String a3 = a(bufferedInputStream);
                            if (a3 != null && a3.equals("NATIVE_RQD_REPORT")) {
                                HashMap hashMap = new HashMap();
                                loop0: while (true) {
                                    str2 = null;
                                    while (true) {
                                        a2 = a(bufferedInputStream);
                                        if (a2 == null) {
                                            break loop0;
                                        }
                                        if (str2 == null) {
                                            str2 = a2;
                                        }
                                    }
                                    hashMap.put(str2, a2);
                                }
                                if (str2 != null) {
                                    x.e("record not pair! drop! %s", str2);
                                    try {
                                        bufferedInputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                                CrashDetailBean a4 = a(context, hashMap, nativeExceptionHandler);
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                                return a4;
                            }
                            x.e("record read fail! %s", a3);
                            try {
                                bufferedInputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                            return null;
                        } catch (IOException e4) {
                            e = e4;
                            e.printStackTrace();
                            if (bufferedInputStream != null) {
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                }
                            }
                            return null;
                        }
                    } catch (IOException e6) {
                        e = e6;
                        bufferedInputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        canRead = 0;
                        if (canRead != 0) {
                            try {
                                canRead.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return null;
    }

    private static CrashDetailBean a(Context context, Map<String, String> map, NativeExceptionHandler nativeExceptionHandler) {
        String str;
        String str2;
        HashMap hashMap;
        if (map == null) {
            return null;
        }
        if (com.tencent.bugly.crashreport.common.info.a.a(context) == null) {
            x.e("abnormal com info not created", new Object[0]);
            return null;
        }
        String str3 = map.get("intStateStr");
        if (str3 == null || str3.trim().length() <= 0) {
            x.e("no intStateStr", new Object[0]);
            return null;
        }
        Map<String, Integer> d = d(str3);
        if (d == null) {
            x.e("parse intSateMap fail", Integer.valueOf(map.size()));
            return null;
        }
        try {
            d.get("sino").intValue();
            d.get("sud").intValue();
            String str4 = map.get("soVersion");
            if (TextUtils.isEmpty(str4)) {
                x.e("error format at version", new Object[0]);
                return null;
            }
            String str5 = map.get("errorAddr");
            if (str5 == null) {
                str5 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            String str6 = str5;
            String str7 = map.get("codeMsg");
            if (str7 == null) {
                str7 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            String str8 = map.get("tombPath");
            if (str8 == null) {
                str8 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            String str9 = str8;
            String str10 = map.get("signalName");
            if (str10 == null) {
                str10 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            map.get("errnoMsg");
            String str11 = map.get("stack");
            if (str11 == null) {
                str11 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            String str12 = map.get("jstack");
            if (str12 != null) {
                str11 = str11 + "java:\n" + str12;
            }
            Integer num = d.get("sico");
            if (num == null || num.intValue() <= 0) {
                str = str7;
                str2 = str10;
            } else {
                str2 = str10 + SQLBuilder.PARENTHESES_LEFT + str7 + SQLBuilder.PARENTHESES_RIGHT;
                str = "KERNEL";
            }
            String str13 = map.get("nativeLog");
            byte[] a2 = (str13 == null || str13.isEmpty()) ? null : z.a((File) null, str13, "BuglyNativeLog.txt");
            String str14 = map.get("sendingProcess");
            if (str14 == null) {
                str14 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            Integer num2 = d.get("spd");
            if (num2 != null) {
                str14 = str14 + SQLBuilder.PARENTHESES_LEFT + num2 + SQLBuilder.PARENTHESES_RIGHT;
            }
            String str15 = str14;
            String str16 = map.get("threadName");
            if (str16 == null) {
                str16 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            Integer num3 = d.get("et");
            if (num3 != null) {
                str16 = str16 + SQLBuilder.PARENTHESES_LEFT + num3 + SQLBuilder.PARENTHESES_RIGHT;
            }
            String str17 = str16;
            String str18 = map.get("processName");
            if (str18 == null) {
                str18 = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            Integer num4 = d.get("ep");
            if (num4 != null) {
                str18 = str18 + SQLBuilder.PARENTHESES_LEFT + num4 + SQLBuilder.PARENTHESES_RIGHT;
            }
            String str19 = map.get("key-value");
            if (str19 != null) {
                HashMap hashMap2 = new HashMap();
                String[] split = str19.split("\n");
                int length = split.length;
                int i = 0;
                while (i < length) {
                    String[] split2 = split[i].split("=");
                    String[] strArr = split;
                    if (split2.length == 2) {
                        hashMap2.put(split2[0], split2[1]);
                    }
                    i++;
                    split = strArr;
                }
                hashMap = hashMap2;
            } else {
                hashMap = null;
            }
            CrashDetailBean packageCrashDatas = nativeExceptionHandler.packageCrashDatas(str18, str17, (d.get("ets").intValue() * 1000) + (d.get("etms").intValue() / 1000), str2, str6, a(str11), str, str15, str9, map.get("sysLogPath"), map.get("jniLogPath"), str4, a2, hashMap, false, false);
            if (packageCrashDatas != null) {
                String str20 = map.get("userId");
                if (str20 != null) {
                    x.c("[Native record info] userId: %s", str20);
                    packageCrashDatas.m = str20;
                }
                String str21 = map.get("sysLog");
                if (str21 != null) {
                    packageCrashDatas.w = str21;
                }
                String str22 = map.get("appVersion");
                if (str22 != null) {
                    x.c("[Native record info] appVersion: %s", str22);
                    packageCrashDatas.f = str22;
                }
                String str23 = map.get("isAppForeground");
                if (str23 != null) {
                    x.c("[Native record info] isAppForeground: %s", str23);
                    packageCrashDatas.N = str23.equalsIgnoreCase("true");
                }
                String str24 = map.get("launchTime");
                if (str24 != null) {
                    x.c("[Native record info] launchTime: %s", str24);
                    try {
                        packageCrashDatas.M = Long.parseLong(str24);
                    } catch (NumberFormatException e) {
                        if (!x.a(e)) {
                            e.printStackTrace();
                        }
                    }
                }
                packageCrashDatas.z = null;
                packageCrashDatas.k = true;
            }
            return packageCrashDatas;
        } catch (Throwable th) {
            x.e("error format", new Object[0]);
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String a(java.io.BufferedInputStream r4) throws java.io.IOException {
        /*
            r0 = 0
            if (r4 != 0) goto L4
            return r0
        L4:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L30
            r2 = 1024(0x400, float:1.435E-42)
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L30
        Lb:
            int r2 = r4.read()     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L3b
            r3 = -1
            if (r2 == r3) goto L27
            if (r2 != 0) goto L23
            java.lang.String r4 = new java.lang.String     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L3b
            byte[] r2 = r1.toByteArray()     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L3b
            java.lang.String r3 = "UTf-8"
            r4.<init>(r2, r3)     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L3b
            r1.close()
            return r4
        L23:
            r1.write(r2)     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L3b
            goto Lb
        L27:
            r1.close()
            goto L3a
        L2b:
            r4 = move-exception
            goto L32
        L2d:
            r4 = move-exception
            r1 = r0
            goto L3c
        L30:
            r4 = move-exception
            r1 = r0
        L32:
            com.tencent.bugly.proguard.x.a(r4)     // Catch: java.lang.Throwable -> L3b
            if (r1 == 0) goto L3a
            r1.close()
        L3a:
            return r0
        L3b:
            r4 = move-exception
        L3c:
            if (r1 == 0) goto L41
            r1.close()
        L41:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.b.a(java.io.BufferedInputStream):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String a(String str) {
        if (str == null) {
            return "";
        }
        String[] split = str.split("\n");
        if (split == null || split.length == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : split) {
            if (!str2.contains("java.lang.Thread.getStackTrace(")) {
                sb.append(str2);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String a(String str, int i, String str2, boolean z) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        if (str == null || i <= 0) {
            return null;
        }
        File file = new File(str);
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        x.a("Read system log from native record file(length: %s bytes): %s", Long.valueOf(file.length()), file.getAbsolutePath());
        a.add(file);
        x.c("Add this record file to list for cleaning lastly.", new Object[0]);
        if (str2 == null) {
            return z.a(new File(str), i, z);
        }
        StringBuilder sb = new StringBuilder();
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        if (Pattern.compile(str2 + "[ ]*:").matcher(readLine).find()) {
                            sb.append(readLine);
                            sb.append("\n");
                        }
                        if (i > 0 && sb.length() > i) {
                            if (z) {
                                sb.delete(i, sb.length());
                                break;
                            }
                            sb.delete(0, sb.length() - i);
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (Exception e) {
                                x.a(e);
                            }
                        }
                        throw th;
                    }
                }
                String sb2 = sb.toString();
                try {
                    bufferedReader.close();
                } catch (Exception e2) {
                    x.a(e2);
                }
                return sb2;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = bufferedReader2;
        }
    }

    public static String a(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String b = b(str, str2);
        if (b != null && !b.isEmpty()) {
            sb.append("Register infos:\n");
            sb.append(b);
        }
        String c = c(str, str2);
        if (c != null && !c.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append("System SO infos:\n");
            sb.append(c);
        }
        return sb.toString();
    }

    public static void a(boolean z, String str) {
        if (str != null) {
            a.add(new File(str, "rqd_record.eup"));
            a.add(new File(str, "reg_record.txt"));
            a.add(new File(str, "map_record.txt"));
            a.add(new File(str, "backup_record.txt"));
            if (z) {
                c(str);
            }
        }
        if (a == null || a.size() <= 0) {
            return;
        }
        for (File file : a) {
            if (file.exists() && file.canWrite()) {
                file.delete();
                x.c("Delete record file %s", file.getAbsoluteFile());
            }
        }
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        File file = new File(str, "backup_record.txt");
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x006f, code lost:
    
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0073, code lost:
    
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0074, code lost:
    
        com.tencent.bugly.proguard.x.a(r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String b(java.lang.String r9, java.lang.String r10) {
        /*
            java.lang.String r0 = "reg_record.txt"
            java.io.BufferedReader r9 = com.tencent.bugly.proguard.z.a(r9, r0)
            r0 = 0
            if (r9 != 0) goto La
            return r0
        La:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            r1.<init>()     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            java.lang.String r2 = r9.readLine()     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            if (r2 == 0) goto L6d
            boolean r10 = r2.startsWith(r10)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            if (r10 != 0) goto L1c
            goto L6d
        L1c:
            java.lang.String r10 = "                "
            r2 = 18
            r3 = 0
            r2 = 0
            r4 = 18
            r5 = 0
        L25:
            java.lang.String r6 = r9.readLine()     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            if (r6 == 0) goto L59
            int r7 = r2 % 4
            if (r7 != 0) goto L3c
            if (r2 <= 0) goto L36
            java.lang.String r5 = "\n"
            r1.append(r5)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
        L36:
            java.lang.String r5 = "  "
            r1.append(r5)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            goto L4f
        L3c:
            int r7 = r6.length()     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            r8 = 16
            if (r7 <= r8) goto L46
            r4 = 28
        L46:
            int r5 = r4 - r5
            java.lang.String r5 = r10.substring(r3, r5)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            r1.append(r5)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
        L4f:
            int r5 = r6.length()     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            r1.append(r6)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            int r2 = r2 + 1
            goto L25
        L59:
            java.lang.String r10 = "\n"
            r1.append(r10)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            java.lang.String r10 = r1.toString()     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L7a
            if (r9 == 0) goto L6c
            r9.close()     // Catch: java.lang.Exception -> L68
            goto L6c
        L68:
            r9 = move-exception
            com.tencent.bugly.proguard.x.a(r9)
        L6c:
            return r10
        L6d:
            if (r9 == 0) goto L77
            r9.close()     // Catch: java.lang.Exception -> L73
            goto L77
        L73:
            r9 = move-exception
            com.tencent.bugly.proguard.x.a(r9)
        L77:
            return r0
        L78:
            r10 = move-exception
            goto L89
        L7a:
            r10 = move-exception
            com.tencent.bugly.proguard.x.a(r10)     // Catch: java.lang.Throwable -> L78
            if (r9 == 0) goto L88
            r9.close()     // Catch: java.lang.Exception -> L84
            goto L88
        L84:
            r9 = move-exception
            com.tencent.bugly.proguard.x.a(r9)
        L88:
            return r0
        L89:
            if (r9 == 0) goto L93
            r9.close()     // Catch: java.lang.Exception -> L8f
            goto L93
        L8f:
            r9 = move-exception
            com.tencent.bugly.proguard.x.a(r9)
        L93:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.b.b(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0041, code lost:
    
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0045, code lost:
    
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0046, code lost:
    
        com.tencent.bugly.proguard.x.a(r3);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String c(java.lang.String r3, java.lang.String r4) {
        /*
            java.lang.String r0 = "map_record.txt"
            java.io.BufferedReader r3 = com.tencent.bugly.proguard.z.a(r3, r0)
            r0 = 0
            if (r3 != 0) goto La
            return r0
        La:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            r1.<init>()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            java.lang.String r2 = r3.readLine()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            if (r2 == 0) goto L3f
            boolean r4 = r2.startsWith(r4)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            if (r4 != 0) goto L1c
            goto L3f
        L1c:
            java.lang.String r4 = r3.readLine()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            if (r4 == 0) goto L30
            java.lang.String r2 = "  "
            r1.append(r2)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            r1.append(r4)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            java.lang.String r4 = "\n"
            r1.append(r4)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            goto L1c
        L30:
            java.lang.String r4 = r1.toString()     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c
            if (r3 == 0) goto L3e
            r3.close()     // Catch: java.lang.Exception -> L3a
            goto L3e
        L3a:
            r3 = move-exception
            com.tencent.bugly.proguard.x.a(r3)
        L3e:
            return r4
        L3f:
            if (r3 == 0) goto L49
            r3.close()     // Catch: java.lang.Exception -> L45
            goto L49
        L45:
            r3 = move-exception
            com.tencent.bugly.proguard.x.a(r3)
        L49:
            return r0
        L4a:
            r4 = move-exception
            goto L5b
        L4c:
            r4 = move-exception
            com.tencent.bugly.proguard.x.a(r4)     // Catch: java.lang.Throwable -> L4a
            if (r3 == 0) goto L5a
            r3.close()     // Catch: java.lang.Exception -> L56
            goto L5a
        L56:
            r3 = move-exception
            com.tencent.bugly.proguard.x.a(r3)
        L5a:
            return r0
        L5b:
            if (r3 == 0) goto L65
            r3.close()     // Catch: java.lang.Exception -> L61
            goto L65
        L61:
            r3 = move-exception
            com.tencent.bugly.proguard.x.a(r3)
        L65:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.b.c(java.lang.String, java.lang.String):java.lang.String");
    }

    public static void c(String str) {
        File[] listFiles;
        if (str == null) {
            return;
        }
        try {
            File file = new File(str);
            if (file.canRead() && file.isDirectory() && (listFiles = file.listFiles()) != null) {
                for (File file2 : listFiles) {
                    if (file2.canRead() && file2.canWrite() && file2.length() == 0) {
                        file2.delete();
                        x.c("Delete empty record file %s", file2.getAbsoluteFile());
                    }
                }
            }
        } catch (Throwable th) {
            x.a(th);
        }
    }

    private static Map<String, Integer> d(String str) {
        if (str == null) {
            return null;
        }
        try {
            HashMap hashMap = new HashMap();
            for (String str2 : str.split(",")) {
                String[] split = str2.split(":");
                if (split.length != 2) {
                    x.e("error format at %s", str2);
                    return null;
                }
                hashMap.put(split[0], Integer.valueOf(Integer.parseInt(split[1])));
            }
            return hashMap;
        } catch (Exception e) {
            x.e("error format intStateStr %s", str);
            e.printStackTrace();
            return null;
        }
    }
}
