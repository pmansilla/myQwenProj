package com.czw.smartkit.device;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.data.AlarmClock;
import com.czw.smartkit.modes.adapter.BaseListAdapter;
import com.czw.smartkit.preferenceModule.SharePreferenceClock;
import com.suke.widget.SwitchButton;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ClockListActivity extends TitleActivity {
    private ArrayList<AlarmClock> alarmClocks = new ArrayList<>();
    protected ClockAdapter clockAdapter;
    protected ListView clockList;

    /* loaded from: classes.dex */
    static abstract class ClockAdapter extends BaseListAdapter<AlarmClock, ItemTag> {
        public ClockAdapter(Context context, ArrayList<AlarmClock> arrayList) {
            super(context, arrayList);
        }

        @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
        public void handDataAndView(ItemTag itemTag, final AlarmClock alarmClock, final int i) {
            itemTag.time.setText(String.format("%02d:%02d", Integer.valueOf(alarmClock.startHour), Integer.valueOf(alarmClock.startMinute)));
            if (TextUtils.isEmpty(alarmClock.name)) {
                alarmClock.name = this.context.getString(R.string.clock);
            }
            itemTag.name.setText(alarmClock.name);
            itemTag.enable.setChecked(alarmClock.enable);
            itemTag.enable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.device.ClockListActivity.ClockAdapter.1
                @Override // com.suke.widget.SwitchButton.OnCheckedChangeListener
                public void onCheckedChanged(SwitchButton switchButton, boolean z) {
                    alarmClock.enable = z;
                    ClockAdapter.this.onChange();
                }
            });
            for (final int i2 = 0; i2 < 7; i2++) {
                itemTag.repeat[i2].setChecked(alarmClock.cycle[i2]);
                itemTag.repeat[i2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.device.ClockListActivity.ClockAdapter.2
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        alarmClock.cycle[i2] = z;
                        ClockAdapter.this.onChange();
                    }
                });
            }
            itemTag.view.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.device.ClockListActivity.ClockAdapter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ClockAdapter.this.onItemClick(i, alarmClock);
                }
            });
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
        public ItemTag instanceTag(View view) {
            return new ItemTag(view);
        }

        @Override // com.czw.smartkit.modes.adapter.BaseListAdapter
        public int loadItemView() {
            return R.layout.item_clock;
        }

        abstract void onChange();

        abstract void onItemClick(int i, AlarmClock alarmClock);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ItemTag extends BaseListAdapter.Tag {
        private SwitchButton enable;
        private TextView name;
        private CheckBox[] repeat;
        private TextView time;

        public ItemTag(View view) {
            super(view);
            this.repeat = new CheckBox[7];
            this.time = (TextView) $View(R.id.time);
            this.name = (TextView) $View(R.id.name);
            this.enable = (SwitchButton) $View(R.id.swEnable);
            for (int i = 0; i < 7; i++) {
                this.repeat[i] = (CheckBox) $View(R.id.week_1 + i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packData() {
        AlarmClock[] alarmClockArr = new AlarmClock[this.alarmClocks.size()];
        for (int i = 0; i < alarmClockArr.length; i++) {
            alarmClockArr[i] = this.alarmClocks.get(i);
        }
        SharePreferenceClock.save(this.alarmClocks);
        BleManager.getBleManager().writeData(DataStruct.createClock(alarmClockArr));
        try {
            BleManager.getBleManager().writeMuliteData(DataStruct.createClockName(alarmClockArr));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.clock_title);
        this.clockList = (ListView) $View(R.id.clockList);
        this.clockAdapter = new ClockAdapter(this, this.alarmClocks) { // from class: com.czw.smartkit.device.ClockListActivity.1
            @Override // com.czw.smartkit.device.ClockListActivity.ClockAdapter
            void onChange() {
                ClockListActivity.this.packData();
            }

            @Override // com.czw.smartkit.device.ClockListActivity.ClockAdapter
            void onItemClick(int i, AlarmClock alarmClock) {
                ClockListActivity.this.startActivityForResult(new Intent(ClockListActivity.this.getUI(), (Class<?>) ClockEditUI.class).putExtra("alarmClock", alarmClock), i);
            }
        };
        this.clockList.setAdapter((ListAdapter) this.clockAdapter);
        List<AlarmClock> read = SharePreferenceClock.read();
        if (read == null) {
            this.alarmClocks.add(new AlarmClock());
            this.alarmClocks.add(new AlarmClock());
            this.alarmClocks.add(new AlarmClock());
        } else {
            this.alarmClocks.clear();
            this.alarmClocks.addAll(read);
        }
        this.clockAdapter.notifyDataSetChanged();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_clock_list;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 200) {
            this.alarmClocks.set(i, (AlarmClock) intent.getSerializableExtra("alarmClock"));
            this.clockAdapter.notifyDataSetChanged();
            packData();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }
}
