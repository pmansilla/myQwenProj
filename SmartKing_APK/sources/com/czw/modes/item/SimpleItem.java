package com.czw.modes.item;

/* loaded from: classes.dex */
public class SimpleItem {
    private Class<?> clz;
    private ItemType itemType;
    private int leftIcon;
    private int leftText;
    private int rightIcon;
    private int rightText;

    /* loaded from: classes.dex */
    public enum ItemType {
        TYPE_NORMAl,
        TYPE_RIGHT_SWITCH,
        TYPE_RIGHT_NONE,
        TYPE_RIGHT_NO_ICON,
        TYPE_RIGHT_NO_TEXT
    }

    /* loaded from: classes.dex */
    public static class SimpleItemBuilder {
        private Class<?> clz;
        private ItemType itemType;
        private int leftIcon = 0;
        private int leftText = 0;
        private int rightText = 0;
        private int rightIcon = 0;

        public SimpleItemBuilder(ItemType itemType) {
            this.itemType = itemType;
        }

        public SimpleItem build() {
            SimpleItem simpleItem = new SimpleItem(this.leftIcon, this.leftText, this.rightText, this.rightIcon, this.clz);
            simpleItem.itemType = this.itemType;
            return simpleItem;
        }

        public SimpleItemBuilder setClz(Class<?> cls) {
            this.clz = cls;
            return this;
        }

        public SimpleItemBuilder setItemType(ItemType itemType) {
            this.itemType = itemType;
            return this;
        }

        public SimpleItemBuilder setLeftIcon(int i) {
            this.leftIcon = i;
            return this;
        }

        public SimpleItemBuilder setLeftText(int i) {
            this.leftText = i;
            return this;
        }

        public SimpleItemBuilder setRightIcon(int i) {
            this.rightIcon = i;
            return this;
        }

        public SimpleItemBuilder setRightText(int i) {
            this.rightText = i;
            return this;
        }
    }

    private SimpleItem(int i, int i2, int i3, int i4, Class cls) {
        this.leftIcon = 0;
        this.leftText = 0;
        this.rightText = 0;
        this.rightIcon = 0;
        this.leftIcon = i;
        this.leftText = i2;
        this.rightText = i3;
        this.rightIcon = i4;
        this.clz = cls;
    }

    private SimpleItem(int i, int i2, int i3, int i4, Class<?> cls, ItemType itemType) {
        this.leftIcon = 0;
        this.leftText = 0;
        this.rightText = 0;
        this.rightIcon = 0;
        this.leftIcon = i;
        this.leftText = i2;
        this.rightText = i3;
        this.rightIcon = i4;
        this.clz = cls;
        this.itemType = itemType;
    }

    public Class<?> getClz() {
        return this.clz;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public int getLeftIcon() {
        return this.leftIcon;
    }

    public int getLeftText() {
        return this.leftText;
    }

    public int getRightIcon() {
        return this.rightIcon;
    }

    public int getRightText() {
        return this.rightText;
    }
}
