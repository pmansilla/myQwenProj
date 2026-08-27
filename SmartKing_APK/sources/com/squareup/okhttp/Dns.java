package com.squareup.okhttp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public interface Dns {
    public static final Dns SYSTEM = new Dns() { // from class: com.squareup.okhttp.Dns.1
        @Override // com.squareup.okhttp.Dns
        public List<InetAddress> lookup(String str) throws UnknownHostException {
            if (str != null) {
                return Arrays.asList(InetAddress.getAllByName(str));
            }
            throw new UnknownHostException("hostname == null");
        }
    };

    List<InetAddress> lookup(String str) throws UnknownHostException;
}
