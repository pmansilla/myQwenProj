package cn.sharesdk.onekeyshare.themes.classic;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.CustomerLogo;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class PlatformPage extends OnekeySharePage {
    private Animation animHide;
    private Animation animShow;
    private Runnable beforeFinish;
    private boolean finished;
    private ClassicTheme impl;
    private LinearLayout llPanel;

    public PlatformPage(OnekeyShareThemeImpl onekeyShareThemeImpl) {
        super(onekeyShareThemeImpl);
        this.impl = (ClassicTheme) ResHelper.forceCast(onekeyShareThemeImpl);
    }

    private void initAnims() {
        this.animShow = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        this.animShow.setDuration(300L);
        this.animHide = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        this.animHide.setDuration(300L);
    }

    private boolean isCanShare(Platform platform) {
        String name = platform.getName();
        return ("Cmcc".equals(name) || "Accountkit".equals(name) || "Telecom".equals(name)) ? false : true;
    }

    protected ArrayList<Object> collectCells() {
        ArrayList<Object> arrayList = new ArrayList<>();
        Platform[] platformList = ShareSDK.getPlatformList();
        if (platformList == null) {
            platformList = new Platform[0];
        }
        HashMap<String, String> hiddenPlatforms = getHiddenPlatforms();
        if (hiddenPlatforms == null) {
            hiddenPlatforms = new HashMap<>();
        }
        for (Platform platform : platformList) {
            if (!hiddenPlatforms.containsKey(platform.getName()) && isCanShare(platform)) {
                arrayList.add(platform);
            }
        }
        ArrayList<CustomerLogo> customerLogos = getCustomerLogos();
        if (customerLogos != null && customerLogos.size() > 0) {
            arrayList.addAll(customerLogos);
        }
        return arrayList;
    }

    protected abstract PlatformPageAdapter newAdapter(ArrayList<Object> arrayList);

    @Override // com.mob.tools.FakeActivity
    public void onCreate() {
        this.activity.getWindow().setBackgroundDrawable(new ColorDrawable(1275068416));
        initAnims();
        LinearLayout linearLayout = new LinearLayout(this.activity);
        linearLayout.setOrientation(1);
        this.activity.setContentView(linearLayout);
        TextView textView = new TextView(this.activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.weight = 1.0f;
        textView.setOnClickListener(new View.OnClickListener() { // from class: cn.sharesdk.onekeyshare.themes.classic.PlatformPage.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PlatformPage.this.finish();
            }
        });
        linearLayout.addView(textView, layoutParams);
        this.llPanel = new LinearLayout(this.activity);
        this.llPanel.setOrientation(1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        this.llPanel.setAnimation(this.animShow);
        linearLayout.addView(this.llPanel, layoutParams2);
        MobViewPager mobViewPager = new MobViewPager(this.activity);
        PlatformPageAdapter newAdapter = newAdapter(collectCells());
        this.llPanel.addView(mobViewPager, new LinearLayout.LayoutParams(-1, newAdapter.getPanelHeight()));
        IndicatorView indicatorView = new IndicatorView(this.activity);
        this.llPanel.addView(indicatorView, new LinearLayout.LayoutParams(-1, newAdapter.getBottomHeight()));
        indicatorView.setScreenCount(newAdapter.getCount());
        indicatorView.onScreenChange(0, 0);
        newAdapter.setIndicator(indicatorView);
        mobViewPager.setAdapter(newAdapter);
    }

    @Override // com.mob.tools.FakeActivity
    public boolean onFinish() {
        if (this.finished) {
            this.finished = false;
            return false;
        }
        this.animHide.setAnimationListener(new Animation.AnimationListener() { // from class: cn.sharesdk.onekeyshare.themes.classic.PlatformPage.4
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                if (PlatformPage.this.beforeFinish == null) {
                    ShareSDK.logDemoEvent(2, null);
                } else {
                    PlatformPage.this.beforeFinish.run();
                    PlatformPage.this.beforeFinish = null;
                }
                PlatformPage.this.finished = true;
                PlatformPage.this.finish();
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
        this.llPanel.clearAnimation();
        this.llPanel.setAnimation(this.animHide);
        this.llPanel.setVisibility(8);
        return true;
    }

    public final void performCustomLogoClick(final View view, final CustomerLogo customerLogo) {
        this.beforeFinish = new Runnable() { // from class: cn.sharesdk.onekeyshare.themes.classic.PlatformPage.3
            @Override // java.lang.Runnable
            public void run() {
                customerLogo.listener.onClick(view);
            }
        };
        finish();
    }

    public final void showEditPage(final Platform platform) {
        this.beforeFinish = new Runnable() { // from class: cn.sharesdk.onekeyshare.themes.classic.PlatformPage.2
            @Override // java.lang.Runnable
            public void run() {
                boolean isSilent = PlatformPage.this.isSilent();
                boolean z = platform instanceof CustomPlatform;
                boolean isUseClientToShare = PlatformPage.this.isUseClientToShare(platform);
                if (isSilent || z || isUseClientToShare) {
                    PlatformPage.this.shareSilently(platform);
                    return;
                }
                Platform.ShareParams formateShareData = PlatformPage.this.formateShareData(platform);
                if (formateShareData != null) {
                    ShareSDK.logDemoEvent(3, platform);
                    formateShareData.setOpenCustomEven(true);
                    if (PlatformPage.this.getCustomizeCallback() != null) {
                        PlatformPage.this.getCustomizeCallback().onShare(platform, formateShareData);
                    }
                    PlatformPage.this.impl.showEditPage(PlatformPage.this.activity, platform, formateShareData);
                }
            }
        };
        finish();
    }
}
