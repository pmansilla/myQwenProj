package npUpdate.nopointer.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AeUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SignMd5Util.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0002J\u001d\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n2\u0006\u0010\f\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010\rJ\u0006\u0010\u000e\u001a\u00020\u0006J\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0011J\u001a\u0010\u0012\u001a\u0004\u0018\u00010\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0014\u001a\u00020\u0006J/\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0016\u0018\u00010\n2\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\bH\u0002¢\u0006\u0002\u0010\u001cR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"LnpUpdate/nopointer/util/SignMd5Util;", "", "()V", "HEX_DIGITS", "", "bytes2HexString", "", "bytes", "", "getAppSignature", "", "Landroid/content/pm/Signature;", "packageName", "(Ljava/lang/String;)[Landroid/content/pm/Signature;", "getAppSignatureMD5", "getSignMD5FromApk", AmapLoc.TYPE_OFFLINE_CELL, "Ljava/io/File;", "hashTemplate", AeUtil.ROOT_DATA_PATH_OLD_NAME, "algorithm", "loadCertificates", "Ljava/security/cert/Certificate;", "jarFile", "Ljava/util/jar/JarFile;", "je", "Ljava/util/jar/JarEntry;", "readBuffer", "(Ljava/util/jar/JarFile;Ljava/util/jar/JarEntry;[B)[Ljava/security/cert/Certificate;", "npUpdate_release"}, k = 1, mv = {1, 1, 15})
/* loaded from: classes2.dex */
public final class SignMd5Util {
    public static final SignMd5Util INSTANCE = new SignMd5Util();
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private SignMd5Util() {
    }

    private final String bytes2HexString(byte[] bytes) {
        int length;
        if (bytes == null || (length = bytes.length) <= 0) {
            return "";
        }
        char[] cArr = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr[i] = HEX_DIGITS[(bytes[i2] >> 4) & 15];
            i = i3 + 1;
            cArr[i3] = HEX_DIGITS[(byte) (bytes[i2] & 15)];
        }
        return new String(cArr);
    }

    private final Signature[] getAppSignature(String packageName) {
        if (packageName.length() == 0) {
            return null;
        }
        try {
            PackageInfo packageInfo = Utils.getApp().getPackageManager().getPackageInfo(packageName, 64);
            if (packageInfo != null) {
                return packageInfo.signatures;
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        try {
            InputStream inputStream = jarFile.getInputStream(je);
            do {
            } while (inputStream.read(readBuffer, 0, readBuffer.length) != -1);
            inputStream.close();
            if (je != null) {
                return je.getCertificates();
            }
            return null;
        } catch (IOException unused) {
            return null;
        }
    }

    @NotNull
    public final String getAppSignatureMD5() {
        Signature[] appSignature;
        String packageName = Utils.getApp().getPackageName();
        Intrinsics.checkExpressionValueIsNotNull(packageName, "packageName");
        if (!(packageName.length() == 0) && (appSignature = getAppSignature(packageName)) != null) {
            if (!(appSignature.length == 0)) {
                return new Regex("(?<=[0-9A-F]{2})[0-9A-F]{2}").replace(bytes2HexString(hashTemplate(appSignature[0].toByteArray(), "MD5")), ":$0");
            }
        }
        return "";
    }

    @NotNull
    public final String getSignMD5FromApk(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "file");
        ArrayList arrayList = new ArrayList();
        JarFile jarFile = new JarFile(file);
        try {
            Certificate[] loadCertificates = loadCertificates(jarFile, jarFile.getJarEntry("AndroidManifest.xml"), new byte[8192]);
            if (loadCertificates != null) {
                for (Certificate certificate : loadCertificates) {
                    arrayList.add(new Regex("(?<=[0-9A-F]{2})[0-9A-F]{2}").replace(bytes2HexString(hashTemplate(certificate.getEncoded(), "MD5")), ":$0"));
                }
            }
        } catch (Exception unused) {
        }
        String str = (String) CollectionsKt.getOrNull(arrayList, 0);
        return str != null ? str : "";
    }

    @Nullable
    public final byte[] hashTemplate(@Nullable byte[] data, @NotNull String algorithm) {
        Intrinsics.checkParameterIsNotNull(algorithm, "algorithm");
        if (data != null) {
            if (!(data.length == 0)) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                    messageDigest.update(data);
                    return messageDigest.digest();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
