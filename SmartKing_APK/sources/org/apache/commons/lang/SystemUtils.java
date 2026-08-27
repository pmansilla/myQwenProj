package org.apache.commons.lang;

import com.amap.location.common.model.AmapLoc;
import java.io.File;
import java.io.PrintStream;

/* loaded from: classes2.dex */
public class SystemUtils {
    public static final boolean IS_JAVA_1_1;
    public static final boolean IS_JAVA_1_2;
    public static final boolean IS_JAVA_1_3;
    public static final boolean IS_JAVA_1_4;
    public static final boolean IS_JAVA_1_5;
    public static final boolean IS_JAVA_1_6;
    public static final boolean IS_OS_AIX;
    public static final boolean IS_OS_HP_UX;
    public static final boolean IS_OS_IRIX;
    public static final boolean IS_OS_LINUX;
    public static final boolean IS_OS_MAC;
    public static final boolean IS_OS_MAC_OSX;
    public static final boolean IS_OS_OS2;
    public static final boolean IS_OS_SOLARIS;
    public static final boolean IS_OS_SUN_OS;
    public static final boolean IS_OS_UNIX;
    public static final boolean IS_OS_WINDOWS;
    public static final boolean IS_OS_WINDOWS_2000;
    public static final boolean IS_OS_WINDOWS_95;
    public static final boolean IS_OS_WINDOWS_98;
    public static final boolean IS_OS_WINDOWS_ME;
    public static final boolean IS_OS_WINDOWS_NT;
    public static final boolean IS_OS_WINDOWS_VISTA;
    public static final boolean IS_OS_WINDOWS_XP;
    public static final float JAVA_VERSION_FLOAT;
    public static final int JAVA_VERSION_INT;
    public static final String JAVA_VERSION_TRIMMED;
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    public static final String USER_COUNTRY;
    public static final String USER_DIR;
    private static final String USER_DIR_KEY = "user.dir";
    public static final String USER_HOME;
    private static final String USER_HOME_KEY = "user.home";
    public static final String USER_LANGUAGE;
    public static final String USER_NAME;
    public static final String USER_TIMEZONE;
    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
    private static final String JAVA_HOME_KEY = "java.home";
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
    public static final String OS_ARCH = getSystemProperty("os.arch");
    public static final String OS_NAME = getSystemProperty("os.name");
    public static final String OS_VERSION = getSystemProperty("os.version");
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

    static {
        USER_COUNTRY = getSystemProperty(getSystemProperty("user.country") == null ? "user.region" : "user.country");
        USER_DIR = getSystemProperty(USER_DIR_KEY);
        USER_HOME = getSystemProperty(USER_HOME_KEY);
        USER_LANGUAGE = getSystemProperty("user.language");
        USER_NAME = getSystemProperty("user.name");
        USER_TIMEZONE = getSystemProperty("user.timezone");
        JAVA_VERSION_TRIMMED = getJavaVersionTrimmed();
        JAVA_VERSION_FLOAT = getJavaVersionAsFloat();
        JAVA_VERSION_INT = getJavaVersionAsInt();
        IS_JAVA_1_1 = getJavaVersionMatches("1.1");
        IS_JAVA_1_2 = getJavaVersionMatches("1.2");
        IS_JAVA_1_3 = getJavaVersionMatches("1.3");
        IS_JAVA_1_4 = getJavaVersionMatches("1.4");
        IS_JAVA_1_5 = getJavaVersionMatches("1.5");
        IS_JAVA_1_6 = getJavaVersionMatches("1.6");
        IS_OS_AIX = getOSMatches("AIX");
        IS_OS_HP_UX = getOSMatches("HP-UX");
        IS_OS_IRIX = getOSMatches("Irix");
        IS_OS_LINUX = getOSMatches("Linux") || getOSMatches("LINUX");
        IS_OS_MAC = getOSMatches("Mac");
        IS_OS_MAC_OSX = getOSMatches("Mac OS X");
        IS_OS_OS2 = getOSMatches("OS/2");
        IS_OS_SOLARIS = getOSMatches("Solaris");
        IS_OS_SUN_OS = getOSMatches("SunOS");
        IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS;
        IS_OS_WINDOWS = getOSMatches(OS_NAME_WINDOWS_PREFIX);
        IS_OS_WINDOWS_2000 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.0");
        IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");
        IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");
        IS_OS_WINDOWS_ME = getOSMatches(OS_NAME_WINDOWS_PREFIX, "4.9");
        IS_OS_WINDOWS_NT = getOSMatches("Windows NT");
        IS_OS_WINDOWS_XP = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.1");
        IS_OS_WINDOWS_VISTA = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.0");
    }

    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    public static float getJavaVersion() {
        return JAVA_VERSION_FLOAT;
    }

    private static float getJavaVersionAsFloat() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0.0f;
        }
        String substring = JAVA_VERSION_TRIMMED.substring(0, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(substring);
            stringBuffer.append(JAVA_VERSION_TRIMMED.substring(4, 5));
            substring = stringBuffer.toString();
        }
        try {
            return Float.parseFloat(substring);
        } catch (Exception unused) {
            return 0.0f;
        }
    }

    private static int getJavaVersionAsInt() {
        String stringBuffer;
        if (JAVA_VERSION_TRIMMED == null) {
            return 0;
        }
        String substring = JAVA_VERSION_TRIMMED.substring(0, 1);
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(substring);
        stringBuffer2.append(JAVA_VERSION_TRIMMED.substring(2, 3));
        String stringBuffer3 = stringBuffer2.toString();
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(stringBuffer3);
            stringBuffer4.append(JAVA_VERSION_TRIMMED.substring(4, 5));
            stringBuffer = stringBuffer4.toString();
        } else {
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append(stringBuffer3);
            stringBuffer5.append(AmapLoc.RESULT_TYPE_GPS);
            stringBuffer = stringBuffer5.toString();
        }
        try {
            return Integer.parseInt(stringBuffer);
        } catch (Exception unused) {
            return 0;
        }
    }

    private static boolean getJavaVersionMatches(String str) {
        if (JAVA_VERSION_TRIMMED == null) {
            return false;
        }
        return JAVA_VERSION_TRIMMED.startsWith(str);
    }

    private static String getJavaVersionTrimmed() {
        if (JAVA_VERSION == null) {
            return null;
        }
        for (int i = 0; i < JAVA_VERSION.length(); i++) {
            char charAt = JAVA_VERSION.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                return JAVA_VERSION.substring(i);
            }
        }
        return null;
    }

    private static boolean getOSMatches(String str) {
        if (OS_NAME == null) {
            return false;
        }
        return OS_NAME.startsWith(str);
    }

    private static boolean getOSMatches(String str, String str2) {
        return OS_NAME != null && OS_VERSION != null && OS_NAME.startsWith(str) && OS_VERSION.startsWith(str2);
    }

    private static String getSystemProperty(String str) {
        try {
            return System.getProperty(str);
        } catch (SecurityException unused) {
            PrintStream printStream = System.err;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Caught a SecurityException reading the system property '");
            stringBuffer.append(str);
            stringBuffer.append("'; the SystemUtils property value will default to null.");
            printStream.println(stringBuffer.toString());
            return null;
        }
    }

    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    public static boolean isJavaAwtHeadless() {
        if (JAVA_AWT_HEADLESS != null) {
            return JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString());
        }
        return false;
    }

    public static boolean isJavaVersionAtLeast(float f) {
        return JAVA_VERSION_FLOAT >= f;
    }

    public static boolean isJavaVersionAtLeast(int i) {
        return JAVA_VERSION_INT >= i;
    }
}
