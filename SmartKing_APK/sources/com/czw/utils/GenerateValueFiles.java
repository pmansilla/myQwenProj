package com.czw.utils;

import com.autonavi.amap.mapcore.tools.GlMapUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public class GenerateValueFiles {
    private static final String HTemplate = "<dimen name=\"y{0}\">{1}px</dimen>\n";
    private static final String SUPPORT_DIMESION = "320,480;480,800;480,854;540,960;600,1024;720,1184;720,1196;720,1280;768,1024;768,1280;800,1280;1080,1812;1080,1920;1440,2560;";
    private static final String VALUE_TEMPLATE = "values-{0}x{1}";
    private static final String WTemplate = "<dimen name=\"x{0}\">{1}px</dimen>\n";
    private int baseH;
    private int baseW;
    private String dirStr = "./res";
    private String supportStr = SUPPORT_DIMESION;

    public GenerateValueFiles(int i, int i2, String str) {
        this.baseW = i;
        this.baseH = i2;
        if (!this.supportStr.contains(i + "," + i2)) {
            this.supportStr += i + "," + i2 + ";";
        }
        this.supportStr += validateInput(str);
        System.out.println(str);
        File file = new File(this.dirStr);
        if (!file.exists()) {
            file.mkdir();
        }
        System.out.println(file.getAbsoluteFile());
    }

    public static float change(float f) {
        return ((int) (f * 100.0f)) / 100.0f;
    }

    private void generateXmlFile(int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        stringBuffer.append("<resources>");
        float f = (i * 1.0f) / this.baseW;
        System.out.println("width : " + i + "," + this.baseW + "," + f);
        for (int i3 = 1; i3 < this.baseW; i3++) {
            stringBuffer.append(WTemplate.replace("{0}", i3 + "").replace("{1}", change(((float) i3) * f) + ""));
        }
        stringBuffer.append(WTemplate.replace("{0}", this.baseW + "").replace("{1}", i + ""));
        stringBuffer.append("</resources>");
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        stringBuffer2.append("<resources>");
        float f2 = (i2 * 1.0f) / this.baseH;
        System.out.println("height : " + i2 + "," + this.baseH + "," + f2);
        for (int i4 = 1; i4 < this.baseH; i4++) {
            stringBuffer2.append(HTemplate.replace("{0}", i4 + "").replace("{1}", change(((float) i4) * f2) + ""));
        }
        stringBuffer2.append(HTemplate.replace("{0}", this.baseH + "").replace("{1}", i2 + ""));
        stringBuffer2.append("</resources>");
        StringBuilder sb = new StringBuilder();
        sb.append(this.dirStr);
        sb.append(File.separator);
        sb.append(VALUE_TEMPLATE.replace("{0}", i2 + "").replace("{1}", i + ""));
        File file = new File(sb.toString());
        file.mkdir();
        File file2 = new File(file.getAbsolutePath(), "lay_x.xml");
        File file3 = new File(file.getAbsolutePath(), "lay_y.xml");
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file2));
            printWriter.print(stringBuffer.toString());
            printWriter.close();
            PrintWriter printWriter2 = new PrintWriter(new FileOutputStream(file3));
            printWriter2.print(stringBuffer2.toString());
            printWriter2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] strArr) {
        int length;
        String str = "";
        int i = GlMapUtil.DEVICE_DISPLAY_DPI_HIGH;
        int i2 = GlMapUtil.DEVICE_DISPLAY_DPI_XHIGH;
        try {
            length = strArr.length;
            try {
            } catch (NumberFormatException e) {
                e = e;
            }
        } catch (NumberFormatException e2) {
            e = e2;
        }
        if (length >= 3) {
            length = Integer.parseInt(strArr[0]);
            int parseInt = Integer.parseInt(strArr[1]);
            try {
                str = strArr[2];
                i2 = parseInt;
            } catch (NumberFormatException e3) {
                e = e3;
                i2 = parseInt;
                i = length;
                System.err.println("right input params : java -jar xxx.jar width height w,h_w,h_..._w,h;");
                e.printStackTrace();
                System.exit(-1);
                new GenerateValueFiles(i, i2, str).generate();
            }
        } else {
            if (strArr.length < 2) {
                if (strArr.length >= 1) {
                    str = strArr[0];
                }
                new GenerateValueFiles(i, i2, str).generate();
            }
            length = Integer.parseInt(strArr[0]);
            i2 = Integer.parseInt(strArr[1]);
        }
        i = length;
        new GenerateValueFiles(i, i2, str).generate();
    }

    private String validateInput(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str2 : str.split("_")) {
            if (str2 != null) {
                try {
                    if (str2.trim().length() != 0) {
                        String[] split = str2.split(",");
                        stringBuffer.append(Integer.parseInt(split[0]) + "," + Integer.parseInt(split[1]) + ";");
                    }
                } catch (Exception unused) {
                    System.out.println("skip invalidate params : w,h = " + str2);
                }
            }
        }
        return stringBuffer.toString();
    }

    public void generate() {
        for (String str : this.supportStr.split(";")) {
            String[] split = str.split(",");
            generateXmlFile(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }
    }
}
