package com.czw.smartkit.views.multiColumnView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MultiColumnBean implements Serializable {
    private String label;
    private List<Float> valueList = new ArrayList();
    private List<Integer> colorList = new ArrayList();

    public MultiColumnBean(String str) {
        this.label = "";
        this.label = str;
    }

    public List<Integer> getColorList() {
        return this.colorList;
    }

    public String getLabel() {
        return this.label;
    }

    public List<Float> getValueList() {
        return this.valueList;
    }

    public void setColorList(List<Integer> list) {
        this.colorList = list;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public void setValueList(List<Float> list) {
        this.valueList = list;
    }
}
