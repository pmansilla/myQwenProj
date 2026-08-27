package com.czw.modes.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.czw.R;
import com.czw.modes.adapter.RecycleAdapter;
import com.czw.modes.item.SimpleItem;
import com.suke.widget.SwitchButton;
import java.util.List;
import me.panpf.sketch.SketchImageView;

/* loaded from: classes.dex */
public abstract class ItemAdapter extends RecycleAdapter<SimpleItem, ItemTag> {

    /* loaded from: classes.dex */
    public static class ItemTag extends RecycleAdapter.RecycleTag {
        public View bottomMarginLine;
        public View dirverLine;
        private SketchImageView leftIcon;
        private TextView leftText;
        private ImageView rightIcon;
        private SwitchButton rightSwitch;
        private TextView rightText;
        public View topMarginLine;

        public ItemTag(View view) {
            super(view);
            this.leftIcon = (SketchImageView) $View(R.id.leftIcon);
            this.leftText = (TextView) $View(R.id.leftText);
            this.rightSwitch = (SwitchButton) $View(R.id.rightSwitch);
            this.rightText = (TextView) $View(R.id.rightText);
            this.rightIcon = (ImageView) $View(R.id.rightIcon);
            this.topMarginLine = $View(R.id.topMarginLine);
            this.dirverLine = $View(R.id.dirverLine);
            this.bottomMarginLine = $View(R.id.bottomMarginLine);
        }

        public SketchImageView getLeftIcon() {
            return this.leftIcon;
        }

        public TextView getLeftText() {
            return this.leftText;
        }

        public ImageView getRightIcon() {
            return this.rightIcon;
        }

        public SwitchButton getRightSwitch() {
            return this.rightSwitch;
        }

        public TextView getRightText() {
            return this.rightText;
        }
    }

    public ItemAdapter(Context context, List<SimpleItem> list) {
        super(context, list);
    }

    @Override // com.czw.modes.adapter.RecycleAdapter
    public void handDataAndView(ItemTag itemTag, SimpleItem simpleItem, int i) {
        if (simpleItem.getLeftIcon() == 0) {
            itemTag.getLeftIcon().setVisibility(8);
        } else {
            itemTag.getLeftIcon().setVisibility(0);
            itemTag.getLeftIcon().displayResourceImage(simpleItem.getLeftIcon());
        }
        if (simpleItem.getLeftText() == 0) {
            itemTag.getLeftText().setVisibility(8);
        } else {
            itemTag.getLeftText().setVisibility(0);
            itemTag.getLeftText().setText(simpleItem.getLeftText());
        }
        if (simpleItem.getRightText() == 0) {
            itemTag.getRightText().setVisibility(8);
        } else {
            itemTag.getRightText().setVisibility(0);
            itemTag.getRightText().setText(simpleItem.getRightText());
        }
        if (simpleItem.getRightIcon() == 0) {
            itemTag.getRightIcon().setVisibility(8);
        } else {
            itemTag.getRightIcon().setVisibility(0);
            itemTag.getRightIcon().setImageResource(simpleItem.getRightIcon());
        }
        if (simpleItem.getItemType() == SimpleItem.ItemType.TYPE_RIGHT_SWITCH) {
            itemTag.getRightIcon().setVisibility(8);
            itemTag.getRightText().setVisibility(8);
            itemTag.getRightSwitch().setVisibility(0);
        } else if (simpleItem.getItemType() == SimpleItem.ItemType.TYPE_RIGHT_NONE) {
            itemTag.getRightIcon().setVisibility(8);
            itemTag.getRightText().setVisibility(8);
            itemTag.getRightSwitch().setVisibility(8);
        } else if (simpleItem.getItemType() == SimpleItem.ItemType.TYPE_RIGHT_NO_ICON) {
            itemTag.getRightIcon().setVisibility(8);
            itemTag.getRightText().setVisibility(0);
            itemTag.getRightSwitch().setVisibility(8);
        } else if (simpleItem.getItemType() == SimpleItem.ItemType.TYPE_RIGHT_NO_TEXT) {
            itemTag.getRightIcon().setVisibility(0);
            itemTag.getRightText().setVisibility(8);
            itemTag.getRightSwitch().setVisibility(8);
        } else {
            itemTag.getRightIcon().setVisibility(0);
            itemTag.getRightText().setVisibility(0);
            itemTag.getRightSwitch().setVisibility(8);
        }
        withThisShowAndEvent(itemTag, simpleItem, i);
    }

    @Override // com.czw.modes.adapter.RecycleAdapter
    public ItemTag instanceTag(View view) {
        return new ItemTag(view);
    }

    @Override // com.czw.modes.adapter.RecycleAdapter
    public int loadItemView() {
        return R.layout.item_item;
    }

    protected abstract void withThisShowAndEvent(ItemTag itemTag, SimpleItem simpleItem, int i);
}
