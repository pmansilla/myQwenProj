package cn.smssdk.gui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import cn.smssdk.gui.util.Const;
import com.mob.tools.FakeActivity;
import com.mob.tools.MobUIShell;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;

/* loaded from: classes.dex */
public class AvatarPickerPage extends FakeActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String INTENT_PICK_URL = "INTENT_PICK_URL";
    private GridAdapter adapter;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class GridAdapter extends BaseAdapter {
        private String[] data = Const.AVATOR_ARR;

        GridAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.data != null) {
                return this.data.length;
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.data[i];
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(AvatarPickerPage.this.activity).inflate(ResHelper.getLayoutRes(AvatarPickerPage.this.getContext(), "smssdk_avatar_picker_item"), viewGroup, false);
            }
            Context context = AvatarPickerPage.this.getContext();
            AsyncImageView asyncImageView = (AsyncImageView) view.findViewById(ResHelper.getIdRes(AvatarPickerPage.this.getContext(), "iv_avator_item"));
            asyncImageView.setRound(ResHelper.dipToPx(context, 32));
            asyncImageView.execute((String) getItem(i), ResHelper.getBitmapRes(context, "smssdk_cp_default_avatar"));
            return view;
        }
    }

    private void initView() {
        TextView textView = (TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_left"));
        textView.setText(ResHelper.getStringRes(getContext(), "smssdk_cancel"));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        textView.setOnClickListener(this);
        ((TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_title"))).setText(ResHelper.getStringRes(getContext(), "smssdk_pick_avatar"));
        TextView textView2 = (TextView) findViewById(ResHelper.getIdRes(getContext(), "tv_right"));
        textView2.setText("");
        textView2.setOnClickListener(this);
        GridView gridView = (GridView) findViewById(ResHelper.getIdRes(getContext(), "gv_avator"));
        this.adapter = new GridAdapter();
        gridView.setAdapter((ListAdapter) this.adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (ResHelper.getIdRes(getContext(), "tv_left") == view.getId()) {
            finish();
        }
    }

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        super.onCreate();
        this.activity.setContentView(ResHelper.getLayoutRes(getContext(), "smssdk_avatar_picker_page"));
        initView();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String str = (String) this.adapter.getItem(i);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(INTENT_PICK_URL, str);
        setResult(hashMap);
        sendResult();
        finish();
    }

    public void show(Context context) {
        show(context, new Intent(context, (Class<?>) MobUIShell.class));
    }

    @Override // com.mob.tools.FakeActivity
    public void show(Context context, Intent intent) {
        super.show(context, intent);
    }
}
