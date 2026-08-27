package com.loc;

import com.sun.mail.imap.IMAPStore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* compiled from: XmlUtils.java */
/* loaded from: classes.dex */
final class cw {
    public static final Object a(XmlPullParser xmlPullParser, String[] strArr) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        while (eventType != 2) {
            if (eventType == 3) {
                throw new XmlPullParserException("Unexpected end tag at: " + xmlPullParser.getName());
            }
            if (eventType == 4) {
                throw new XmlPullParserException("Unexpected text: " + xmlPullParser.getText());
            }
            try {
                eventType = xmlPullParser.next();
                if (eventType == 1) {
                    throw new XmlPullParserException("Unexpected end of document");
                }
            } catch (Exception unused) {
                throw new XmlPullParserException("Unexpected call next(): " + xmlPullParser.getName());
            }
        }
        return b(xmlPullParser, strArr);
    }

    private static HashMap a(XmlPullParser xmlPullParser, String str, String[] strArr) throws XmlPullParserException, IOException {
        HashMap hashMap = new HashMap();
        int eventType = xmlPullParser.getEventType();
        do {
            if (eventType == 2) {
                Object b = b(xmlPullParser, strArr);
                if (strArr[0] == null) {
                    throw new XmlPullParserException("Map value without name attribute: " + xmlPullParser.getName());
                }
                hashMap.put(strArr[0], b);
            } else if (eventType == 3) {
                if (xmlPullParser.getName().equals(str)) {
                    return hashMap;
                }
                throw new XmlPullParserException("Expected " + str + " end tag at: " + xmlPullParser.getName());
            }
            eventType = xmlPullParser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + str + " end tag");
    }

    private static void a(Object obj, String str, XmlSerializer xmlSerializer) throws XmlPullParserException, IOException {
        String str2;
        if (obj == null) {
            xmlSerializer.startTag(null, "null");
            if (str != null) {
                xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
            }
            xmlSerializer.endTag(null, "null");
            return;
        }
        if (obj instanceof String) {
            xmlSerializer.startTag(null, "string");
            if (str != null) {
                xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
            }
            xmlSerializer.text(obj.toString());
            xmlSerializer.endTag(null, "string");
            return;
        }
        if (obj instanceof Integer) {
            str2 = "int";
        } else if (obj instanceof Long) {
            str2 = "long";
        } else if (obj instanceof Float) {
            str2 = "float";
        } else if (obj instanceof Double) {
            str2 = "double";
        } else {
            if (!(obj instanceof Boolean)) {
                if (!(obj instanceof byte[])) {
                    if (obj instanceof int[]) {
                        a((int[]) obj, str, xmlSerializer);
                        return;
                    }
                    if (obj instanceof Map) {
                        a((Map) obj, str, xmlSerializer);
                        return;
                    }
                    if (obj instanceof List) {
                        a((List) obj, str, xmlSerializer);
                        return;
                    }
                    if (!(obj instanceof CharSequence)) {
                        throw new RuntimeException("writeValueXml: unable to write value " + obj);
                    }
                    xmlSerializer.startTag(null, "string");
                    if (str != null) {
                        xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
                    }
                    xmlSerializer.text(obj.toString());
                    xmlSerializer.endTag(null, "string");
                    return;
                }
                byte[] bArr = (byte[]) obj;
                if (bArr == null) {
                    xmlSerializer.startTag(null, "null");
                    xmlSerializer.endTag(null, "null");
                    return;
                }
                xmlSerializer.startTag(null, "byte-array");
                if (str != null) {
                    xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
                }
                xmlSerializer.attribute(null, "num", Integer.toString(bArr.length));
                StringBuilder sb = new StringBuilder(bArr.length * 2);
                for (byte b : bArr) {
                    int i = b >> 4;
                    sb.append(i >= 10 ? (i + 97) - 10 : i + 48);
                    int i2 = b & 255;
                    sb.append(i2 >= 10 ? (i2 + 97) - 10 : i2 + 48);
                }
                xmlSerializer.text(sb.toString());
                xmlSerializer.endTag(null, "byte-array");
                return;
            }
            str2 = "boolean";
        }
        xmlSerializer.startTag(null, str2);
        if (str != null) {
            xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
        }
        xmlSerializer.attribute(null, "value", obj.toString());
        xmlSerializer.endTag(null, str2);
    }

    private static void a(List list, String str, XmlSerializer xmlSerializer) throws XmlPullParserException, IOException {
        String str2;
        if (list == null) {
            xmlSerializer.startTag(null, "null");
            str2 = "null";
        } else {
            xmlSerializer.startTag(null, "list");
            if (str != null) {
                xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                a(list.get(i), (String) null, xmlSerializer);
            }
            str2 = "list";
        }
        xmlSerializer.endTag(null, str2);
    }

    public static final void a(Map map, String str, XmlSerializer xmlSerializer) throws XmlPullParserException, IOException {
        String str2;
        if (map == null) {
            xmlSerializer.startTag(null, "null");
            str2 = "null";
        } else {
            xmlSerializer.startTag(null, "map");
            if (str != null) {
                xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
            }
            for (Map.Entry entry : map.entrySet()) {
                a(entry.getValue(), (String) entry.getKey(), xmlSerializer);
            }
            str2 = "map";
        }
        xmlSerializer.endTag(null, str2);
    }

    private static void a(int[] iArr, String str, XmlSerializer xmlSerializer) throws XmlPullParserException, IOException {
        String str2;
        if (iArr == null) {
            xmlSerializer.startTag(null, "null");
            str2 = "null";
        } else {
            xmlSerializer.startTag(null, "int-array");
            if (str != null) {
                xmlSerializer.attribute(null, IMAPStore.ID_NAME, str);
            }
            xmlSerializer.attribute(null, "num", Integer.toString(iArr.length));
            for (int i : iArr) {
                xmlSerializer.startTag(null, "item");
                xmlSerializer.attribute(null, "value", Integer.toString(i));
                xmlSerializer.endTag(null, "item");
            }
            str2 = "int-array";
        }
        xmlSerializer.endTag(null, str2);
    }

    private static int[] a(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException {
        try {
            int[] iArr = new int[Integer.parseInt(xmlPullParser.getAttributeValue(null, "num"))];
            int i = 0;
            int eventType = xmlPullParser.getEventType();
            do {
                if (eventType == 2) {
                    if (!xmlPullParser.getName().equals("item")) {
                        throw new XmlPullParserException("Expected item tag at: " + xmlPullParser.getName());
                    }
                    try {
                        iArr[i] = Integer.parseInt(xmlPullParser.getAttributeValue(null, "value"));
                    } catch (NullPointerException unused) {
                        throw new XmlPullParserException("Need value attribute in item");
                    } catch (NumberFormatException unused2) {
                        throw new XmlPullParserException("Not a number in value attribute in item");
                    }
                } else if (eventType == 3) {
                    if (xmlPullParser.getName().equals(str)) {
                        return iArr;
                    }
                    if (!xmlPullParser.getName().equals("item")) {
                        throw new XmlPullParserException("Expected " + str + " end tag at: " + xmlPullParser.getName());
                    }
                    i++;
                }
                eventType = xmlPullParser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + str + " end tag");
        } catch (NullPointerException unused3) {
            throw new XmlPullParserException("Need num attribute in byte-array");
        } catch (NumberFormatException unused4) {
            throw new XmlPullParserException("Not a number in num attribute in byte-array");
        }
    }

    private static final Object b(XmlPullParser xmlPullParser, String[] strArr) throws XmlPullParserException, IOException {
        int next;
        Object d;
        Object obj = null;
        String attributeValue = xmlPullParser.getAttributeValue(null, IMAPStore.ID_NAME);
        String name = xmlPullParser.getName();
        if (!name.equals("null")) {
            if (name.equals("string")) {
                String str = "";
                while (true) {
                    int next2 = xmlPullParser.next();
                    if (next2 == 1) {
                        throw new XmlPullParserException("Unexpected end of document in <string>");
                    }
                    if (next2 == 3) {
                        if (xmlPullParser.getName().equals("string")) {
                            strArr[0] = attributeValue;
                            return str;
                        }
                        throw new XmlPullParserException("Unexpected end tag in <string>: " + xmlPullParser.getName());
                    }
                    if (next2 == 4) {
                        str = str + xmlPullParser.getText();
                    } else if (next2 == 2) {
                        throw new XmlPullParserException("Unexpected start tag in <string>: " + xmlPullParser.getName());
                    }
                }
            } else if (name.equals("int")) {
                obj = Integer.valueOf(Integer.parseInt(xmlPullParser.getAttributeValue(null, "value")));
            } else if (name.equals("long")) {
                obj = Long.valueOf(xmlPullParser.getAttributeValue(null, "value"));
            } else {
                if (name.equals("float")) {
                    d = new Float(xmlPullParser.getAttributeValue(null, "value"));
                } else if (name.equals("double")) {
                    d = new Double(xmlPullParser.getAttributeValue(null, "value"));
                } else {
                    if (!name.equals("boolean")) {
                        if (name.equals("int-array")) {
                            xmlPullParser.next();
                            int[] a = a(xmlPullParser, "int-array");
                            strArr[0] = attributeValue;
                            return a;
                        }
                        if (name.equals("map")) {
                            xmlPullParser.next();
                            HashMap a2 = a(xmlPullParser, "map", strArr);
                            strArr[0] = attributeValue;
                            return a2;
                        }
                        if (!name.equals("list")) {
                            throw new XmlPullParserException("Unknown tag: " + name);
                        }
                        xmlPullParser.next();
                        ArrayList b = b(xmlPullParser, "list", strArr);
                        strArr[0] = attributeValue;
                        return b;
                    }
                    obj = Boolean.valueOf(xmlPullParser.getAttributeValue(null, "value"));
                }
                obj = d;
            }
        }
        do {
            next = xmlPullParser.next();
            if (next == 1) {
                throw new XmlPullParserException("Unexpected end of document in <" + name + ">");
            }
            if (next == 3) {
                if (xmlPullParser.getName().equals(name)) {
                    strArr[0] = attributeValue;
                    return obj;
                }
                throw new XmlPullParserException("Unexpected end tag in <" + name + ">: " + xmlPullParser.getName());
            }
            if (next == 4) {
                throw new XmlPullParserException("Unexpected text in <" + name + ">: " + xmlPullParser.getName());
            }
        } while (next != 2);
        throw new XmlPullParserException("Unexpected start tag in <" + name + ">: " + xmlPullParser.getName());
    }

    private static ArrayList b(XmlPullParser xmlPullParser, String str, String[] strArr) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        int eventType = xmlPullParser.getEventType();
        do {
            if (eventType == 2) {
                arrayList.add(b(xmlPullParser, strArr));
            } else if (eventType == 3) {
                if (xmlPullParser.getName().equals(str)) {
                    return arrayList;
                }
                throw new XmlPullParserException("Expected " + str + " end tag at: " + xmlPullParser.getName());
            }
            eventType = xmlPullParser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + str + " end tag");
    }
}
