package org.apache.commons.lang.math;

import java.util.Random;
import kotlin.jvm.internal.LongCompanionObject;

/* loaded from: classes2.dex */
public final class JVMRandom extends Random {
    private static final long serialVersionUID = 1;
    private boolean constructed;

    public JVMRandom() {
        this.constructed = false;
        this.constructed = true;
    }

    public static long nextLong(long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("Upper bound for nextInt must be positive");
        }
        double random = Math.random();
        double d = j;
        Double.isNaN(d);
        return (long) (random * d);
    }

    @Override // java.util.Random
    public boolean nextBoolean() {
        return Math.random() > 0.5d;
    }

    @Override // java.util.Random
    public void nextBytes(byte[] bArr) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Random
    public double nextDouble() {
        return Math.random();
    }

    @Override // java.util.Random
    public float nextFloat() {
        return (float) Math.random();
    }

    @Override // java.util.Random
    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Random
    public int nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    @Override // java.util.Random
    public int nextInt(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Upper bound for nextInt must be positive");
        }
        double random = Math.random();
        double d = i;
        Double.isNaN(d);
        return (int) (random * d);
    }

    @Override // java.util.Random
    public long nextLong() {
        return nextLong(LongCompanionObject.MAX_VALUE);
    }

    @Override // java.util.Random
    public synchronized void setSeed(long j) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }
}
