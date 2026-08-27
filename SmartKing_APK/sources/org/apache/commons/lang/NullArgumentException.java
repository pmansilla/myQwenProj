package org.apache.commons.lang;

/* loaded from: classes2.dex */
public class NullArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1174360235354917591L;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NullArgumentException(java.lang.String r2) {
        /*
            r1 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            if (r2 != 0) goto L9
            java.lang.String r2 = "Argument"
        L9:
            r0.append(r2)
            java.lang.String r2 = " must not be null."
            r0.append(r2)
            java.lang.String r2 = r0.toString()
            r1.<init>(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.NullArgumentException.<init>(java.lang.String):void");
    }
}
