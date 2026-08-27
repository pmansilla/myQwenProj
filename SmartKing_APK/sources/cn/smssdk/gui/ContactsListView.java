package cn.smssdk.gui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ContactsListView extends RelativeLayout {
    private GroupAdapter adapter;
    private int curFirstItem;
    private InnerAdapter innerAdapter;
    private ListView lvBody;
    private AbsListView.OnScrollListener osListener;
    private int titleHeight;
    private TextView tvTitle;

    /* loaded from: classes.dex */
    public static abstract class GroupAdapter {
        protected final ContactsListView view;

        public GroupAdapter(ContactsListView contactsListView) {
            this.view = contactsListView;
        }

        public abstract int getCount(int i);

        public abstract int getGroupCount();

        public abstract String getGroupTitle(int i);

        public abstract Object getItem(int i, int i2);

        public abstract TextView getTitleView(int i, TextView textView, ViewGroup viewGroup);

        public abstract View getView(int i, int i2, View view, ViewGroup viewGroup);

        public void notifyDataSetChanged() {
            this.view.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InnerAdapter extends BaseAdapter {
        private GroupAdapter adapter;
        private ArrayList<Object> listData = new ArrayList<>();
        private ArrayList<Integer> titleIndex = new ArrayList<>();
        private ArrayList<Integer> lastItemIndex = new ArrayList<>();

        public InnerAdapter(GroupAdapter groupAdapter) {
            this.adapter = groupAdapter;
            init();
        }

        private void init() {
            this.listData.clear();
            this.titleIndex.clear();
            this.lastItemIndex.clear();
            int groupCount = this.adapter.getGroupCount();
            for (int i = 0; i < groupCount; i++) {
                int count = this.adapter.getCount(i);
                if (count > 0) {
                    this.titleIndex.add(Integer.valueOf(this.listData.size()));
                    this.listData.add(this.adapter.getGroupTitle(i));
                    for (int i2 = 0; i2 < count; i2++) {
                        this.listData.add(this.adapter.getItem(i, i2));
                    }
                    this.lastItemIndex.add(Integer.valueOf(this.listData.size() - 1));
                }
            }
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.listData.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.listData.get(i);
        }

        public int getItemGroup(int i) {
            int size = this.titleIndex.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (i < this.titleIndex.get(i2).intValue()) {
                    return i2 - 1;
                }
            }
            return size - 1;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i) {
            return !isTitle(i) ? 1 : 0;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            int itemGroup = getItemGroup(i);
            if (isTitle(i)) {
                return (view == null || !(view instanceof TextView)) ? this.adapter.getTitleView(itemGroup, null, viewGroup) : this.adapter.getTitleView(itemGroup, (TextView) view, viewGroup);
            }
            return this.adapter.getView(itemGroup, (i - this.titleIndex.get(itemGroup).intValue()) - 1, view, viewGroup);
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 2;
        }

        public boolean isLastItem(int i) {
            int size = this.lastItemIndex.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (this.lastItemIndex.get(i2).intValue() == i) {
                    return true;
                }
            }
            return false;
        }

        public boolean isTitle(int i) {
            int size = this.titleIndex.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (this.titleIndex.get(i2).intValue() == i) {
                    return true;
                }
            }
            return false;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            init();
            super.notifyDataSetChanged();
        }
    }

    public ContactsListView(Context context) {
        super(context);
        init(context);
    }

    public ContactsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ContactsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.lvBody = new ListView(context);
        this.lvBody.setCacheColorHint(0);
        this.lvBody.setSelector(new ColorDrawable());
        int bitmapRes = ResHelper.getBitmapRes(context, "smssdk_cl_divider");
        if (bitmapRes > 0) {
            this.lvBody.setDivider(context.getResources().getDrawable(bitmapRes));
        }
        this.lvBody.setDividerHeight(1);
        this.lvBody.setVerticalScrollBarEnabled(false);
        this.lvBody.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: cn.smssdk.gui.ContactsListView.1
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                ContactsListView.this.curFirstItem = i;
                if (ContactsListView.this.tvTitle != null) {
                    ContactsListView.this.onScroll();
                }
                if (ContactsListView.this.osListener != null) {
                    ContactsListView.this.osListener.onScroll(absListView, i, i2, i3);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (ContactsListView.this.osListener != null) {
                    ContactsListView.this.osListener.onScrollStateChanged(absListView, i);
                }
            }
        });
        this.lvBody.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        addView(this.lvBody);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDataSetChanged() {
        this.innerAdapter.notifyDataSetChanged();
        setTitle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScroll() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.tvTitle.getLayoutParams();
        if (this.innerAdapter.isLastItem(this.curFirstItem)) {
            this.tvTitle.setText(this.adapter.getGroupTitle(this.innerAdapter.getItemGroup(this.curFirstItem)));
            int top = this.lvBody.getChildAt(1).getTop();
            if (top < this.titleHeight) {
                layoutParams.setMargins(0, top - this.titleHeight, 0, 0);
                this.tvTitle.setLayoutParams(layoutParams);
                return;
            }
        }
        layoutParams.topMargin = 0;
        this.tvTitle.setLayoutParams(layoutParams);
        if (this.innerAdapter.isTitle(this.curFirstItem)) {
            this.tvTitle.setText(this.adapter.getGroupTitle(this.innerAdapter.getItemGroup(this.curFirstItem)));
        }
    }

    private void setTitle() {
        if (this.tvTitle != null) {
            removeView(this.tvTitle);
        }
        if (this.innerAdapter.getCount() == 0) {
            return;
        }
        this.tvTitle = (TextView) this.innerAdapter.getView(((Integer) this.innerAdapter.titleIndex.get(this.innerAdapter.getItemGroup(this.curFirstItem))).intValue(), null, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(9);
        layoutParams.addRule(10);
        addView(this.tvTitle, layoutParams);
        this.tvTitle.measure(0, 0);
        this.titleHeight = this.tvTitle.getMeasuredHeight();
        onScroll();
    }

    public GroupAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(GroupAdapter groupAdapter) {
        this.adapter = groupAdapter;
        this.innerAdapter = new InnerAdapter(groupAdapter);
        this.lvBody.setAdapter((ListAdapter) this.innerAdapter);
        setTitle();
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.osListener = onScrollListener;
    }

    public void setSelection(int i) {
        setSelection(i, -1);
    }

    public void setSelection(int i, int i2) {
        this.lvBody.setSelection(((Integer) this.innerAdapter.titleIndex.get(i)).intValue() + i2 + 1);
    }
}
