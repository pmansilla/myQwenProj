package cn.smssdk.entity;

import cn.smssdk.utils.e;
import com.mob.MobSDK;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class UiSettings extends BaseEntity {
    private int backgroundImgId;
    private int logoHeight;
    private int logoImgId;
    private int logoWidth;
    private String msgText;
    private int positiveBtnHeight;
    private int positiveBtnImgId;
    private int positiveBtnTextColorId;
    private int positiveBtnTextId;
    private int positiveBtnTextSize;
    private int positiveBtnWidth;
    private int titleTextColorId;
    private int titleTextId;
    private int titleTextSizeDp;
    public static final int DEFAULT_TITLE_TEXT_COLOR_ID = ResHelper.getColorRes(MobSDK.getContext(), "smssdk_common_black");
    public static final int DEFAULT_TITLE_TEXT_SIZE_DP = e.b(e.a("smssdk_authorize_text_size_l"));
    public static final int DEFAULT_TITLE_TEXT_ID = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_authorize_dialog_title");

    /* loaded from: classes.dex */
    public static class Builder extends BaseEntity {
        private int titleTextId = -1;
        private int titleTextColorId = -13430989;
        private int titleTextSizeDp = -1;
        private String msgText = "";
        private int backgroundImgId = -1;
        private int logoImgId = -1;
        private int logoWidth = -1;
        private int logoHeight = -1;
        private int positiveBtnImgId = -1;
        private int positiveBtnTextId = -1;
        private int positiveBtnTextColorId = -1;
        private int positiveBtnTextSize = -1;
        private int positiveBtnWidth = -1;
        private int positiveBtnHeight = -1;

        public UiSettings build() {
            return new UiSettings(this);
        }

        public UiSettings buildDefault() {
            return new UiSettings(new Builder().setTitleTextId(UiSettings.DEFAULT_TITLE_TEXT_ID).setTitleTextColorId(UiSettings.DEFAULT_TITLE_TEXT_COLOR_ID).setTitleTextSizeDp(UiSettings.DEFAULT_TITLE_TEXT_SIZE_DP));
        }

        public Builder setBackgroundImgId(int i) {
            this.backgroundImgId = i;
            return this;
        }

        public Builder setLogoHeight(int i) {
            this.logoHeight = i;
            return this;
        }

        public Builder setLogoImgId(int i) {
            this.logoImgId = i;
            return this;
        }

        public Builder setLogoWidth(int i) {
            this.logoWidth = i;
            return this;
        }

        public Builder setMsgText(String str) {
            this.msgText = str;
            return this;
        }

        public Builder setPositiveBtnHeight(int i) {
            this.positiveBtnHeight = i;
            return this;
        }

        public Builder setPositiveBtnImgId(int i) {
            this.positiveBtnImgId = i;
            return this;
        }

        public Builder setPositiveBtnTextColorId(int i) {
            this.positiveBtnTextColorId = i;
            return this;
        }

        public Builder setPositiveBtnTextId(int i) {
            this.positiveBtnTextId = i;
            return this;
        }

        public Builder setPositiveBtnTextSize(int i) {
            this.positiveBtnTextSize = i;
            return this;
        }

        public Builder setPositiveBtnWidth(int i) {
            this.positiveBtnWidth = i;
            return this;
        }

        public Builder setTitleTextColorId(int i) {
            this.titleTextColorId = i;
            return this;
        }

        public Builder setTitleTextId(int i) {
            this.titleTextId = i;
            return this;
        }

        public Builder setTitleTextSizeDp(int i) {
            this.titleTextSizeDp = i;
            return this;
        }
    }

    private UiSettings(Builder builder) {
        this.titleTextId = builder.titleTextId;
        this.titleTextColorId = builder.titleTextColorId;
        this.titleTextSizeDp = builder.titleTextSizeDp;
        this.msgText = builder.msgText;
        this.logoImgId = builder.logoImgId;
        this.positiveBtnImgId = builder.positiveBtnImgId;
        this.positiveBtnTextId = builder.positiveBtnTextId;
        this.positiveBtnTextColorId = builder.positiveBtnTextColorId;
        this.backgroundImgId = builder.backgroundImgId;
        this.logoWidth = builder.logoWidth;
        this.logoHeight = builder.logoHeight;
        this.positiveBtnTextSize = builder.positiveBtnTextSize;
        this.positiveBtnWidth = builder.positiveBtnWidth;
        this.positiveBtnHeight = builder.positiveBtnHeight;
    }

    public int getBackgroundImgId() {
        return this.backgroundImgId;
    }

    public int getLogoHeight() {
        return this.logoHeight;
    }

    public int getLogoImgId() {
        return this.logoImgId;
    }

    public int getLogoWidth() {
        return this.logoWidth;
    }

    public String getMsgText() {
        return this.msgText;
    }

    public int getPositiveBtnHeight() {
        return this.positiveBtnHeight;
    }

    public int getPositiveBtnImgId() {
        return this.positiveBtnImgId;
    }

    public int getPositiveBtnTextColorId() {
        return this.positiveBtnTextColorId;
    }

    public int getPositiveBtnTextId() {
        return this.positiveBtnTextId;
    }

    public int getPositiveBtnTextSize() {
        return this.positiveBtnTextSize;
    }

    public int getPositiveBtnWidth() {
        return this.positiveBtnWidth;
    }

    public int getTitleTextColorId() {
        return this.titleTextColorId;
    }

    public int getTitleTextId() {
        return this.titleTextId;
    }

    public int getTitleTextSizeDp() {
        return this.titleTextSizeDp;
    }
}
