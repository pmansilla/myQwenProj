package com.mob.commons.dialog.entity;

@Deprecated
/* loaded from: classes.dex */
public class MobPolicyUi extends BaseEntity {
    private int backgroundColorId;
    private String backgroundColorStr;
    private int negativeBtnColorId;
    private String negativeBtnColorStr;
    private int positiveBtnColorId;
    private String positiveBtnColorStr;

    /* loaded from: classes.dex */
    public static class Builder extends BaseEntity {
        private String backgroundColorStr;
        private String negativeBtnColorStr;
        private String positiveBtnColorStr;
        private int backgroundColorId = -1;
        private int positiveBtnColorId = -1;
        private int negativeBtnColorId = -1;

        public MobPolicyUi build() {
            return new MobPolicyUi(this);
        }

        public Builder setBackgroundColorId(int i) {
            this.backgroundColorId = i;
            return this;
        }

        public Builder setBackgroundColorStr(String str) {
            this.backgroundColorStr = str;
            return this;
        }

        public Builder setNegativeBtnColorId(int i) {
            this.negativeBtnColorId = i;
            return this;
        }

        public Builder setNegativeBtnColorStr(String str) {
            this.negativeBtnColorStr = str;
            return this;
        }

        public Builder setPositiveBtnColorId(int i) {
            this.positiveBtnColorId = i;
            return this;
        }

        public Builder setPositiveBtnColorStr(String str) {
            this.positiveBtnColorStr = str;
            return this;
        }
    }

    private MobPolicyUi(Builder builder) {
        this.backgroundColorId = builder.backgroundColorId;
        this.backgroundColorStr = builder.backgroundColorStr;
        this.positiveBtnColorId = builder.positiveBtnColorId;
        this.positiveBtnColorStr = builder.positiveBtnColorStr;
        this.negativeBtnColorId = builder.negativeBtnColorId;
        this.negativeBtnColorStr = builder.negativeBtnColorStr;
    }

    public int getBackgroundColorId() {
        return this.backgroundColorId;
    }

    public String getBackgroundColorStr() {
        return this.backgroundColorStr;
    }

    public int getNegativeBtnColorId() {
        return this.negativeBtnColorId;
    }

    public String getNegativeBtnColorStr() {
        return this.negativeBtnColorStr;
    }

    public int getPositiveBtnColorId() {
        return this.positiveBtnColorId;
    }

    public String getPositiveBtnColorStr() {
        return this.positiveBtnColorStr;
    }
}
