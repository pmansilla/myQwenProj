package com.czw.smartkit.measure;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.czw.smartkit.modes.adapter.BaseListAdapter;
import java.util.List;

/* loaded from: classes.dex */
public abstract class ComAdapter<D> extends BaseListAdapter<D, ItemTag> {

    /* loaded from: classes.dex */
    public static class ItemTag extends BaseListAdapter.Tag {
        private TextView timeTv;
        private TextView valueTv;

        public ItemTag(View view) {
            super(view);
            this.timeTv = (TextView) $View(R.id.time);
            this.valueTv = (TextView) $View(R.id.value);
        }

        public TextView getTimeTv() {
            return this.timeTv;
        }

        public TextView getValueTv() {
            return this.valueTv;
        }
    }

    public ComAdapter(Context context, List<D> list) {
        super(context, list);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
    public ItemTag instanceTag(View view) {
        return new ItemTag(view);
    }

    @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
    public int loadItemView() {
        return R.layout.item_history;
    }
}
