package me.panpf.sketch.decode;

import me.panpf.sketch.request.LoadRequest;

/* loaded from: classes2.dex */
public interface ResultProcessor {
    void process(LoadRequest loadRequest, DecodeResult decodeResult) throws ProcessException;
}
