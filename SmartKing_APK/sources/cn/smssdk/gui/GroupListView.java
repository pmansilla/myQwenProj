package cn.smssdk.gui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.smssdk.SMSSDK;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GroupListView extends RelativeLayout {
    private GroupAdapter adapter;
    private int curFirstItem;
    private InnerAdapter innerAdapter;
    private ListView lvBody;
    private OnItemClickListener oicListener;
    private AbsListView.OnScrollListener osListener;
    private int titleHeight;
    private View tvTitle;

    /* loaded from: classes.dex */
    public static abstract class GroupAdapter {
        protected final GroupListView view;

        public GroupAdapter(GroupListView groupListView) {
            this.view = groupListView;
        }

        public abstract int getCount(int i);

        public abstract int getGroupCount();

        public abstract String getGroupTitle(int i);

        public abstract Object getItem(int i, int i2);

        public abstract View getSubTitleView(View view, ViewGroup viewGroup, String str);

        public abstract View getTitleView(View view, ViewGroup viewGroup, String str);

        public abstract View getView(View view, ViewGroup viewGroup, String[] strArr);

        public void notifyDataSetChanged() {
            this.view.notifyDataSetChanged();
        }

        public abstract void onGroupChange(View view, String str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InnerAdapter extends BaseAdapter {
        private static final int TYPE_COUNTRY = 1;
        private static final int TYPE_GROUP_TITLE = 0;
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
                        Object item = this.adapter.getItem(i, i2);
                        if (item != null && (item instanceof String[])) {
                            this.listData.add((String[]) this.adapter.getItem(i, i2));
                        }
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
            Object obj = this.listData.get(i);
            return getItemViewType(i) == 0 ? view != null ? this.adapter.getTitleView(view, viewGroup, (String) obj) : this.adapter.getTitleView(null, viewGroup, (String) obj) : this.adapter.getView(view, viewGroup, (String[]) obj);
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

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(GroupListView groupListView, View view, int i, int i2);
    }

    public GroupListView(Context context) {
        super(context);
        init(context);
    }

    public GroupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public GroupListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.lvBody = new ListView(context);
        this.lvBody.setCacheColorHint(0);
        this.lvBody.setSelector(new ColorDrawable());
        this.lvBody.setVerticalScrollBarEnabled(false);
        this.lvBody.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: cn.smssdk.gui.GroupListView.1
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                GroupListView.this.curFirstItem = i;
                if (GroupListView.this.tvTitle != null) {
                    GroupListView.this.onScroll();
                }
                if (GroupListView.this.osListener != null) {
                    GroupListView.this.osListener.onScroll(absListView, i, i2, i3);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (GroupListView.this.osListener != null) {
                    GroupListView.this.osListener.onScrollStateChanged(absListView, i);
                }
            }
        });
        this.lvBody.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: cn.smssdk.gui.GroupListView.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (GroupListView.this.oicListener != null) {
                    GroupListView.this.oicListener.onItemClick(GroupListView.this, view, GroupListView.this.innerAdapter.getItemGroup(i), ((i - ((Integer) GroupListView.this.innerAdapter.titleIndex.get(r1)).intValue()) - 1) - GroupListView.this.lvBody.getHeaderViewsCount());
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
        if (this.lvBody.getHeaderViewsCount() > 0) {
            this.tvTitle.setVisibility(this.curFirstItem > 0 ? 0 : 8);
        }
        if (this.innerAdapter.isLastItem(this.curFirstItem - this.lvBody.getHeaderViewsCount())) {
            this.adapter.onGroupChange(this.tvTitle, this.adapter.getGroupTitle(this.innerAdapter.getItemGroup(this.curFirstItem)));
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
            this.adapter.onGroupChange(this.tvTitle, this.adapter.getGroupTitle(this.innerAdapter.getItemGroup(this.curFirstItem)));
        }
    }

    private void setTitle() {
        if (this.tvTitle != null) {
            removeView(this.tvTitle);
        }
        if (this.innerAdapter.getCount() == 0) {
            return;
        }
        this.tvTitle = this.innerAdapter.getView(((Integer) this.innerAdapter.titleIndex.get(this.innerAdapter.getItemGroup(this.curFirstItem))).intValue(), null, this);
        if (this.lvBody.getHeaderViewsCount() > 0) {
            this.tvTitle.setVisibility(8);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
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

    public void setCurrentCountryId(String str) {
        if (this.lvBody != null) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(1);
            linearLayout.addView(this.adapter.getSubTitleView(null, linearLayout, getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_selected"))));
            linearLayout.addView(this.adapter.getView(null, linearLayout, SMSSDK.getCountry(str)));
            this.lvBody.addHeaderView(linearLayout);
            this.tvTitle.setVisibility(8);
        }
    }

    public void setDivider(Drawable drawable) {
        this.lvBody.setDivider(drawable);
    }

    public void setDividerHeight(int i) {
        this.lvBody.setDividerHeight(i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.oicListener = onItemClickListener;
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
