package ycble.runchinaup.core;

import java.util.ArrayList;
import java.util.List;
import ycble.runchinaup.util.PhoneDeviceUtil;

/* loaded from: classes2.dex */
public class PhoneBleExceptionCode {
    private static List<String> phoneBleExceptionCodeArrList = new ArrayList();

    static {
        phoneBleExceptionCodeArrList.add("Meizu_A_MX4 Pro_A_5.1.1_A_257");
    }

    private PhoneBleExceptionCode() {
    }

    public static String getPhoneCode(int i) {
        return PhoneDeviceUtil.getDeviceBrand() + "_A_" + PhoneDeviceUtil.getSystemModel() + "_A_" + PhoneDeviceUtil.getSystemVersion() + "_A_" + i;
    }

    public static boolean isPhoneBleExcepiton(int i) {
        return phoneBleExceptionCodeArrList.contains(getPhoneCode(i));
    }
}
