package com.czw.smartkit.statistics;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.czw.modes.pop.smartking.TopFloatPop;
import com.czw.modes.widget.MyViewPager;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseActivity;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.DateUtil;
import com.czw.utils.ViewUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* loaded from: classes.dex */
abstract class BaseHistoryActivity extends BaseActivity implements View.OnClickListener {
    private ImageView centerImg;
    private View centerLayout;
    protected TextView centerText;
    private View dateBarLayout;
    private TextView dateTime;
    private ImageView leftBtn;
    private MyPagerAdapter mAdapter;
    private ImageView rightBtn;
    private RadioGroup time_group;
    private MyViewPager viewPager;
    private int dayType = 0;
    private int dayIndex = 0;
    private int weekIndex = 0;
    private int monthIndex = 0;
    SimpleDateFormat yyMM = new SimpleDateFormat("yyyy-MM");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragments;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> arrayList) {
            super(fragmentManager);
            this.mFragments = null;
            this.mFragments = arrayList;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.mFragments.size();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            return this.mFragments.get(i);
        }
    }

    private void hanWeek() {
        long[] weekdays = DateUtil.getWeekdays(this.weekIndex);
        String format = DateUtil.yyyyMMdd.format(Long.valueOf(weekdays[0]));
        String format2 = DateUtil.yyyyMMdd.format(Long.valueOf(weekdays[6]));
        this.dateTime.setText(format + " ~ " + format2);
        onWeekChoice(weekdays);
    }

    private void handDay() {
        String format = DateUtil.yyyyMMdd.format(DateUtil.getTheDayAfterDate(new Date(), this.dayIndex));
        this.dateTime.setText(format);
        onDayChoice(format);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handDayType() {
        switch (this.dayType) {
            case 1:
                hanWeek();
                return;
            case 2:
                handMonth();
                return;
            default:
                handDay();
                return;
        }
    }

    private void handMonth() {
        String[] split = this.yyMM.format(DateUtil.afterMonthDate(this.monthIndex)).split("-");
        int intValue = Integer.valueOf(split[0]).intValue();
        int intValue2 = Integer.valueOf(split[1]).intValue();
        int lastDayOfMonth = DateUtil.getLastDayOfMonth(intValue, intValue2);
        this.dateTime.setText(intValue + "-" + String.format("%02d", Integer.valueOf(intValue2)));
        onMonthChoice(intValue, intValue2, lastDayOfMonth);
    }

    private void initEvents() {
        this.centerLayout.setOnClickListener(this);
        $View(R.id.leftLayout).setOnClickListener(this);
        this.leftBtn.setOnClickListener(this);
        this.rightBtn.setOnClickListener(this);
        $View(R.id.rightIcon).setOnClickListener(this);
        this.time_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.czw.smartkit.statistics.BaseHistoryActivity.1
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.time_1 /* 2131296882 */:
                        BaseHistoryActivity.this.dayType = 0;
                        BaseHistoryActivity.this.viewPager.setCurrentItem(0);
                        break;
                    case R.id.time_2 /* 2131296883 */:
                        BaseHistoryActivity.this.dayType = 1;
                        BaseHistoryActivity.this.viewPager.setCurrentItem(1);
                        break;
                    case R.id.time_3 /* 2131296884 */:
                        BaseHistoryActivity.this.dayType = 2;
                        BaseHistoryActivity.this.viewPager.setCurrentItem(2);
                        break;
                }
                BaseHistoryActivity.this.handDayType();
            }
        });
    }

    private void showHistoryTypePop() {
        TopFloatPop.getPop(getUI()).showPicker($View(R.id.acv_win), new TopFloatPop.ClickCallback() { // from class: com.czw.smartkit.statistics.BaseHistoryActivity.2
            @Override // com.czw.modes.pop.smartking.TopFloatPop.ClickCallback
            public void onClick(int i) {
                switch (i) {
                    case 0:
                        BaseHistoryActivity.this.jumpAndFinsh(StepHistoryActivity.class);
                        return;
                    case 1:
                        BaseHistoryActivity.this.jumpAndFinsh(SleepHistoryActivity.class);
                        return;
                    case 2:
                        BaseHistoryActivity.this.jumpAndFinsh(TransHistoryActivity.class);
                        return;
                    default:
                        return;
                }
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.centerText = (TextView) $View(R.id.centerText);
        this.centerLayout = $View(R.id.centerLayout);
        this.centerImg = (ImageView) $View(R.id.centerImg);
        this.dateBarLayout = $View(R.id.dateBarLayout);
        this.dateBarLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.leftBtn = (ImageView) $View(R.id.leftBtn);
        this.rightBtn = (ImageView) $View(R.id.rightBtn);
        this.dateTime = (TextView) $View(R.id.dateTime);
        this.time_group = (RadioGroup) $View(R.id.time_group);
        this.viewPager = (MyViewPager) $View(R.id.myViewPager);
        this.leftBtn.setColorFilter(Color.parseColor("#FFFFFFFF"));
        this.rightBtn.setColorFilter(Color.parseColor("#FFFFFFFF"));
        this.dateTime.setTextColor(getResources().getColor(R.color.white));
        this.mAdapter = new MyPagerAdapter(getSupportFragmentManager(), loadFragment());
        this.viewPager.setAdapter(this.mAdapter);
        initEvents();
        handDayType();
    }

    protected abstract ArrayList<Fragment> loadFragment();

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.centerLayout /* 2131296358 */:
                showHistoryTypePop();
                return;
            case R.id.leftBtn /* 2131296561 */:
                if (this.dayType == 0) {
                    this.dayIndex--;
                } else if (this.dayType == 1) {
                    this.weekIndex--;
                } else if (this.dayType == 2) {
                    this.monthIndex--;
                }
                handDayType();
                return;
            case R.id.leftLayout /* 2131296564 */:
                finish();
                return;
            case R.id.rightBtn /* 2131296700 */:
                if (this.dayType == 0) {
                    if (this.dayIndex >= 0) {
                        return;
                    } else {
                        this.dayIndex++;
                    }
                } else if (this.dayType == 1) {
                    if (this.weekIndex >= 0) {
                        return;
                    } else {
                        this.weekIndex++;
                    }
                } else if (this.dayType == 2) {
                    if (this.monthIndex >= 0) {
                        return;
                    } else {
                        this.monthIndex++;
                    }
                }
                handDayType();
                return;
            case R.id.rightIcon /* 2131296701 */:
                SkUtils.showShare(getUI(), ViewUtil.getCacheBitmapFromView($View(R.id.acv_win)));
                return;
            default:
                return;
        }
    }

    protected abstract void onDayChoice(String str);

    protected abstract void onMonthChoice(int i, int i2, int i3);

    protected abstract void onWeekChoice(long[] jArr);
}
