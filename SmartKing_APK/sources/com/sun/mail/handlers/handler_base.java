package com.sun.mail.handlers;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;

/* loaded from: classes2.dex */
public abstract class handler_base implements DataContentHandler {
    protected Object getData(ActivationDataFlavor activationDataFlavor, DataSource dataSource) throws IOException {
        return getContent(dataSource);
    }

    protected abstract ActivationDataFlavor[] getDataFlavors();

    public Object getTransferData(DataFlavor dataFlavor, DataSource dataSource) throws IOException {
        ActivationDataFlavor[] dataFlavors = getDataFlavors();
        for (int i = 0; i < dataFlavors.length; i++) {
            if (dataFlavors[i].equals(dataFlavor)) {
                return getData(dataFlavors[i], dataSource);
            }
        }
        return null;
    }

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] dataFlavors = getDataFlavors();
        if (dataFlavors.length == 1) {
            return new DataFlavor[]{dataFlavors[0]};
        }
        DataFlavor[] dataFlavorArr = new DataFlavor[dataFlavors.length];
        System.arraycopy(dataFlavors, 0, dataFlavorArr, 0, dataFlavors.length);
        return dataFlavorArr;
    }
}
