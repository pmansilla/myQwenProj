package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    private Throwable createException(String str, Throwable th, Class<?> cls) throws Exception {
        Constructor<?> constructor = null;
        Constructor<?> constructor2 = null;
        Constructor<?> constructor3 = null;
        for (Constructor<?> constructor4 : cls.getConstructors()) {
            Class<?>[] parameterTypes = constructor4.getParameterTypes();
            if (parameterTypes.length == 0) {
                constructor3 = constructor4;
            } else if (parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                constructor2 = constructor4;
            } else if (parameterTypes.length == 2 && parameterTypes[0] == String.class && parameterTypes[1] == Throwable.class) {
                constructor = constructor4;
            }
        }
        if (constructor != null) {
            return (Throwable) constructor.newInstance(str, th);
        }
        if (constructor2 != null) {
            return (Throwable) constructor2.newInstance(str);
        }
        if (constructor3 != null) {
            return (Throwable) constructor3.newInstance(new Object[0]);
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0031, code lost:
    
        if (java.lang.Throwable.class.isAssignableFrom(r13) != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00f1, code lost:
    
        if (r3 != null) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00f3, code lost:
    
        r14 = (T) new java.lang.Exception(r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x010c, code lost:
    
        if (r5 == null) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x010e, code lost:
    
        ((java.lang.Throwable) r14).setStackTrace(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0111, code lost:
    
        if (r13 == null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0113, code lost:
    
        if (r3 == null) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0117, code lost:
    
        if (r3 != r11.clazz) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0119, code lost:
    
        r12 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x012b, code lost:
    
        if (r12 == null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x012d, code lost:
    
        r13 = r13.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0139, code lost:
    
        if (r13.hasNext() == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x013b, code lost:
    
        r0 = (java.util.Map.Entry) r13.next();
        r1 = (java.lang.String) r0.getKey();
        r0 = r0.getValue();
        r1 = r12.getFieldDeserializer(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x014f, code lost:
    
        if (r1 == null) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0151, code lost:
    
        r1.setValue(r14, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x011b, code lost:
    
        r12 = r12.getConfig().getDeserializer(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0125, code lost:
    
        if ((r12 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0127, code lost:
    
        r12 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x012a, code lost:
    
        r12 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0155, code lost:
    
        return (T) r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ff, code lost:
    
        if (java.lang.Throwable.class.isAssignableFrom(r3) == false) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0179, code lost:
    
        throw new com.alibaba.fastjson.JSONException("type not match, not Throwable. " + r3.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0101, code lost:
    
        r14 = (T) createException(r4, r0, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0105, code lost:
    
        if (r14 != null) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0107, code lost:
    
        r14 = (T) new java.lang.Exception(r4, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0156, code lost:
    
        r12 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x015e, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create instance error", r12);
     */
    @Override // com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r12, java.lang.reflect.Type r13, java.lang.Object r14) {
        /*
            Method dump skipped, instructions count: 386
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    @Override // com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }
}
