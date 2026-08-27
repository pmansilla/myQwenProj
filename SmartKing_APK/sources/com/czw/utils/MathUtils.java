package com.czw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/* loaded from: classes.dex */
public class MathUtils {
    public static float getAvg(float[] fArr) {
        if (fArr.length == 0) {
            return 0.0f;
        }
        int i = 0;
        for (float f : fArr) {
            i = (int) (i + f);
        }
        return i / r0;
    }

    public static int getAvg(int[] iArr) {
        if (iArr.length == 0) {
            return 0;
        }
        int length = iArr.length;
        int i = 0;
        for (int i2 : iArr) {
            i += i2;
        }
        return i / length;
    }

    public static float getMax(List<Float> list) {
        return getTmpArr(list).get(list.size() - 1).floatValue();
    }

    public static float getMax(float[] fArr) {
        return getTmpArr(fArr)[fArr.length - 1];
    }

    public static int getMax(int[] iArr) {
        return getTmpArr(iArr)[iArr.length - 1];
    }

    public static float getMin(float[] fArr) {
        return getTmpArr(fArr)[0];
    }

    public static int getMin(int[] iArr) {
        return getTmpArr(iArr)[0];
    }

    public static int getRandom(int i, int i2) {
        return (new Random().nextInt(i) % ((i - i2) + 1)) + i2;
    }

    static List<Float> getTmpArr(List<Float> list) {
        list.size();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(list);
        Collections.sort(arrayList);
        return arrayList;
    }

    static float[] getTmpArr(float[] fArr) {
        int length = fArr.length;
        float[] fArr2 = new float[length];
        System.arraycopy(fArr, 0, fArr2, 0, length);
        Arrays.sort(fArr2);
        return fArr2;
    }

    static int[] getTmpArr(int[] iArr) {
        int length = iArr.length;
        int[] iArr2 = new int[length];
        System.arraycopy(iArr, 0, iArr2, 0, length);
        Arrays.sort(iArr2);
        return iArr2;
    }
}
