package com.czw.smartkit.device;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseBleConnTitleActivity;
import com.czw.smartkit.bleModule.data.AlarmClock;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class ClockEditUI extends BaseBleConnTitleActivity {
    private AlarmClock alarmClock = null;
    private CheckBox[] boxs = new CheckBox[7];
    private EditText clockName;
    private TimePicker timePicker;

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.set_clock);
        this.titleBar.setRightText(R.string.ok);
        this.alarmClock = (AlarmClock) getIntent().getSerializableExtra("alarmClock");
        this.timePicker = (TimePicker) $View(R.id.timePicker);
        this.clockName = (EditText) $View(R.id.clockName);
        this.clockName.setText(this.alarmClock.name);
        this.clockName.setSelection(this.clockName.length());
        this.timePicker.setIs24HourView(true);
        this.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: com.czw.smartkit.device.ClockEditUI.1
            @Override // android.widget.TimePicker.OnTimeChangedListener
            public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                ClockEditUI.this.alarmClock.startHour = i;
                ClockEditUI.this.alarmClock.startMinute = i2;
            }
        });
        for (final int i = 0; i < 7; i++) {
            this.boxs[i] = (CheckBox) $View(R.id.week_1 + i);
            this.boxs[i].setChecked(this.alarmClock.cycle[i]);
            this.boxs[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.device.ClockEditUI.2
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    ClockEditUI.this.alarmClock.cycle[i] = z;
                }
            });
        }
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_edit_clock;
    }

    @Override // com.czw.smartkit.base.TitleActivity
    public void onTitleRightClick(View view) {
        try {
            if (this.clockName.getText().toString().getBytes("utf-8").length > 18) {
                toast(R.string.name_too_long);
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.alarmClock.startHour = this.timePicker.getCurrentHour().intValue();
        this.alarmClock.startMinute = this.timePicker.getCurrentMinute().intValue();
        this.alarmClock.name = this.clockName.getText().toString();
        setResult(200, new Intent().putExtra("alarmClock", this.alarmClock));
        finish();
    }
}
