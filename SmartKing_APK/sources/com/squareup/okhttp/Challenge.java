package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;

/* loaded from: classes2.dex */
public final class Challenge {
    private final String realm;
    private final String scheme;

    public Challenge(String str, String str2) {
        this.scheme = str;
        this.realm = str2;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Challenge) {
            Challenge challenge = (Challenge) obj;
            if (Util.equal(this.scheme, challenge.scheme) && Util.equal(this.realm, challenge.realm)) {
                return true;
            }
        }
        return false;
    }

    public String getRealm() {
        return this.realm;
    }

    public String getScheme() {
        return this.scheme;
    }

    public int hashCode() {
        return ((899 + (this.realm != null ? this.realm.hashCode() : 0)) * 31) + (this.scheme != null ? this.scheme.hashCode() : 0);
    }

    public String toString() {
        return this.scheme + " realm=\"" + this.realm + "\"";
    }
}
