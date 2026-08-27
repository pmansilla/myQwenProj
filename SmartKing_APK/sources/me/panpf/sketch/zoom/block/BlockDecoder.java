package me.panpf.sketch.zoom.block;

import android.text.TextUtils;
import me.panpf.sketch.SLog;
import me.panpf.sketch.util.KeyCounter;
import me.panpf.sketch.zoom.BlockDisplayer;

/* loaded from: classes2.dex */
public class BlockDecoder {
    private static final String NAME = "BlockDecoder";
    private BlockDisplayer blockDisplayer;
    private ImageRegionDecoder decoder;
    private KeyCounter initKeyCounter = new KeyCounter();
    private boolean initializing;
    private boolean running;

    public BlockDecoder(BlockDisplayer blockDisplayer) {
        this.blockDisplayer = blockDisplayer;
    }

    void clean(String str) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "clean. %s", str);
        }
        this.initKeyCounter.refresh();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decodeBlock(Block block) {
        if (!isReady()) {
            SLog.w(NAME, "not ready. decodeBlock. %s", block.getInfo());
        } else {
            block.decoder = this.decoder;
            this.blockDisplayer.getBlockExecutor().submitDecodeBlock(block.getKey(), block);
        }
    }

    public ImageRegionDecoder getDecoder() {
        return this.decoder;
    }

    public void initCompleted(String str, ImageRegionDecoder imageRegionDecoder) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "init completed. %s", str);
        }
        this.initializing = false;
        this.decoder = imageRegionDecoder;
    }

    public void initError(String str, Exception exc) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "init failed. %s. %s", exc.getMessage(), str);
        }
        this.initializing = false;
    }

    public boolean isInitializing() {
        return this.running && this.initializing;
    }

    public boolean isReady() {
        return this.running && this.decoder != null && this.decoder.isReady();
    }

    public void recycle(String str) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "recycle. %s", str);
        }
        if (this.decoder != null) {
            this.decoder.recycle();
        }
    }

    public void setImage(String str, boolean z) {
        clean("setImage");
        if (this.decoder != null) {
            this.decoder.recycle();
            this.decoder = null;
        }
        if (TextUtils.isEmpty(str)) {
            this.initializing = false;
            this.running = false;
        } else {
            this.initializing = true;
            this.running = true;
            this.blockDisplayer.getBlockExecutor().submitInit(str, this.initKeyCounter, z);
        }
    }
}
