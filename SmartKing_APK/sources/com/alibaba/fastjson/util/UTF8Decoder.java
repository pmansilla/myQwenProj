package com.alibaba.fastjson.util;

import com.alibaba.fastjson.asm.Opcodes;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/* loaded from: classes.dex */
public class UTF8Decoder extends CharsetDecoder {
    private static final Charset charset = Charset.forName("UTF-8");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Surrogate {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public static final int UCS4_MAX = 1114111;
        public static final int UCS4_MIN = 65536;

        private Surrogate() {
        }

        public static char high(int i) {
            return (char) ((((i - 65536) >> 10) & 1023) | 55296);
        }

        public static char low(int i) {
            return (char) (((i - 65536) & 1023) | 56320);
        }

        public static boolean neededFor(int i) {
            return i >= 65536 && i <= 1114111;
        }
    }

    public UTF8Decoder() {
        super(charset, 1.0f, 1.0f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0093, code lost:
    
        return xflow(r13, r5, r6, r14, r8, 2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ca, code lost:
    
        return xflow(r13, r5, r6, r14, r8, 3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0123, code lost:
    
        return xflow(r13, r5, r6, r14, r8, 4);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.nio.charset.CoderResult decodeArrayLoop(java.nio.ByteBuffer r13, java.nio.CharBuffer r14) {
        /*
            Method dump skipped, instructions count: 306
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.UTF8Decoder.decodeArrayLoop(java.nio.ByteBuffer, java.nio.CharBuffer):java.nio.charset.CoderResult");
    }

    private static boolean isMalformed2(int i, int i2) {
        return (i & 30) == 0 || (i2 & Opcodes.CHECKCAST) != 128;
    }

    private static boolean isMalformed3(int i, int i2, int i3) {
        return ((i != -32 || (i2 & 224) != 128) && (i2 & Opcodes.CHECKCAST) == 128 && (i3 & Opcodes.CHECKCAST) == 128) ? false : true;
    }

    private static boolean isMalformed4(int i, int i2, int i3) {
        return ((i & Opcodes.CHECKCAST) == 128 && (i2 & Opcodes.CHECKCAST) == 128 && (i3 & Opcodes.CHECKCAST) == 128) ? false : true;
    }

    private static boolean isNotContinuation(int i) {
        return (i & Opcodes.CHECKCAST) != 128;
    }

    private static CoderResult lookupN(ByteBuffer byteBuffer, int i) {
        for (int i2 = 1; i2 < i; i2++) {
            if (isNotContinuation(byteBuffer.get())) {
                return CoderResult.malformedForLength(i2);
            }
        }
        return CoderResult.malformedForLength(i);
    }

    private static CoderResult malformed(ByteBuffer byteBuffer, int i, CharBuffer charBuffer, int i2, int i3) {
        byteBuffer.position(i - byteBuffer.arrayOffset());
        CoderResult malformedN = malformedN(byteBuffer, i3);
        updatePositions(byteBuffer, i, charBuffer, i2);
        return malformedN;
    }

    public static CoderResult malformedN(ByteBuffer byteBuffer, int i) {
        switch (i) {
            case 1:
                byte b = byteBuffer.get();
                return (b >> 2) == -2 ? byteBuffer.remaining() < 4 ? CoderResult.UNDERFLOW : lookupN(byteBuffer, 5) : (b >> 1) == -2 ? byteBuffer.remaining() < 5 ? CoderResult.UNDERFLOW : lookupN(byteBuffer, 6) : CoderResult.malformedForLength(1);
            case 2:
                return CoderResult.malformedForLength(1);
            case 3:
                byte b2 = byteBuffer.get();
                byte b3 = byteBuffer.get();
                return CoderResult.malformedForLength(((b2 == -32 && (b3 & 224) == 128) || isNotContinuation(b3)) ? 1 : 2);
            case 4:
                int i2 = byteBuffer.get() & 255;
                int i3 = byteBuffer.get() & 255;
                return (i2 > 244 || (i2 == 240 && (i3 < 144 || i3 > 191)) || ((i2 == 244 && (i3 & GlMapUtil.DEVICE_DISPLAY_DPI_MEDIAN) != 128) || isNotContinuation(i3))) ? CoderResult.malformedForLength(1) : isNotContinuation(byteBuffer.get()) ? CoderResult.malformedForLength(2) : CoderResult.malformedForLength(3);
            default:
                throw new IllegalStateException();
        }
    }

    static void updatePositions(Buffer buffer, int i, Buffer buffer2, int i2) {
        buffer.position(i);
        buffer2.position(i2);
    }

    private static CoderResult xflow(Buffer buffer, int i, int i2, Buffer buffer2, int i3, int i4) {
        updatePositions(buffer, i, buffer2, i3);
        return (i4 == 0 || i2 - i < i4) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
    }

    @Override // java.nio.charset.CharsetDecoder
    protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        return decodeArrayLoop(byteBuffer, charBuffer);
    }
}
