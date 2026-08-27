package com.mob.tools.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class MultiPart extends HTTPPart {
    private ArrayList<HTTPPart> parts = new ArrayList<>();

    public MultiPart append(HTTPPart hTTPPart) throws Throwable {
        this.parts.add(hTTPPart);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        MultiPartInputStream multiPartInputStream = new MultiPartInputStream();
        Iterator<HTTPPart> it = this.parts.iterator();
        while (it.hasNext()) {
            multiPartInputStream.addInputStream(it.next().getInputStream());
        }
        return multiPartInputStream;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        Iterator<HTTPPart> it = this.parts.iterator();
        long j = 0;
        while (it.hasNext()) {
            j += it.next().length();
        }
        return j;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<HTTPPart> it = this.parts.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        return sb.toString();
    }
}
