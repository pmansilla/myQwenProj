package javax.mail.search;

/* loaded from: classes2.dex */
public abstract class StringTerm extends SearchTerm {
    private static final long serialVersionUID = 1274042129007696269L;
    protected boolean ignoreCase;
    protected String pattern;

    /* JADX INFO: Access modifiers changed from: protected */
    public StringTerm(String str) {
        this.pattern = str;
        this.ignoreCase = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StringTerm(String str, boolean z) {
        this.pattern = str;
        this.ignoreCase = z;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof StringTerm)) {
            return false;
        }
        StringTerm stringTerm = (StringTerm) obj;
        return this.ignoreCase ? stringTerm.pattern.equalsIgnoreCase(this.pattern) && stringTerm.ignoreCase == this.ignoreCase : stringTerm.pattern.equals(this.pattern) && stringTerm.ignoreCase == this.ignoreCase;
    }

    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    public String getPattern() {
        return this.pattern;
    }

    public int hashCode() {
        return this.ignoreCase ? this.pattern.hashCode() : this.pattern.hashCode() ^ (-1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean match(String str) {
        int length = str.length() - this.pattern.length();
        for (int i = 0; i <= length; i++) {
            if (str.regionMatches(this.ignoreCase, i, this.pattern, 0, this.pattern.length())) {
                return true;
            }
        }
        return false;
    }
}
