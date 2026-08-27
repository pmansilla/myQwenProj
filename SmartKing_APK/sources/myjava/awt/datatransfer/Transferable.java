package myjava.awt.datatransfer;

import java.io.IOException;

/* loaded from: classes2.dex */
public interface Transferable {
    Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException;

    DataFlavor[] getTransferDataFlavors();

    boolean isDataFlavorSupported(DataFlavor dataFlavor);
}
