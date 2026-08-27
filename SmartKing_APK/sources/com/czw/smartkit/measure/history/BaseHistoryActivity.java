package com.czw.smartkit.measure.history;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.czw.modes.pop.PickerBirthdayPop;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.measure.ComAdapter;
import com.czw.utils.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes.dex */
abstract class BaseHistoryActivity<D> extends TitleActivity implements View.OnClickListener {
    private static final SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
    private TextView dateTimeTv;
    protected View emptyLayout;
    protected ListView listView = null;
    protected ComAdapter<D> adapter = null;
    private int dayIndex = 0;

    private void choiceData() {
        onDayChoice(smp.format(DateUtil.getTheDayAfterDate(new Date(), this.dayIndex)));
    }

    protected void choiceDateImte() {
        PickerBirthdayPop.getPop(getUI()).showPicker($View(R.id.act_win), "2018-03-07", new PickerBirthdayPop.ClickCallback() { // from class: com.czw.smartkit.measure.history.BaseHistoryActivity.1
            @Override // com.czw.modes.pop.PickerBirthdayPop.ClickCallback
            public void getBirthDate(int i, int i2, int i3) {
                String format = String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
                DateUtil.parserFromStr("yyyy-MM-dd", format);
                BaseHistoryActivity.this.onDayChoice(format);
            }
        });
    }

    protected abstract ComAdapter<D> getAdapter();

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.dateTimeTv = (TextView) $View(R.id.dateTime);
        this.dateTimeTv.setTextColor(getResources().getColor(R.color.black));
        this.listView = (ListView) $View(R.id.listView);
        this.emptyLayout = $View(R.id.emptyLayout);
        this.emptyLayout = $View(R.id.emptyLayout);
        $View(R.id.leftBtn).setOnClickListener(this);
        $View(R.id.rightBtn).setOnClickListener(this);
        this.dateTimeTv.setOnClickListener(this);
        this.adapter = getAdapter();
        this.listView.setAdapter((ListAdapter) this.adapter);
        choiceData();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.dateTime) {
            choiceDateImte();
            return;
        }
        if (id == R.id.leftBtn) {
            this.dayIndex--;
            choiceData();
        } else if (id == R.id.rightBtn && this.dayIndex < 0) {
            this.dayIndex++;
            choiceData();
        }
    }

    public void onDayChoice(String str) {
        this.dateTimeTv.setText(str);
    }
}
