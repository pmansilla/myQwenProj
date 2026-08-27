package me.panpf.sketch.zoom.block;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import com.tencent.bugly.Bugly;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import me.panpf.sketch.ErrorTracker;
import me.panpf.sketch.SLog;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.util.ObjectPool;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.zoom.BlockDisplayer;
import me.panpf.sketch.zoom.Size;
import me.panpf.sketch.zoom.block.DecodeHandler;

/* loaded from: classes2.dex */
public class BlockManager {
    private static final String NAME = "BlockManager";
    private BitmapPool bitmapPool;
    private BlockDisplayer blockDisplayer;
    private Context context;
    public int blockBaseNumber = 3;
    public Rect drawRect = new Rect();
    public Rect decodeRect = new Rect();
    public Rect drawSrcRect = new Rect();
    public Rect decodeSrcRect = new Rect();
    public List<Block> blockList = new LinkedList();
    public Rect visibleRect = new Rect();
    private ObjectPool<Block> blockPool = new ObjectPool<>(new ObjectPool.ObjectFactory<Block>() { // from class: me.panpf.sketch.zoom.block.BlockManager.1
        @Override // me.panpf.sketch.util.ObjectPool.ObjectFactory
        public Block newObject() {
            return new Block();
        }
    }, 60);
    private ObjectPool<Rect> rectPool = new ObjectPool<>(new ObjectPool.ObjectFactory<Rect>() { // from class: me.panpf.sketch.zoom.block.BlockManager.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // me.panpf.sketch.util.ObjectPool.ObjectFactory
        public Rect newObject() {
            return new Rect();
        }
    }, 20);

    public BlockManager(Context context, BlockDisplayer blockDisplayer) {
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.bitmapPool = Sketch.with(applicationContext).getConfiguration().getBitmapPool();
        this.blockDisplayer = blockDisplayer;
    }

    private void calculateBlocksDecodeRect(Rect rect, Rect rect2, int i, int i2, int i3, int i4, int i5, int i6) {
        if (this.blockDisplayer.getZoomScale() != this.blockDisplayer.getLastZoomScale() || this.decodeRect.isEmpty()) {
            rect.set(rect2);
            return;
        }
        rect.set(this.decodeRect);
        int round = Math.round(i * 0.8f);
        int round2 = Math.round(i2 * 0.8f);
        int abs = Math.abs(rect2.left - rect.left);
        int abs2 = Math.abs(rect2.top - rect.top);
        int abs3 = Math.abs(rect2.right - rect.right);
        int abs4 = Math.abs(rect2.bottom - rect.bottom);
        if (rect2.left < rect.left) {
            if (rect2.left == 0) {
                rect.left = 0;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect left to 0, newDecodeRect=%s", rect.toShortString());
                }
            } else if (abs > round || rect.left - i3 <= 0) {
                while (rect.left > rect2.left) {
                    rect.left = Math.max(0, rect.left - i3);
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "decode rect left expand %d, newDecodeRect=%s", Integer.valueOf(i3), rect.toShortString());
                    }
                }
            }
        }
        if (rect2.top < rect.top) {
            if (rect2.top == 0) {
                rect.top = 0;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect top to 0, newDecodeRect=%s", rect.toShortString());
                }
            } else if (abs2 > round2 || rect.top - i4 <= 0) {
                while (rect.top > rect2.top) {
                    rect.top = Math.max(0, rect.top - i4);
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "decode rect top expand %d, newDecodeRect=%s", Integer.valueOf(i4), rect.toShortString());
                    }
                }
            }
        }
        if (rect2.right > rect.right) {
            if (rect2.right == i5) {
                rect.right = i5;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect right to %d, newDecodeRect=%s", Integer.valueOf(i5), rect.toShortString());
                }
            } else if (abs3 > round || rect.right + i3 >= i5) {
                while (rect.right < rect2.right) {
                    rect.right = Math.min(i5, rect.right + i3);
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "decode rect right expand %d, newDecodeRect=%s", Integer.valueOf(i3), rect.toShortString());
                    }
                }
            }
        }
        if (rect2.bottom > rect.bottom) {
            if (rect2.bottom > i6) {
                rect.bottom = i6;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect bottom to %d, newDecodeRect=%s", Integer.valueOf(i6), rect.toShortString());
                }
            } else if (abs4 > round2 || rect.bottom + i4 >= i6) {
                while (rect.bottom < rect2.bottom) {
                    rect.bottom = Math.min(i6, rect.bottom + i4);
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "decode rect bottom expand %d, newDecodeRect=%s", Integer.valueOf(i4), rect.toShortString());
                    }
                }
            }
        }
        while (true) {
            if (rect.left + i3 >= rect2.left && rect.top + i4 >= rect2.top && rect.right - i3 <= rect2.right && rect.bottom - i4 <= rect2.bottom) {
                return;
            }
            if (rect.left + i3 < rect2.left) {
                rect.left += i3;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect left reduced %d, newDecodeRect=%s", Integer.valueOf(i3), rect.toShortString());
                }
            }
            if (rect.top + i4 < rect2.top) {
                rect.top += i4;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect top reduced %d, newDecodeRect=%s", Integer.valueOf(i4), rect.toShortString());
                }
            }
            if (rect.right - i3 > rect2.right) {
                rect.right -= i3;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect right reduced %d, newDecodeRect=%s", Integer.valueOf(i3), rect.toShortString());
                }
            }
            if (rect.bottom - i4 > rect2.bottom) {
                rect.bottom -= i4;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "decode rect bottom reduced %d, newDecodeRect=%s", Integer.valueOf(i4), rect.toShortString());
                }
            }
        }
    }

    private int calculateInSampleSize(int i, int i2, int i3, int i4) {
        float f = (this.blockBaseNumber / 10.0f) + 1.0f;
        return Sketch.with(this.context).getConfiguration().getSizeCalculator().calculateInSampleSize(i, i2, Math.round(i3 * f), Math.round(i4 * f), false);
    }

    private void calculateSrcRect(Rect rect, Rect rect2, int i, int i2, float f, float f2) {
        rect.left = Math.max(0, Math.round(rect2.left * f));
        rect.top = Math.max(0, Math.round(rect2.top * f2));
        rect.right = Math.min(i, Math.round(rect2.right * f));
        rect.bottom = Math.min(i2, Math.round(rect2.bottom * f2));
    }

    private boolean canLoad(int i, int i2, int i3, int i4) {
        for (Block block : this.blockList) {
            if (block.drawRect.left == i && block.drawRect.top == i2 && block.drawRect.right == i3 && block.drawRect.bottom == i4) {
                return false;
            }
        }
        return true;
    }

    private List<Rect> findEmptyRect(Rect rect, List<Block> list) {
        Block block = null;
        if (rect.isEmpty()) {
            return null;
        }
        if (list == null || list.size() == 0) {
            Rect rect2 = this.rectPool.get();
            rect2.set(rect);
            LinkedList linkedList = new LinkedList();
            linkedList.add(rect2);
            return linkedList;
        }
        Comparator<Block> comparator = new Comparator<Block>() { // from class: me.panpf.sketch.zoom.block.BlockManager.3
            @Override // java.util.Comparator
            public int compare(Block block2, Block block3) {
                return ((block2.drawRect.top > block3.drawRect.top || block2.drawRect.bottom < block3.drawRect.bottom) && (block2.drawRect.top < block3.drawRect.top || block2.drawRect.bottom > block3.drawRect.bottom)) ? block2.drawRect.top - block3.drawRect.top : block2.drawRect.left - block3.drawRect.left;
            }
        };
        try {
            Collections.sort(list, comparator);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ErrorTracker errorTracker = Sketch.with(this.context).getConfiguration().getErrorTracker();
            errorTracker.onBlockSortError(e, list, false);
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            try {
                Collections.sort(list, comparator);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
                errorTracker.onBlockSortError(e, list, true);
            }
            System.setProperty("java.util.Arrays.useLegacyMergeSort", Bugly.SDK_IS_DEV);
        }
        int i = rect.left;
        int i2 = rect.top;
        Iterator<Block> it = list.iterator();
        LinkedList linkedList2 = null;
        int i3 = i2;
        int i4 = 0;
        int i5 = -1;
        while (it.hasNext()) {
            Block next = it.next();
            if (block == null || next.drawRect.top >= i5) {
                if (block != null && block.drawRect.right < rect.right) {
                    Rect rect3 = this.rectPool.get();
                    rect3.set(block.drawRect.right, i3, rect.right, i5);
                    if (linkedList2 == null) {
                        linkedList2 = new LinkedList();
                    }
                    linkedList2.add(rect3);
                }
                if (i5 == -1) {
                    i5 = i3;
                }
                int i6 = next.drawRect.bottom;
                if (next.drawRect.left > i) {
                    Rect rect4 = this.rectPool.get();
                    rect4.set(i, next.drawRect.top, next.drawRect.left, next.drawRect.bottom);
                    if (linkedList2 == null) {
                        linkedList2 = new LinkedList();
                    }
                    linkedList2.add(rect4);
                }
                if (next.drawRect.top > i5) {
                    Rect rect5 = this.rectPool.get();
                    rect5.set(i, i5, next.drawRect.right, next.drawRect.top);
                    if (linkedList2 == null) {
                        linkedList2 = new LinkedList();
                    }
                    linkedList2.add(rect5);
                }
                i4 = next.drawRect.right;
                i3 = i5;
                i5 = i6;
            } else {
                if (next.drawRect.bottom == block.drawRect.bottom) {
                    if (next.drawRect.left > i4) {
                        Rect rect6 = this.rectPool.get();
                        rect6.set(i4, i3, next.drawRect.left, i5);
                        if (linkedList2 == null) {
                            linkedList2 = new LinkedList();
                        }
                        linkedList2.add(rect6);
                    }
                    if (next.drawRect.top > i3) {
                        Rect rect7 = this.rectPool.get();
                        rect7.set(next.drawRect.left, i3, next.drawRect.right, next.drawRect.top);
                        if (linkedList2 == null) {
                            linkedList2 = new LinkedList();
                        }
                        linkedList2.add(rect7);
                    }
                    i4 = next.drawRect.right;
                } else {
                    it.remove();
                }
            }
            block = next;
        }
        if (i4 < rect.right) {
            Rect rect8 = this.rectPool.get();
            rect8.set(i4, i3, rect.right, i5);
            if (linkedList2 == null) {
                linkedList2 = new LinkedList();
            }
            linkedList2.add(rect8);
        }
        if (i5 < rect.bottom) {
            Rect rect9 = this.rectPool.get();
            rect9.set(rect.left, i5, rect.right, rect.bottom);
            if (linkedList2 == null) {
                linkedList2 = new LinkedList();
            }
            linkedList2.add(rect9);
        }
        return linkedList2;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadBlocks(java.util.List<android.graphics.Rect> r17, int r18, int r19, int r20, int r21, float r22, float r23, int r24, android.graphics.Rect r25) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: me.panpf.sketch.zoom.block.BlockManager.loadBlocks(java.util.List, int, int, int, int, float, float, int, android.graphics.Rect):void");
    }

    private void recycleBlocks(List<Block> list, Rect rect) {
        Iterator<Block> it = list.iterator();
        while (it.hasNext()) {
            Block next = it.next();
            if (this.blockDisplayer.getZoomScale() != next.scale || !SketchUtils.isCross(next.drawRect, rect)) {
                if (next.isEmpty()) {
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "recycle loading block and refresh key. block=%s", next.getInfo());
                    }
                    next.refreshKey();
                    it.remove();
                } else {
                    if (SLog.isLoggable(1048578)) {
                        SLog.d(NAME, "recycle block. block=%s", next.getInfo());
                    }
                    it.remove();
                    next.clean(this.bitmapPool);
                    this.blockPool.put(next);
                }
            }
        }
    }

    public void clean(String str) {
        for (Block block : this.blockList) {
            block.refreshKey();
            block.clean(this.bitmapPool);
            this.blockPool.put(block);
            if (SLog.isLoggable(1048578)) {
                SLog.d(NAME, "clean block and refresh key. %s. block=%s", str, block.getInfo());
            }
        }
        this.blockList.clear();
        this.visibleRect.setEmpty();
        this.drawRect.setEmpty();
        this.drawSrcRect.setEmpty();
        this.decodeRect.setEmpty();
        this.decodeSrcRect.setEmpty();
    }

    public void decodeCompleted(Block block, Bitmap bitmap, int i) {
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "decode completed. useTime=%dms, block=%s, bitmap=%dx%d(%s), blocks=%d", Integer.valueOf(i), block.getInfo(), Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), bitmap.getConfig() != null ? bitmap.getConfig().name() : null, Integer.valueOf(this.blockList.size()));
        }
        block.bitmap = bitmap;
        block.bitmapDrawSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        block.decoder = null;
        this.blockDisplayer.invalidateView();
        BlockDisplayer.OnBlockChangedListener onBlockChangedListener = this.blockDisplayer.getOnBlockChangedListener();
        if (onBlockChangedListener != null) {
            onBlockChangedListener.onBlockChanged(this.blockDisplayer);
        }
    }

    public void decodeError(Block block, DecodeHandler.DecodeErrorException decodeErrorException) {
        SLog.w(NAME, "decode failed. %s. block=%s, blocks=%d", decodeErrorException.getCauseMessage(), block.getInfo(), Integer.valueOf(this.blockList.size()));
        this.blockList.remove(block);
        block.clean(this.bitmapPool);
        this.blockPool.put(block);
    }

    public long getAllocationByteCount() {
        long j = 0;
        if (this.blockList == null || this.blockList.size() <= 0) {
            return 0L;
        }
        Iterator<Block> it = this.blockList.iterator();
        while (it.hasNext()) {
            if (!it.next().isEmpty()) {
                j += SketchUtils.getByteCount(r3.bitmap);
            }
        }
        return j;
    }

    public void recycle(String str) {
        clean(str);
        this.blockPool.clear();
        this.rectPool.clear();
    }

    public void update(Rect rect, Size size, Size size2, Point point, boolean z) {
        Rect rect2;
        Rect rect3;
        if (z) {
            if (SLog.isLoggable(524290)) {
                SLog.d(NAME, "zooming. newVisibleRect=%s, blocks=%d", rect.toShortString(), Integer.valueOf(this.blockList.size()));
                return;
            }
            return;
        }
        if (this.visibleRect.equals(rect)) {
            if (SLog.isLoggable(1048578)) {
                SLog.d(NAME, "visible rect no changed. update. newVisibleRect=%s, oldVisibleRect=%s", rect.toShortString(), this.visibleRect.toShortString());
                return;
            }
            return;
        }
        this.visibleRect.set(rect);
        int i = point.x;
        int i2 = point.y;
        float width = i / size.getWidth();
        float height = i2 / size.getHeight();
        int width2 = (int) ((rect.width() / this.blockBaseNumber) / 2.0f);
        int height2 = (int) ((rect.height() / this.blockBaseNumber) / 2.0f);
        Rect rect4 = this.rectPool.get();
        rect4.left = Math.max(0, rect.left - width2);
        rect4.top = Math.max(0, rect.top - height2);
        rect4.right = Math.min(size.getWidth(), rect.right + width2);
        rect4.bottom = Math.min(size.getHeight(), rect.bottom + height2);
        if (rect4.isEmpty()) {
            SLog.e(NAME, "newDrawRect is empty. %s", rect4.toShortString());
            return;
        }
        int i3 = this.blockBaseNumber + 1;
        int width3 = rect4.width() / i3;
        int height3 = rect4.height() / i3;
        if (width3 <= 0 || height3 <= 0) {
            SLog.e(NAME, "blockWidth or blockHeight exception. %dx%d", Integer.valueOf(width3), Integer.valueOf(height3));
            return;
        }
        if (rect4.right < size.getWidth()) {
            rect4.right = rect4.left + (i3 * width3);
        } else if (rect4.left > 0) {
            rect4.left = rect4.right - (i3 * width3);
        }
        if (rect4.bottom < size.getHeight()) {
            rect4.bottom = rect4.top + (i3 * height3);
        } else if (rect4.top > 0) {
            rect4.top = rect4.bottom - (i3 * height3);
        }
        Rect rect5 = this.rectPool.get();
        calculateSrcRect(rect5, rect4, i, i2, width, height);
        int calculateInSampleSize = calculateInSampleSize(rect5.width(), rect5.height(), size2.getWidth(), size2.getHeight());
        if (SLog.isLoggable(1048578)) {
            SLog.d(NAME, "update start. newVisibleRect=%s, newDrawRect=%s, oldDecodeRect=%s, inSampleSize=%d, scale=%s, lastScale=%s, blocks=%d", rect.toShortString(), rect4.toShortString(), this.decodeRect.toShortString(), Integer.valueOf(calculateInSampleSize), Float.valueOf(this.blockDisplayer.getZoomScale()), Float.valueOf(this.blockDisplayer.getLastZoomScale()), Integer.valueOf(this.blockList.size()));
        }
        Rect rect6 = this.rectPool.get();
        calculateBlocksDecodeRect(rect6, rect4, width2, height2, width3, height3, size.getWidth(), size.getHeight());
        Rect rect7 = this.rectPool.get();
        calculateSrcRect(rect7, rect6, i, i2, width, height);
        if (rect6.isEmpty()) {
            rect2 = rect7;
            rect3 = rect6;
            if (SLog.isLoggable(1048578)) {
                SLog.d(NAME, "update finished. final draw rect is empty. newDecodeRect=%s", rect3.toShortString());
            }
        } else if (rect6.equals(this.decodeRect)) {
            rect3 = rect6;
            rect2 = rect7;
            if (SLog.isLoggable(1048578)) {
                SLog.d(NAME, "update finished draw rect no change");
            }
        } else {
            recycleBlocks(this.blockList, rect6);
            List<Rect> findEmptyRect = findEmptyRect(rect6, this.blockList);
            if (findEmptyRect == null || findEmptyRect.size() <= 0) {
                rect3 = rect6;
                rect2 = rect7;
                if (SLog.isLoggable(1048578)) {
                    SLog.d(NAME, "not found empty rect");
                }
            } else {
                rect3 = rect6;
                rect2 = rect7;
                loadBlocks(findEmptyRect, width3, height3, i, i2, width, height, calculateInSampleSize, rect3);
            }
            BlockDisplayer.OnBlockChangedListener onBlockChangedListener = this.blockDisplayer.getOnBlockChangedListener();
            if (onBlockChangedListener != null) {
                onBlockChangedListener.onBlockChanged(this.blockDisplayer);
            }
            if (SLog.isLoggable(1048578)) {
                SLog.d(NAME, "update finished, newDecodeRect=%s, blocks=%d", rect3.toShortString(), Integer.valueOf(this.blockList.size()));
            }
        }
        this.drawRect.set(rect4);
        this.drawSrcRect.set(rect5);
        this.decodeRect.set(rect3);
        Rect rect8 = rect2;
        this.decodeSrcRect.set(rect8);
        rect4.setEmpty();
        rect5.setEmpty();
        rect3.setEmpty();
        rect8.setEmpty();
        this.rectPool.put(rect4);
        this.rectPool.put(rect5);
        this.rectPool.put(rect3);
        this.rectPool.put(rect8);
    }
}
