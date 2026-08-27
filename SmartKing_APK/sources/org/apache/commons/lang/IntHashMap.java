package org.apache.commons.lang;

/* loaded from: classes2.dex */
class IntHashMap {
    private transient int count;
    private float loadFactor;
    private transient Entry[] table;
    private int threshold;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Entry {
        int hash;
        int key;
        Entry next;
        Object value;

        protected Entry(int i, int i2, Object obj, Entry entry) {
            this.hash = i;
            this.key = i2;
            this.value = obj;
            this.next = entry;
        }
    }

    public IntHashMap() {
        this(20, 0.75f);
    }

    public IntHashMap(int i) {
        this(i, 0.75f);
    }

    public IntHashMap(int i, float f) {
        if (i < 0) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Illegal Capacity: ");
            stringBuffer.append(i);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        if (f <= 0.0f) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Illegal Load: ");
            stringBuffer2.append(f);
            throw new IllegalArgumentException(stringBuffer2.toString());
        }
        i = i == 0 ? 1 : i;
        this.loadFactor = f;
        this.table = new Entry[i];
        this.threshold = (int) (i * f);
    }

    public synchronized void clear() {
        Entry[] entryArr = this.table;
        int length = entryArr.length;
        while (true) {
            length--;
            if (length >= 0) {
                entryArr[length] = null;
            } else {
                this.count = 0;
            }
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Entry[] entryArr = this.table;
        int length = entryArr.length;
        while (true) {
            int i = length - 1;
            if (length <= 0) {
                return false;
            }
            for (Entry entry = entryArr[i]; entry != null; entry = entry.next) {
                if (entry.value.equals(obj)) {
                    return true;
                }
            }
            length = i;
        }
    }

    public boolean containsKey(int i) {
        Entry[] entryArr = this.table;
        for (Entry entry = entryArr[(Integer.MAX_VALUE & i) % entryArr.length]; entry != null; entry = entry.next) {
            if (entry.hash == i) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Object obj) {
        return contains(obj);
    }

    public Object get(int i) {
        Entry[] entryArr = this.table;
        for (Entry entry = entryArr[(Integer.MAX_VALUE & i) % entryArr.length]; entry != null; entry = entry.next) {
            if (entry.hash == i) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public Object put(int i, Object obj) {
        Entry[] entryArr = this.table;
        int i2 = Integer.MAX_VALUE & i;
        int length = i2 % entryArr.length;
        for (Entry entry = entryArr[length]; entry != null; entry = entry.next) {
            if (entry.hash == i) {
                Object obj2 = entry.value;
                entry.value = obj;
                return obj2;
            }
        }
        if (this.count >= this.threshold) {
            rehash();
            entryArr = this.table;
            length = i2 % entryArr.length;
        }
        entryArr[length] = new Entry(i, i, obj, entryArr[length]);
        this.count++;
        return null;
    }

    protected void rehash() {
        int length = this.table.length;
        Entry[] entryArr = this.table;
        int i = (length * 2) + 1;
        Entry[] entryArr2 = new Entry[i];
        this.threshold = (int) (i * this.loadFactor);
        this.table = entryArr2;
        while (true) {
            int i2 = length - 1;
            if (length <= 0) {
                return;
            }
            Entry entry = entryArr[i2];
            while (entry != null) {
                Entry entry2 = entry.next;
                int i3 = (entry.hash & Integer.MAX_VALUE) % i;
                entry.next = entryArr2[i3];
                entryArr2[i3] = entry;
                entry = entry2;
            }
            length = i2;
        }
    }

    public Object remove(int i) {
        Entry[] entryArr = this.table;
        int length = (Integer.MAX_VALUE & i) % entryArr.length;
        Entry entry = null;
        for (Entry entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
            if (entry2.hash == i) {
                if (entry != null) {
                    entry.next = entry2.next;
                } else {
                    entryArr[length] = entry2.next;
                }
                this.count--;
                Object obj = entry2.value;
                entry2.value = null;
                return obj;
            }
            entry = entry2;
        }
        return null;
    }

    public int size() {
        return this.count;
    }
}
