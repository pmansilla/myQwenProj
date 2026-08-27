package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.nio.charset.Charset;
import org.apache.harmony.awt.ContextStorage;
import org.apache.harmony.awt.internal.nls.Messages;
import org.apache.harmony.misc.SystemUtils;

/* loaded from: classes2.dex */
public abstract class DTK {
    protected SystemFlavorMap systemFlavorMap;
    private NativeClipboard nativeClipboard = null;
    private NativeClipboard nativeSelection = null;
    protected final DataTransferThread dataTransferThread = new DataTransferThread(this);

    protected DTK() {
        this.dataTransferThread.start();
    }

    private static DTK createDTK() {
        String str;
        switch (SystemUtils.getOS()) {
            case 1:
                str = "org.apache.harmony.awt.datatransfer.windows.WinDTK";
                break;
            case 2:
                str = "org.apache.harmony.awt.datatransfer.linux.LinuxDTK";
                break;
            default:
                throw new RuntimeException(Messages.getString("awt.4E"));
        }
        try {
            return (DTK) Class.forName(str).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DTK getDTK() {
        synchronized (ContextStorage.getContextLock()) {
            if (ContextStorage.shutdownPending()) {
                return null;
            }
            DTK dtk = ContextStorage.getDTK();
            if (dtk == null) {
                dtk = createDTK();
                ContextStorage.setDTK(dtk);
            }
            return dtk;
        }
    }

    protected void appendSystemFlavorMap(SystemFlavorMap systemFlavorMap, DataFlavor dataFlavor, String str) {
        systemFlavorMap.addFlavorForUnencodedNative(str, dataFlavor);
        systemFlavorMap.addUnencodedNativeForFlavor(dataFlavor, str);
    }

    protected void appendSystemFlavorMap(SystemFlavorMap systemFlavorMap, String[] strArr, String str, String str2) {
        TextFlavor.addUnicodeClasses(systemFlavorMap, str2, str);
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] != null && Charset.isSupported(strArr[i])) {
                TextFlavor.addCharsetClasses(systemFlavorMap, str2, str, strArr[i]);
            }
        }
    }

    public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent);

    public abstract DropTargetContextPeer createDropTargetContextPeer(DropTargetContext dropTargetContext);

    protected String[] getCharsets() {
        return new String[]{"UTF-16", "UTF-8", "unicode", "ISO-8859-1", "US-ASCII"};
    }

    public String getDefaultCharset() {
        return "unicode";
    }

    public NativeClipboard getNativeClipboard() {
        if (this.nativeClipboard == null) {
            this.nativeClipboard = newNativeClipboard();
        }
        return this.nativeClipboard;
    }

    public NativeClipboard getNativeSelection() {
        if (this.nativeSelection == null) {
            this.nativeSelection = newNativeSelection();
        }
        return this.nativeSelection;
    }

    public synchronized SystemFlavorMap getSystemFlavorMap() {
        return this.systemFlavorMap;
    }

    public abstract void initDragAndDrop();

    public void initSystemFlavorMap(SystemFlavorMap systemFlavorMap) {
        String[] charsets = getCharsets();
        appendSystemFlavorMap(systemFlavorMap, DataFlavor.stringFlavor, "text/plain");
        appendSystemFlavorMap(systemFlavorMap, charsets, "plain", "text/plain");
        appendSystemFlavorMap(systemFlavorMap, charsets, "html", "text/html");
        appendSystemFlavorMap(systemFlavorMap, DataProvider.urlFlavor, "application/x-java-url");
        appendSystemFlavorMap(systemFlavorMap, charsets, "uri-list", "application/x-java-url");
        appendSystemFlavorMap(systemFlavorMap, DataFlavor.javaFileListFlavor, "application/x-java-file-list");
        appendSystemFlavorMap(systemFlavorMap, DataFlavor.imageFlavor, "image/x-java-image");
    }

    protected abstract NativeClipboard newNativeClipboard();

    protected abstract NativeClipboard newNativeSelection();

    public abstract void runEventLoop();

    public synchronized void setSystemFlavorMap(SystemFlavorMap systemFlavorMap) {
        this.systemFlavorMap = systemFlavorMap;
    }
}
